package edu.ptit.da2020.config;

import static edu.ptit.da2020.constant.FileConstant.EDGE;
import static edu.ptit.da2020.constant.FileConstant.INVERTED;
import static edu.ptit.da2020.constant.FileConstant.NAME;
import static edu.ptit.da2020.constant.FileConstant.VERTEX;
import static edu.ptit.da2020.constant.FileConstant.VERTEX_NAME;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@Data
@Slf4j
@Order(1)
public class DataLoader {

  private Map<String, Double[]> listV;
  private Map<String, String[]> listE;
  private Map<String, Integer[]> ii;
  private Map<Integer, String> listName;
  private Map<String, Integer> listCongestions;
  private Map<String, String> listVN;

  @Autowired
  RedisTemplate redisTemplate;

  @PostConstruct
  private void initGraph() {
    log.info("init graph...");

    loadVertex();
    loadEdge();
    loadInvertedKey();
    loadName();
    loadCongestion();
    loadVertexWithName();
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
            for (int i = 0; i < array.length; i++) {
                array[i] = Double.parseDouble(temp[i + 1]);
            }
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
            for (int i = 0; i < array.length; i++) {
                array[i] = Integer.parseInt(arrayString[i]);
            }
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
    LinkedHashSet<String> keySet = (LinkedHashSet<String>) redisTemplate.opsForHash()
        .keys("CONGEST");
    List<Integer> level = redisTemplate.opsForHash().multiGet("CONGEST", keySet);
    listCongestions = new HashMap<>();
    int i = 0;
    for (String k : keySet) {
      listCongestions.put(k, level.get(i));
      i++;
    }
  }

  private void loadVertexWithName() {
    listVN = new LinkedHashMap<>();

    log.info("start read file " + VERTEX_NAME);
    try {
      File myObj = new File(VERTEX_NAME);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String line = myReader.nextLine().trim();
        if (StringUtils.isNotEmpty(line)) {
          String[] temp = line.split("::");
          listVN.put(temp[0], temp[1]);
        }
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      log.error("An error occurred " + e);
    }
    log.info("done read file " + VERTEX_NAME + ", total V: " + listVN.size());
  }
}
