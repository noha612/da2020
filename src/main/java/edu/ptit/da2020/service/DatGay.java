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

    @SneakyThrows
    @PostConstruct
    public void setUp() {
        System.out.println("set up graph...");
        Set<Station> stations = new HashSet<>();
        Map<String, Set<String>> connections = new HashMap<>();

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
                String id = e.getAttribute("id");

                NodeList nd = e.getElementsByTagName("nd");
                Set<String> set = new HashSet<>();

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
            }
        }

        underground = new Graph<>(stations, connections);
        routeFinder = new RouteFinder<>(underground, new HaversineScorer(), new HaversineScorer());
        System.out.println("Done set up graph!");
    }

    public List<Station> findRoute(String startId, String finishId) {
        return routeFinder.findRoute(underground.getNode(startId), underground.getNode(finishId));
    }
}
