package edu.ptit.da2020;

import edu.ptit.da2020.config.DataLoader;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestSomething {
    private static final String MAP_FILE = "src/main/resources/map/mapPTIT.xml";
    private static final String EDGE = "src/main/resources/map/HN_edge_raw.txt";
    private static final String RAW_NODE = "src/main/resources/map/PTIT_raw_node.txt";
    @Autowired
    DataLoader dataLoader;

    @Test
    public void contextLoads() {

    }

    @Test
    public void aaa() {
        genV();
        Set<String> set = new LinkedHashSet<>();
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
                fw.write(i + " " + 1 + "\n");
            }
            log.info("done");
        } catch (IOException e) {
            log.error("An error occurred " + e);
            e.printStackTrace();
        }
        log.info("done write file " + EDGE);
    }



    private static void genV() {
        Set<String> set = new LinkedHashSet<>();
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

}
