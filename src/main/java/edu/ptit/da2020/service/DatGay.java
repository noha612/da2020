package edu.ptit.da2020.service;

import edu.ptit.da2020.astar.Graph;
import edu.ptit.da2020.astar.RouteFinder;
import edu.ptit.da2020.entity.Station;
import edu.ptit.da2020.utils.HaversineScorer;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

@Service
public class DatGay {

    private Graph<Station> underground;

    private RouteFinder<Station> routeFinder;

    private Map<String, String> locations;

    @SneakyThrows
    @PostConstruct
    public void setUp() {
        System.out.println("set up graph...");
        Set<Station> stations = new HashSet<>();
        Map<String, Set<String>> connections = new HashMap<>();
        locations = new HashMap<>();

        File fXmlFile = new File("src/main/resources/map/mapPTIT.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();

        NodeList nList;
        //get node
        nList = doc.getElementsByTagName("node");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node n = nList.item(temp);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                String id = e.getAttribute("id");
                double lat = Double.parseDouble(e.getAttribute("lat"));
                double lon = Double.parseDouble(e.getAttribute("lon"));
                stations.add(new Station(id, id, lat, lon));
            }
        }

        //get way
        nList = doc.getElementsByTagName("way");

        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node n = nList.item(temp);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;

                //construct graph
                NodeList nd = e.getElementsByTagName("nd");
                Set<String> set = new LinkedHashSet<>();

                for (int temp2 = 0; temp2 < nd.getLength(); temp2++) {
                    Node n2 = nd.item(temp2);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        Element e2 = (Element) n2;
                        String nodeId = e2.getAttribute("ref");
                        set.add(nodeId);
                    }
                }

                //set connector
                for (String i : set) {
                    if (connections.containsKey(i)) {
                        for (String j : set) {
                            if (!i.equals(j)) connections.get(i).add(j);
                        }
                    } else {
                        connections.put(i, new HashSet<>(set));
                        connections.get(i).remove(i);
                    }
                }

                //construct locations
                NodeList tag = e.getElementsByTagName("tag");
                for (int temp2 = 0; temp2 < tag.getLength(); temp2++) {
                    Node n2 = tag.item(temp2);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        Element e2 = (Element) n2;
                        if ("name".equals(e2.getAttribute("k"))) {
                            locations.put(e2.getAttribute("v"), String.valueOf(set.toArray()[0]));
                        }

                    }
                }
            }
        }

        underground = new Graph<>(stations, connections);
        routeFinder = new RouteFinder<>(underground, new HaversineScorer(), new HaversineScorer());
        System.out.println("Done set up graph!");
    }

    public List<Station> findRoute(String startId, String finishId) {
        return routeFinder.findRoute(underground.getNode(startId), underground.getNode(finishId));
    }

    public String findIdByName(String name) {
        for (String i : locations.keySet()) {
            if (i.contains(name)) return i + " " + locations.get(i);
        }
        for (String i : locations.keySet()) {
            if (i.toLowerCase().contains(name.toLowerCase())) return i + " " + locations.get(i);
        }
        return "Not found id";
    }
}
