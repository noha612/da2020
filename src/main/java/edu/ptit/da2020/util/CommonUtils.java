package edu.ptit.da2020.util;

import edu.ptit.da2020.model.entity.Intersection;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommonUtils {

    public static String removeAccents(String input) {
        if (input == null || input.length() < 1) {
            return "";
        }
        return (StringUtils.replaceChars(StringUtils.replaceChars(StringUtils.stripAccents(input), (char) 273, (char) 100), (char) 272, (char) 68));
    }

    public static Integer[] intersectionArrays(List<Integer[]> list) {
        Integer[] result = list.get(0);
        if (list.size() < 2) {
            return result;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            Integer[] temp = intersectionArray(list.get(i), list.get(i + 1));
            if (temp.length > 0) result = temp;
        }
        return result;
    }

    public static Integer[] intersectionArray(Integer[] arr1, Integer[] arr2) {
        List<Integer> temp = new ArrayList<>();
        int i = 0, j = 0;
        while (i < arr1.length && j < arr2.length) {
            if (arr1[i] < arr2[j])
                i++;
            else if (arr2[j] < arr1[i])
                j++;
            else {
                temp.add(arr2[j++]);
                i++;
            }
        }
        return temp.toArray(new Integer[0]);
    }
    public static double computeCost(Intersection from, Intersection to) {
        double R = 6372.8; // km

        double dLat = Math.toRadians(to.getLatitude() - from.getLatitude());
        double dLon = Math.toRadians(to.getLongitude() - from.getLongitude());
        double lat1 = Math.toRadians(from.getLatitude());
        double lat2 = Math.toRadians(to.getLatitude());

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}