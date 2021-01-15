package edu.ptit.da2020.pre_processing;

import static edu.ptit.da2020.constant.FileConstant.MAP_FILE;
import static edu.ptit.da2020.constant.FileConstant.NAME;
import static edu.ptit.da2020.constant.FileConstant.RAW;
import static edu.ptit.da2020.constant.FileConstant.VERTEX;
import static edu.ptit.da2020.constant.FileConstant.VERTEX_NAME;

import edu.ptit.da2020.util.CommonUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class NameFilter {

  private static Map<String, Double[]> listV;
  private static Map<String, Double[]> listR;

  public static void main(String[] args) {

    loadVertex();
    loadRawNode();
    vertexToName();
    nameToVertex();

  }

  private static void vertexToName() {
    Set<String> set = new LinkedHashSet<>();

    Map<String, String> map = new LinkedHashMap<>();
    String node = "";
    String name = "";
    String number = "";
    String street = "";

    try {
      log.info("start read file " + MAP_FILE);
      File myObj = new File(MAP_FILE);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String way = myReader.nextLine().trim();

        if (way.startsWith("<node")) {
          String[] temp = way.split(" ");

          node = temp[1].substring(temp[1].indexOf("\"") + 1, temp[1].length() - 1);

          String line = myReader.nextLine().trim();
          while (!line.startsWith("</node") && !line.startsWith("<node")) {
            if (line.startsWith("<tag k=\"name\" ")) {
              line = line.replace("<tag k=\"name\" v=\"", "");
              line = line.replace("\"/>", "");
              name = line;
            } else if (line.startsWith("<tag k=\"addr:housenumber\"")) {
              line = line.replace("<tag k=\"addr:housenumber\" v=\"", "");
              line = line.replace("\"/>", "");
              number = line;
            } else if (line.startsWith("<tag k=\"addr:street\" ")) {
              line = line.replace("<tag k=\"addr:street\" v=\"", "");
              line = line.replace("\"/>", "");
              street = line;
            }
            line = myReader.nextLine().trim();
          }
          if (StringUtils.isNotEmpty(name)) {
            map.put(name, node);
          }
          if (StringUtils.isNotEmpty(street)) {
            if (StringUtils.isNotEmpty(number)) {
                if (number.toLowerCase().startsWith("so") || number.toLowerCase()
                    .startsWith("số")) {
                    map.put(number + " " + street, node);
                } else {
                    map.put("Số " + number + " " + street, node);
                }
            } else {
              map.put(street, node);
            }
          }
          node = "";
          name = "";
          number = "";
          street = "";
        }

        if (way.startsWith("<way")) {
          ArrayList<String> nodeInWay = new ArrayList<>();
          String line = myReader.nextLine().trim();
          while (!line.startsWith("</way")) {
            if (line.startsWith("<nd")) {
              line = line.replace("<nd ref=\"", "");
              line = line.replace("\"/>", "");
              nodeInWay.add(line);
            }
            if (line.startsWith("<tag k=\"name\" v=\"")) {
              line = line.replace("<tag k=\"name\" v=\"", "");
              line = line.replace("\"/>", "");
                if (!map.containsKey(line)) {
                    map.put(line, nodeInWay.get(nodeInWay.size() / 2));
                }
            }
            line = myReader.nextLine().trim();
          }
        }
      }
      myReader.close();

    } catch (
        FileNotFoundException e) {
      log.error("An error occurred " + e);
    }
    log.info("done read file " + MAP_FILE);

    int c = 0;
    for (Map.Entry<String, Double[]> entry : listV.entrySet()) {
      double d = Double.MAX_VALUE;
      String nn = "blabla";
      for (Map.Entry<String, String> entry2 : map.entrySet()) {
        double temp = CommonUtil.haversineFormula(entry.getValue()[0], entry.getValue()[1],
            listR.get(entry2.getValue())[0], listR.get(entry2.getValue())[1]);
        if (temp < d) {
          d = temp;
          nn = entry2.getKey();
        }
      }
      set.add(entry.getKey() + "::" + nn);
      c++;
      log.info(c + "");
    }

    log.info("start write file " + VERTEX_NAME);
    try {
      File f = new File(VERTEX_NAME);
      if (f.createNewFile()) {
        log.info("File created " + f.getName());
      } else {
        log.info("File already exists " + f.getName());
      }
    } catch (IOException e) {
      log.error("An error occurred " + e);
    }
    try (
        FileWriter fw = new FileWriter(VERTEX_NAME)
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

  private static void nameToVertex() {
    Map<String, String> map = new LinkedHashMap<>();
    String node = "";
    String name = "";
    String number = "";
    String street = "";

    try {
      log.info("start read file " + MAP_FILE);
      File myObj = new File(MAP_FILE);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String way = myReader.nextLine().trim();

        if (way.startsWith("<node")) {
          String[] temp = way.split(" ");

          node = temp[1].substring(temp[1].indexOf("\"") + 1, temp[1].length() - 1);

          String line = myReader.nextLine().trim();
          while (!line.startsWith("</node") && !line.startsWith("<node")) {
            if (line.startsWith("<tag k=\"name\" ")) {
              line = line.replace("<tag k=\"name\" v=\"", "");
              line = line.replace("\"/>", "");
              name = line;
            } else if (line.startsWith("<tag k=\"addr:housenumber\"")) {
              line = line.replace("<tag k=\"addr:housenumber\" v=\"", "");
              line = line.replace("\"/>", "");
              number = line;
            } else if (line.startsWith("<tag k=\"addr:street\" ")) {
              line = line.replace("<tag k=\"addr:street\" v=\"", "");
              line = line.replace("\"/>", "");
              street = line;
            }
            line = myReader.nextLine().trim();
          }
          if (StringUtils.isNotEmpty(name)) {
            map.put(name, node);
          }
          if (StringUtils.isNotEmpty(street)) {
            if (StringUtils.isNotEmpty(number)) {
                if (number.toLowerCase().startsWith("so") || number.toLowerCase()
                    .startsWith("số")) {
                    map.put(number + " " + street, node);
                } else {
                    map.put("Số " + number + " " + street, node);
                }
            } else {
              map.put(street, node);
            }
          }
          node = "";
          name = "";
          number = "";
          street = "";
        }

        if (way.startsWith("<way")) {
          ArrayList<String> nodeInWay = new ArrayList<>();
          String line = myReader.nextLine().trim();
          while (!line.startsWith("</way")) {
            if (line.startsWith("<nd")) {
              line = line.replace("<nd ref=\"", "");
              line = line.replace("\"/>", "");
              nodeInWay.add(line);
            }
            if (line.startsWith("<tag k=\"name\" v=\"")) {
              line = line.replace("<tag k=\"name\" v=\"", "");
              line = line.replace("\"/>", "");
                if (!map.containsKey(line)) {
                    map.put(line, nodeInWay.get(nodeInWay.size() / 2));
                }
            }
            line = myReader.nextLine().trim();
          }
        }
      }
      myReader.close();

    } catch (
        FileNotFoundException e) {
      log.error("An error occurred " + e);
    }
    log.info("done read file " + MAP_FILE);

    int c = 0;
    for (Map.Entry<String, String> entry : map.entrySet()) {
      Double[] d = listR.get(entry.getValue());
      map.put(entry.getKey(), findLocationByPoint(d[0], d[1]));
      System.out.println(c++);
    }

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

  private static void loadVertex() {
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

  private static void loadRawNode() {
    listR = new LinkedHashMap<>();

    log.info("start read file " + RAW);
    try {
      File myObj = new File(RAW);
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
          listR.put(key, array);
        }
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      log.error("An error occurred " + e);
    }
    log.info("done read file " + RAW + ", total V: " + listR.size());
  }

  private static String findLocationByPoint(double lat, double lng) {
    double d = Double.MAX_VALUE;
    String id = "blabla";

    for (Map.Entry<String, Double[]> entry : listV.entrySet()) {
      double temp = CommonUtil.haversineFormula(lat, lng, entry.getValue()[0], entry.getValue()[1]);
      if (temp < d) {
        d = temp;
        id = entry.getKey();
      }
    }

    return id;
  }
}
