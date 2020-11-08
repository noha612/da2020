package edu.ptit.da2020.trash_package;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Slf4j
public class Gen {
    private static final String MAP_FILE = "C:\\Users\\ntnhu\\Documents\\doan\\map";
    //    private static final String MAP_FILE = "src/main/resources/map/mapPTIT";
    private static final String RAW_NODE = "src/main/resources/raw_node.txt";
    private static final String EDGE = "src/main/resources/HN_edge.txt";
    private static final String VERTEX = "src/main/resources/HN_vertex.txt";
    private static final String NAME = "src/main/resources/HN_name.txt";
    static LinkedHashSet<String> set;

    public static void main(String[] args) {
        genV();
        genE();
        filter();
        genN();
    }

    private static void genV() {
        set = new LinkedHashSet<>();
        log.info("start read file " + MAP_FILE);
        try {
            File myObj = new File(MAP_FILE);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine().trim();
                if (line.startsWith("<node")) {
                    String[] temp = line.split(" ");
                    set.add(
                            temp[1].substring(temp[1].indexOf("\"") + 1, temp[1].length() - 1) + " " +
                                    temp[2].substring(temp[2].indexOf("\"") + 1, temp[2].length() - 1) + " " +
                                    temp[3].substring(temp[3].indexOf("\"") + 1, temp[3].length() - 1)
                    );
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.error("An error occurred " + e);
        }
        log.info("done read file " + MAP_FILE);

        log.info("start write file " + RAW_NODE);
        try {
            File f = new File(RAW_NODE);
            if (f.createNewFile()) {
                log.info("File created " + f.getName());
            } else {
                log.info("File already exists " + f.getName());
            }
        } catch (IOException e) {
            log.error("An error occurred " + e);
        }
        try (
                FileWriter fw = new FileWriter(RAW_NODE)
        ) {
            log.info("begin insert raw node, size: " + set.size());
            for (String i : set) {
                fw.write(i + "\n");
            }
            log.info("done");
        } catch (IOException e) {
            log.error("An error occurred.");
            e.printStackTrace();
        }
        log.info("done write file");
    }

    private static void genE() {
        set = new LinkedHashSet<>();
        log.info("start read file " + MAP_FILE);
        try {
            File myObj = new File(MAP_FILE);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String way = myReader.nextLine().trim();
                if (way.startsWith("<way")) {
                    ArrayList<String> nodeInWay = new ArrayList<>();
                    String line = myReader.nextLine().trim();
                    while (!line.startsWith("</way")) {
                        if (line.startsWith("<nd")) {
                            line = line.replace("<nd ref=\"", "");
                            line = line.replace("\"/>", "");
                            nodeInWay.add(line);
                        }
                        line = myReader.nextLine().trim();
                    }
                    if (!nodeInWay.get(0).equalsIgnoreCase(nodeInWay.get(nodeInWay.size() - 1))) {
                        for (int i = 0; i < nodeInWay.size(); i++) {
                            if (i < nodeInWay.size() - 1) set.add(nodeInWay.get(i) + " " + nodeInWay.get(i + 1));
                            if (i > 0) set.add(nodeInWay.get(i) + " " + nodeInWay.get(i - 1));
                        }
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.error("An error occurred " + e);
        }
        log.info("done read file " + MAP_FILE);

        log.info("start write file " + EDGE);
        try {
            File f = new File(EDGE);
            if (f.createNewFile()) {
                log.info("File created " + f.getName());
            } else {
                log.info("File already exists " + f.getName());
            }
        } catch (IOException e) {
            log.error("An error occurred " + e);
        }
        try (
                FileWriter fw = new FileWriter(EDGE)
        ) {
            log.info("begin insert E, size: " + set.size());
            for (String i : set) {
                fw.write(i + "\n");
            }
            log.info("done");
        } catch (IOException e) {
            log.error("An error occurred " + e);
            e.printStackTrace();
        }
        log.info("done write file " + EDGE);
    }

    private static void genN() {
        Map<String, String> map = new LinkedHashMap<>();
        log.info("start read file " + MAP_FILE);
        try {
            File myObj = new File(MAP_FILE);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String way = myReader.nextLine().trim();
                if (way.startsWith("<way")) {
                    ArrayList<String> nodeInWay = new ArrayList<>();
                    String line = myReader.nextLine().trim();
                    while (!line.startsWith("</way")) {
                        if (line.startsWith("<nd")) {
                            line = line.replace("<nd ref=\"", "");
                            line = line.replace("\"/>", "");
                            nodeInWay.add(line);
                        }
                        if (!nodeInWay.get(0).equalsIgnoreCase(nodeInWay.get(nodeInWay.size() - 1))) {
                            if (line.startsWith("<tag k=\"name\" v=\"")) {
                                line = line.replace("<tag k=\"name\" v=\"", "");
                                line = line.replace("\"/>", "");
                                if (!map.containsKey(line)) map.put(line, nodeInWay.get(nodeInWay.size() / 2));
                            }
                        }
                        line = myReader.nextLine().trim();
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.error("An error occurred " + e);
        }
        log.info("done read file " + MAP_FILE);

        log.info("start write file " + NAME);
        try {
            File f = new File(NAME);
            if (f.createNewFile()) {
                log.info("File created " + f.getName());
            } else {
                log.info("File already exists " + f.getName());
            }
        } catch (IOException e) {
            log.error("An error occurred " + e);
        }
        try (
                FileWriter fw = new FileWriter(NAME);
        ) {
            log.info("begin insert E, size: " + map.size());
            int count = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                fw.write(count + "::" + entry.getKey() + "::" + entry.getValue() + "\n");
                count++;
            }
            log.info("done");
        } catch (IOException e) {
            log.error("An error occurred " + e);
            e.printStackTrace();
        }
        log.info("done write file " + NAME);
    }

    private static void filter() {
        Set<String> keys = new HashSet<>();
        List<String> filter = new ArrayList<>();

        log.info("start read file " + EDGE);
        try {
            File myObj = new File(EDGE);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine().trim();
                if (StringUtils.isNotEmpty(line)) {
                    String[] temp = line.split(" ");
                    keys.add(temp[0]);
                    keys.add(temp[1]);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.error("An error occurred " + e);
        }
        log.info("done read file " + EDGE);


        log.info("start read file " + RAW_NODE);
        try {
            File myObj = new File(RAW_NODE);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine().trim();
                if (StringUtils.isNotEmpty(line)) {
                    String[] temp = line.split(" ");
                    if (keys.contains(temp[0])) {
                        filter.add(temp[0] + " " + temp[1] + " " + temp[2]);
                    }
                }
            }
            myReader.close();
        } catch (
                FileNotFoundException e) {
            log.error("An error occurred " + e);
        }

        log.info("start write file " + VERTEX);
        try {
            File f = new File(VERTEX);
            if (f.createNewFile()) {
                log.info("File created " + f.getName());
            } else {
                log.info("File already exists " + f.getName());
            }
        } catch (IOException e) {
            log.error("An error occurred " + e);
        }
        try (
                FileWriter fw = new FileWriter(VERTEX);
        ) {
            log.info("begin insert V, size: " + filter.size());
            for (String i : filter) {
                fw.write(i + "\n");
            }
            log.info("done");
        } catch (IOException e) {
            log.error("An error occurred.");
            e.printStackTrace();
        }
        log.info("done write file");
    }
}

