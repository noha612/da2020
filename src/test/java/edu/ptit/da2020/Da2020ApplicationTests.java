package edu.ptit.da2020;

import edu.ptit.da2020.entity.Node;
import edu.ptit.da2020.repository.NodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

@SpringBootTest
class Da2020ApplicationTests {
    @Autowired
    NodeRepository nodeRepository;

    @Test
    void contextLoads() {
    }

    @Test
    public void testDB() {
//        nodeRepository.save(new Node(1, 2, 3));
    }

    @Test
    public void addition_isCorrect() {

        HashMap<String, HashSet<String>> graph;


        try {
            graph = new HashMap<>();

            File fXmlFile = new File("src/main/resources/map/mapPTIT.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList;
            nList = doc.getElementsByTagName("node");
            System.out.println(nList.getLength());
            List<Node> listNode = new ArrayList<>();

            for (int temp = 0; temp < nList.getLength(); temp++) {
//
                org.w3c.dom.Node n = nList.item(temp);
//
                if (n.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
//
                    Element e = (Element) n;
                    String id = e.getAttribute("id");
                    double lat = Double.parseDouble(e.getAttribute("lat"));
                    double lon = Double.parseDouble(e.getAttribute("lon"));
                    listNode.add(new Node(id, lat, lon));
                }
//                if (temp % 1000  == 0) {
//                    System.out.println(temp);
//                    nodeRepository.saveAll(listNode);
//                    listNode.clear();
//                }
            }
            nodeRepository.saveAll(listNode);
//
//                    HashSet<String> set = new HashSet<>();
//
//                    for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {
//
//
//                        Node nNode2 = nList2.item(temp2);
//
//                        if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
//
//                            Element eElement2 = (Element) nNode2;
//                            String id = eElement2.getAttribute("ref");
//                            set.add(id);
//
//                        }
//                    }
//
//                    for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {
//
//
//                        Node nNode2 = nList2.item(temp2);
//
//                        if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
//
//                            Element eElement2 = (Element) nNode2;
//                            String id = eElement2.getAttribute("ref");
//                            HashSet<String> tempSet = (HashSet<String>) set.clone();
//                            tempSet.remove(id);
//
//                            if (graph.get(id) == null) {
//                                graph.put(id, tempSet);
//                            } else {
//                                HashSet<String> idSet = graph.get(id);
//                                for (String i : tempSet) idSet.add(i);
//                                graph.put(id, idSet);
//                            }
//
//                        }
//                    }
//                }
//
//                for (String i : graph.keySet()) {
//                    System.out.print(i + ": ");
//                    System.out.print(graph.get(i));
//                    System.out.println();
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
