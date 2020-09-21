package edu.ptit.da2020.config;

import edu.ptit.da2020.constant.OsmXMLConstant;
import edu.ptit.da2020.model.Intersection;
import edu.ptit.da2020.model.graphmodel.Graph;
import edu.ptit.da2020.util.HaversineScorer;
import edu.ptit.da2020.util.algorithm.RouteFinder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

@Configuration
@Getter
@Setter
@Slf4j
public class GraphConfiguration {

    @Value("${mapXMLfile}")
    String mapXMLFile;

    private Graph<Intersection> map;

    private Map<String, String> locations;

    private RouteFinder<Intersection> routeFinder;
    private Set<Intersection> intersections;
    private Map<String, Set<String>> connections;
    private Document doc;
    private DocumentBuilder dBuilder;
    private DocumentBuilderFactory dbFactory;
    private File fXmlFile;


    @SneakyThrows
    @PostConstruct
    public void initMap() {
        log.info("");
        fXmlFile = new File(mapXMLFile);
        dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();

        initIntersections();
        initConnectionsAndLocations();

        map = new Graph<>(intersections, connections);

        routeFinder = new RouteFinder<>(map, new HaversineScorer(), new HaversineScorer());
    }

    private void initIntersections() {
        intersections = new HashSet<>();
        NodeList nodes = doc.getElementsByTagName(OsmXMLConstant.OSM_NODE);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String id = ((Element) node).getAttribute(OsmXMLConstant.OSM_ID);
                double lat = Double.parseDouble(((Element) node).getAttribute(OsmXMLConstant.OSM_LATITUDE));
                double lng = Double.parseDouble(((Element) node).getAttribute(OsmXMLConstant.OSM_LONGITUDE));
                intersections.add(new Intersection(id, id, lat, lng));
            }
        }
    }

    private void initConnectionsAndLocations() {
        connections = new HashMap<>();
        locations = new HashMap<>();
        NodeList ways = doc.getElementsByTagName(OsmXMLConstant.OSM_WAY);

        for (int i = 0; i < ways.getLength(); i++) {

            Node way = ways.item(i);
            if (way.getNodeType() == Node.ELEMENT_NODE) {
                NodeList nds = ((Element) way).getElementsByTagName(OsmXMLConstant.OSM_WAY_NODE);
                Set<String> nodeInWay = new LinkedHashSet<>();
                for (int j = 0; j < nds.getLength(); j++) {
                    Node nd = nds.item(j);
                    if (nd.getNodeType() == Node.ELEMENT_NODE) {
                        String nodeId = ((Element) nd).getAttribute(OsmXMLConstant.OSM_WAY_REF);
                        nodeInWay.add(nodeId);
                    }
                }

                updateConnections(nodeInWay);
                updateLocations((Element) way, nodeInWay);
            }
        }
    }

    private void updateConnections(Set<String> nodeInWay) {
        for (String nodeId : nodeInWay) {
            if (connections.containsKey(nodeId)) {
                for (String j : nodeInWay) {
                    if (!nodeId.equals(j)) connections.get(nodeId).add(j);
                }
            } else {
                connections.put(nodeId, new HashSet<>(nodeInWay));
                connections.get(nodeId).remove(nodeId);
            }
        }
    }

    private void updateLocations(Element way, Set<String> nodeInWay) {
        NodeList tags = way.getElementsByTagName(OsmXMLConstant.OSM_WAY_TAG);
        for (int i = 0; i < tags.getLength(); i++) {
            Node tag = tags.item(i);
            if (tag.getNodeType() == Node.ELEMENT_NODE) {
                if (OsmXMLConstant.OSM_TAG_KEY_NAME.equals(
                        ((Element) tag).getAttribute(OsmXMLConstant.OSM_TAG_KEY))
                ) {
                    locations.put(
                            ((Element) tag).getAttribute(OsmXMLConstant.OSM_TAG_VALUE),
                            String.valueOf(nodeInWay.toArray()[0])
                    );
                }
            }
        }

    }
}
