package edu.ptit.da2020.init;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

@Configuration
@Data
@Slf4j
@Order(1)
public class DataInit {

    private static final String VERTEX = "src/main/resources/map/HN_vertex.txt";
    private static final String EDGE = "src/main/resources/map/HN_edge.txt";
    private static final String INVERTED = "src/main/resources/map/inverted_index.txt";
    private static final String NAME = "src/main/resources/map/HN_name.txt";

    private Map<String, Double[]> listV;
    private Map<String, String[]> listE;
    private Map<String, Integer[]> ii;
    private Map<Integer, String> listName;
    private Map<String, Integer> listCongestions;

    @PostConstruct
    public void initGraph() {
        log.info("init graph...");

        loadVertex();
        loadEdge();
        loadInvertedKey();
        loadName();
        loadCongestion();
    }

    private void loadVertex() {
        listV = new LinkedHashMap<>();

        log.info("start read file " + VERTEX);
        try {
            File myObj = new File(VERTEX);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine().trim();
                if (StringUtils.isNotEmpty(line)) {
                    String[] temp = line.split(" ");
                    String key = temp[0];
                    Double[] array = new Double[2];
                    for (int i = 0; i < array.length; i++) array[i] = Double.parseDouble(temp[i + 1]);
                    listV.put(key, array);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.error("An error occurred " + e);
        }
        log.info("done read file " + VERTEX + ", total V: " + listV.size());
    }

    private void loadEdge() {
        listE = new HashMap<>();

        log.info("start read file " + EDGE);
        try {
            File myObj = new File(EDGE);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine().trim();
                if (StringUtils.isNotEmpty(line)) {
                    String[] temp = line.split(" ");
                    listE.put(temp[0] + "_" + temp[1], temp);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.error("An error occurred " + e);
        }
        log.info("done read file " + EDGE + ", total star: " + listE.size());
    }

    private void loadInvertedKey() {
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

    private void loadName() {
        listName = new HashMap<>();

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
                    listName.put(key, value);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.error("An error occurred " + e);
        }
        log.info("done read file " + NAME);

    }

    public void loadCongestion() {
        listCongestions = new HashMap<>();

        log.info("start read file " + EDGE);
        try {
            File myObj = new File(EDGE);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine().trim();
                if (StringUtils.isNotEmpty(line)) {
                    String[] temp = line.split(" ");
                    listCongestions.put(temp[0] + "_" + temp[1], Integer.parseInt(temp[2]));
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.error("An error occurred " + e);
        }
        log.info("done read file " + EDGE + ", total star: " + listE.size());
    }
}
