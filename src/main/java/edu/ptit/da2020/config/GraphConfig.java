package edu.ptit.da2020.config;

import edu.ptit.da2020.model.entity.Intersection;
import edu.ptit.da2020.model.graph.Graph;
import edu.ptit.da2020.util.algorithm.RouteFinder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Configuration
@Getter
@Setter
@Slf4j
public class GraphConfig {

    private Graph<Intersection> map;
    private Map<String, String> locations;
    private RouteFinder<Intersection> routeFinder;
    private Set<Intersection> intersections;
    private ArrayList<Intersection> al;
    private Map<String, Set<String>> connections;
    private Map<String, Double> realTimeCost;

    private static final String VERTEX = "HN_vertex.txt";
    private static final String EDGE = "HN_edge.txt";
    private static final String INVERTED = "inverted_index.txt";
    private static final String NAME = "HN_name.txt";

    private Map<String, Integer[]> ii;
    private Map<Integer, String> ln;

    @SneakyThrows
    @PostConstruct
    public void initGraph() {
        log.info("init graph...");
        initConnectionsFromFile();
        initIntersectionsFromFile();
        map = new Graph<>(intersections, connections);

        initII();
        initN();
    }


    private void initIntersectionsFromFile() {
        intersections = new LinkedHashSet<>();

        log.info("start read file " + VERTEX);
        try {
            File myObj = new File(VERTEX);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine().trim();
                if (StringUtils.isNotEmpty(line)) {
                    String[] temp = line.split(" ");
                    if (connections.containsKey(temp[0])) {
                        intersections.add(
                                new Intersection(
                                        temp[0],
                                        Double.parseDouble(temp[1]),
                                        Double.parseDouble(temp[2])
                                )
                        );
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.error("An error occurred " + e);
        }
        log.info("done read file " + VERTEX + ", total V: " + intersections.size());
    }


    private void initConnectionsFromFile() {
        connections = new HashMap<>();
        locations = new HashMap<>();

        log.info("start read file " + EDGE);
        try {
            File myObj = new File(EDGE);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine().trim();
                if (StringUtils.isNotEmpty(line)) {
                    String[] temp = line.split(" ");
                    if (connections.containsKey(temp[0])) {
                        connections.get(temp[0]).add(temp[1]);
                    } else {
                        Set<String> tempSet = new HashSet<>();
                        tempSet.add(temp[1]);
                        connections.put(temp[0], tempSet);
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.error("An error occurred " + e);
        }
        log.info("done read file " + EDGE + ", total star: " + connections.size());
    }

    private void initII() {
        ii = new HashMap<>();

        log.info("start read file " + INVERTED);
        try {
            File myObj = new File(INVERTED);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine().trim();
                if (StringUtils.isNotEmpty(line)) {
                    String[] temp = line.split(" = ");
                    String key = temp[0];
                    String[] arrayString = temp[1].substring(1, temp[1].length() - 1).split(",");
                    Integer[] array = new Integer[arrayString.length];
                    for (int i = 0; i < array.length; i++) array[i] = Integer.parseInt(arrayString[i]);
                    ii.put(key, array);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.error("An error occurred " + e);
        }
        log.info("done read file " + INVERTED);

    }

    private void initN() {
        ln = new HashMap<>();

        log.info("start read file " + NAME);
        try {
            File myObj = new File(NAME);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine().trim();
                if (StringUtils.isNotEmpty(line)) {
                    String[] temp = line.split("::");
                    Integer key = Integer.parseInt(temp[0]);
                    String value = temp[1] + "::" + temp[2];
                    ln.put(key, value);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.error("An error occurred " + e);
        }
        log.info("done read file " + NAME);

    }
}
