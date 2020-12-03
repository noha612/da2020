package edu.ptit.da2020;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Scanner;

public class NavGraphFactory {
    private static final String MAP_FILE = "/home/hoangpn/Downloads/map";

    public static void main(String[] args) {
        String node = "";
        String name = "";
        String number = "";
        String street = "";
        int d = 0;

        try {
            File myObj = new File(MAP_FILE);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String way = myReader.nextLine().trim();
                if (way.startsWith("<node")) {
                    String[] temp = way.split(" ");

                    node =
                            temp[1].substring(temp[1].indexOf("\"") + 1, temp[1].length() - 1) + " " +
                                    temp[2].substring(temp[2].indexOf("\"") + 1, temp[2].length() - 1) + " " +
                                    temp[3].substring(temp[3].indexOf("\"") + 1, temp[3].length() - 1) + " "
                    ;

                    String line = myReader.nextLine().trim();
                    while (!line.startsWith("</node") && !line.startsWith("<node")) {
                        if (line.startsWith("<tag k=\"name\" ")) {
                            name = line;
                            d++;
                        }
                        if (line.startsWith("<tag k=\"addr:housenumber\"")) {
                            number = line;
                        }
                        if (line.startsWith("<tag k=\"addr:street\" ")) {
                            street = line;
                        }
                        line = myReader.nextLine().trim();
                    }
                }
                if (StringUtils.isNotEmpty(name) || StringUtils.isNotEmpty(number) || StringUtils.isNotEmpty(street))
                    System.out.println(node + number + street + name);

                node = "";
                name = "";
                number = "";
                street = "";
            }
            myReader.close();
        } catch (Exception e) {
        }
        System.out.println(d);
    }
}
