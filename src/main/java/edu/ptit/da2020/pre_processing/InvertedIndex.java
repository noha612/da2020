package edu.ptit.da2020.pre_processing;

import edu.ptit.da2020.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class InvertedIndex {
    private static final String NAME = "src/main/resources/map/HN_name.txt";
    private static final String INVERTED = "src/main/resources/map/inverted_index.txt";

    public static void main(String[] args) {
        preProcessing();
    }

    private static void preProcessing() {
        Map<String, String> map = new HashMap<>();

        log.info("start read file " + NAME);
        try {
            File myObj = new File(NAME);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine().trim();
                if (StringUtils.isNotEmpty(line)) {
                    String[] temp = line.split("::");
                    for (String i : temp[1].split("\\s+")) {
                        i = CommonUtils.removeAccents(i);
                        i = i.toLowerCase();
                        String arrayValue = map.get(i) == null ? temp[0] : map.get(i) + "," + temp[0];
                        map.put(i, arrayValue);
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.error("An error occurred " + e);
        }
        log.info("done read file " + NAME);



        log.info("start write file " + INVERTED);
        try {
            File f = new File(INVERTED);
            if (f.createNewFile()) {
                log.info("File created " + f.getName());
            } else {
                log.info("File already exists " + f.getName());
            }
        } catch (IOException e) {
            log.error("An error occurred " + e);
        }
        try (
                FileWriter fw = new FileWriter(INVERTED);
        ) {
            log.info("begin insert inverted index");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                fw.write(entry.getKey() + " = {" + entry.getValue() + "}\n");
            }
            log.info("done");
        } catch (IOException e) {
            log.error("An error occurred.");
            e.printStackTrace();
        }
        log.info("done write file");
    }
}
