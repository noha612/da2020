package edu.ptit.da2020.util;

import static edu.ptit.da2020.constant.BaseConstant.R;

import edu.ptit.da2020.model.GeoPoint;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

public class CommonUtil {

  public static String removeAccents(String input) {
    if (input == null || input.length() < 1) {
      return "";
    }
    return (StringUtils.replaceChars(
        StringUtils.replaceChars(StringUtils.stripAccents(input), (char) 273, (char) 100),
        (char) 272, (char) 68));
  }

  public static Integer[] intersectionArrays(List<Integer[]> list) {
    Integer[] result = list.get(0);
    if (list.size() < 2) {
      return result;
    }
    for (int i = 0; i < list.size() - 1; i++) {
      Integer[] temp = intersectionArray(list.get(i), list.get(i + 1));
      if (temp.length > 0) {
        result = temp;
      }
    }
    return result;
  }

  public static Integer[] intersectionArray(Integer[] arr1, Integer[] arr2) {
    List<Integer> temp = new ArrayList<>();
    int i = 0, j = 0;
    while (i < arr1.length && j < arr2.length) {
      if (arr1[i] < arr2[j]) {
        i++;
      } else if (arr2[j] < arr1[i]) {
        j++;
      } else {
        temp.add(arr2[j++]);
        i++;
      }
    }
    return temp.toArray(new Integer[0]);
  }

  public static double distance(double fromLat, double toLat, double fromLng, double toLng) {
    double R = 6371; // km

    double dLat = Math.toRadians(toLat - fromLat);
    double dLon = Math.toRadians(toLng - fromLng);
    double lat1 = Math.toRadians(fromLat);
    double lat2 = Math.toRadians(toLat);

    double a =
        Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1)
            * Math.cos(lat2);
    double c = 2 * Math.asin(Math.sqrt(a));
    return R * c;
  }

  public static double getAreaByHeronFormula(double a, double b, double c) {
    double p = (a + b + c) / 2;
    return Math.sqrt(p * (p - a) * (p - b) * (p - c));
  }

  public static double getHeightOfTriangle(double a, double b, double c, double x) {
    double S = getAreaByHeronFormula(a, b, c);
    return 2 * S / x;
  }

  //Altitude of C
  public static Coordinate getAltitudeCoordinateOfTriangle(
      Coordinate A,
      Coordinate B,
      Coordinate C
  ) {
    projection(A, B, C);
    double d = (B.y - A.y) / (B.x - A.x);
    double x = (A.x * d * d + (C.y - A.y) * d + C.x) / (d * d + 1);
    double y = C.y - (x - C.x) / d;
    Coordinate H = new Coordinate(x, y);
    inverse(H);
    return H;
  }


  private static void projection(Coordinate A, Coordinate B, Coordinate C) {
    Mercator.yAxisProjection(A.getX());
    Mercator.yAxisProjection(B.getX());
    Mercator.yAxisProjection(C.getX());
    Mercator.xAxisProjection(A.getY());
    Mercator.xAxisProjection(B.getY());
    Mercator.xAxisProjection(C.getY());
  }

  private static void inverse(Coordinate H) {
    Mercator.yAxisInverseProjection(H.getX());
    Mercator.xAxisInverseProjection(H.getY());
  }

  public static double haversineFormula(GeoPoint from, GeoPoint to) {

    double dLat = Math.toRadians(to.getLat() - from.getLat());
    double dLon = Math.toRadians(to.getLng() - from.getLng());
    double lat1 = Math.toRadians(from.getLat());
    double lat2 = Math.toRadians(to.getLat());

    double a =
        Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1)
            * Math.cos(lat2);
    double c = 2 * Math.asin(Math.sqrt(a));
    return R * c;
  }

  public static double haversineFormula(
      double fromLat, double fromLng,
      double toLat, double toLng
  ) {

    double dLat = Math.toRadians(toLat - fromLat);
    double dLon = Math.toRadians(toLng - fromLng);
    double lat1 = Math.toRadians(fromLat);
    double lat2 = Math.toRadians(toLat);

    double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1)
        * Math.cos(lat2);
    double c = 2 * Math.asin(Math.sqrt(a));
    return R * c;
  }

  @Data
  @NoArgsConstructor
  public static class Coordinate {

    private double x;
    private double y;

    public Coordinate(double x, double y) {
      this.x = x;
      this.y = y;
    }
  }

  public static String getTime(LocalDateTime fromDateTime, LocalDateTime toDateTime) {

    LocalDateTime tempDateTime = LocalDateTime.from(fromDateTime);
//
//        long years = tempDateTime.until(toDateTime, ChronoUnit.YEARS);
//        tempDateTime = tempDateTime.plusYears(years);
//        long months = tempDateTime.until(toDateTime, ChronoUnit.MONTHS);
//        tempDateTime = tempDateTime.plusMonths(months);
//        long days = tempDateTime.until(toDateTime, ChronoUnit.DAYS);
//        tempDateTime = tempDateTime.plusDays(days);
//        long hours = tempDateTime.until(toDateTime, ChronoUnit.HOURS);
//        tempDateTime = tempDateTime.plusHours(hours);
//        long minutes = tempDateTime.until(toDateTime, ChronoUnit.MINUTES);
//        tempDateTime = tempDateTime.plusMinutes(minutes);

    long seconds = tempDateTime.until(toDateTime, ChronoUnit.SECONDS);
    tempDateTime = tempDateTime.plusSeconds(seconds);

    long milisecond = tempDateTime.until(toDateTime, ChronoUnit.MILLIS);

    return seconds + "." + milisecond;
  }

  public static void main(String[] args) {
    Coordinate A = new Coordinate(2.0, -1.0);
    Coordinate B = new Coordinate(2.0, 5.0);
    Coordinate C = new Coordinate(2.0, 3.0);
    System.out.println(getAltitudeCoordinateOfTriangle(A, B, C));

    double latA = 21.9749816;
    double lngA = 105.8651266;
    System.out.println(distance(20.9450, 20.9450, 105.7420, 105.9110));
    System.out.println(distance(20.9450, 21.0980, 105.7420, 105.7420));
    System.out.println(17.555203956249414 *
        17.017630413377333);
  }
}
