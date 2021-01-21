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

  public static String getDistrict(GeoPoint gp) {
    String district = "";
    if (haversineFormula(gp, new GeoPoint(21.032324, 105.8513729)) <= 0.9176581542823958 ||
        haversineFormula(gp, new GeoPoint(21.026931, 105.8541989)) <= 0.9176581542823958 ||
        haversineFormula(gp, new GeoPoint(21.0266882, 105.849953)) <= 0.9176581542823958) {
      return "Hoàn Kiếm";
    }
    if (haversineFormula(gp, new GeoPoint(21.013269, 105.8258443)) <= 1.3408354787632726
        || haversineFormula(gp, new GeoPoint(21.02302, 105.8306243)) <= 0.9372964501346642
        || haversineFormula(gp, new GeoPoint(21.024821, 105.8021843)) <= 0.6161128460152889
        || haversineFormula(gp, new GeoPoint(21.013904, 105.8105083)) <= 0.4277265384835576
        || haversineFormula(gp, new GeoPoint(21.017249, 105.8047363)) <= 0.3214062235737385
        || haversineFormula(gp, new GeoPoint(21.006183, 105.8325793)) <= 0.6908506792383218) {
      return "Đống Đa";
    }
    if (haversineFormula(gp, new GeoPoint(21.021538, 105.8114653)) <= 0.4190999912822531
        || haversineFormula(gp, new GeoPoint(21.042889, 105.809444)) <= 0.3887715432045291
        || haversineFormula(gp, new GeoPoint(21.031209, 105.806752)) <= 0.3887715432045291
        || haversineFormula(gp, new GeoPoint(21.035605, 105.828725)) <= 0.4190999912822531
        || haversineFormula(gp, new GeoPoint(21.033622, 105.816837)) <= 1.0184553285596156
        || haversineFormula(gp, new GeoPoint(21.045958, 105.848291)) <= 0.6428645366678956
        || haversineFormula(gp, new GeoPoint(21.043825, 105.841865)) <= 0.6428645366678956
        || haversineFormula(gp, new GeoPoint(21.036475, 105.837745)) <= 0.6428645366678956
        || haversineFormula(gp, new GeoPoint(21.037977, 105.832885)) <= 0.4190999912822531) {
      return "Ba Đình";
    }
    if (haversineFormula(gp, new GeoPoint(21.006012, 105.855103)) <= 1.376704386786427
        || haversineFormula(gp, new GeoPoint(21.003023, 105.870542)) <= 1.0630658129483739
        || haversineFormula(gp, new GeoPoint(21.014681, 105.865531)) <= 0.5076291263112964
        || haversineFormula(gp, new GeoPoint(21.016544, 105.844491)) <= 0.3201520891415271
        || haversineFormula(gp, new GeoPoint(20.993437, 105.848675)) <= 0.3201520891415271
        || haversineFormula(gp, new GeoPoint(20.995631, 105.845607)) <= 0.3201520891415271) {
      return "Hai Bà Trưng";
    }
    if (haversineFormula(gp, new GeoPoint(20.975814, 105.857766)) <= 2.2417990348243766
        || haversineFormula(gp, new GeoPoint(20.979855, 105.884222)) <= 2.2417990348243766
        || haversineFormula(gp, new GeoPoint(20.957374, 105.884501)) <= 0.8470672713194735
        || haversineFormula(gp, new GeoPoint(20.959503, 105.879046)) <= 0.8470672713194735
        || haversineFormula(gp, new GeoPoint(20.962037, 105.847159)) <= 0.8470672713194735
        || haversineFormula(gp, new GeoPoint(20.963309, 105.837460)) <= 0.8470672713194735
        || haversineFormula(gp, new GeoPoint(20.961626, 105.824821)) <= 0.8470672713194735
        || haversineFormula(gp, new GeoPoint(20.977144, 105.836762)) <= 0.8470672713194735
        || haversineFormula(gp, new GeoPoint(20.976403, 105.814028)) <= 0.8470672713194735
        || haversineFormula(gp, new GeoPoint(20.977976, 105.823019)) <= 0.8470672713194735
        || haversineFormula(gp, new GeoPoint(20.982594, 105.826506)) <= 0.8470672713194735
        || haversineFormula(gp, new GeoPoint(20.986140, 105.828587)) <= 0.8470672713194735
        || haversineFormula(gp, new GeoPoint(20.971624, 105.830582)) <= 0.8470672713194735) {
      return "Hoàng Mai";
    }
    if (haversineFormula(gp, new GeoPoint(21.044106, 105.900257)) <= 4.017608527045769
        || haversineFormula(gp, new GeoPoint(21.072321, 105.855678)) <= 1.334088608924107
        || haversineFormula(gp, new GeoPoint(21.014525, 105.886781)) <= 1.334088608924107
        || haversineFormula(gp, new GeoPoint(21.010862, 105.895028)) <= 1.334088608924107
        || haversineFormula(gp, new GeoPoint(21.014327, 105.911175)) <= 1.334088608924107
        || haversineFormula(gp, new GeoPoint(21.057274, 105.861987)) <= 1.334088608924107) {
      return "Long Biên";
    }
    if (haversineFormula(gp, new GeoPoint(21.010324, 105.752922)) <= 2.0360743224162525
        || haversineFormula(gp, new GeoPoint(21.019127, 105.759832)) <= 2.0360743224162525
        || haversineFormula(gp, new GeoPoint(21.004094, 105.767160)) <= 2.0360743224162525
        || haversineFormula(gp, new GeoPoint(21.033568, 105.746432)) <= 0.8314901005359085
        || haversineFormula(gp, new GeoPoint(21.024544, 105.775627)) <= 0.8314901005359085
        || haversineFormula(gp, new GeoPoint(20.994237, 105.787482)) <= 0.8314901005359085) {
      return "Nam Từ Liêm";
    }
    if (haversineFormula(gp, new GeoPoint(21.072838, 105.753998)) <= 2.921391115709194
        || haversineFormula(gp, new GeoPoint(21.090081, 105.741739)) <= 1.6252402742300458
        || haversineFormula(gp, new GeoPoint(21.057019, 105.748754)) <= 1.6252402742300458
        || haversineFormula(gp, new GeoPoint(21.084155, 105.778150)) <= 1.6252402742300458
        || haversineFormula(gp, new GeoPoint(21.069814, 105.786562)) <= 1.6252402742300458
        || haversineFormula(gp, new GeoPoint(21.061107, 105.784767)) <= 1.6252402742300458) {
      return "Bắc Từ Liêm";
    }
    if (haversineFormula(gp, new GeoPoint(21.072851, 105.822496)) <= 1.873185301998316
        || haversineFormula(gp, new GeoPoint(21.059451, 105.823639)) <= 1.873185301998316
        || haversineFormula(gp, new GeoPoint(21.058300, 105.838960)) <= 1.0664429581626906
        || haversineFormula(gp, new GeoPoint(21.054401, 105.806064)) <= 1.0664429581626906
        || haversineFormula(gp, new GeoPoint(21.088563, 105.803579)) <= 1.0664429581626906
        || haversineFormula(gp, new GeoPoint(21.085474, 105.816804)) <= 1.0664429581626906
        || haversineFormula(gp, new GeoPoint(21.080922, 105.809674)) <= 1.0664429581626906) {
      return "Tây Hồ";
    }
    if (haversineFormula(gp, new GeoPoint(21.039852, 105.777723)) <= 0.9235165403374773
        || haversineFormula(gp, new GeoPoint(21.039415, 105.797254)) <= 0.9235165403374773
        || haversineFormula(gp, new GeoPoint(21.033054, 105.791896)) <= 0.9235165403374773
        || haversineFormula(gp, new GeoPoint(21.022814, 105.791584)) <= 0.7374714457864453
        || haversineFormula(gp, new GeoPoint(21.012173, 105.799323)) <= 0.7374714457864453
        || haversineFormula(gp, new GeoPoint(21.011135, 105.796407)) <= 0.7374714457864453) {
      return "Cầu Giấy";
    }
    if (haversineFormula(gp, new GeoPoint(20.953519, 105.760362)) <= 3.3112388590903294
        || haversineFormula(gp, new GeoPoint(20.982836, 105.746612)) <= 1.0603713328997046
        || haversineFormula(gp, new GeoPoint(20.977703, 105.784929)) <= 1.0603713328997046) {
      return "Hà Đông";
    }
    if (haversineFormula(gp, new GeoPoint(20.997259, 105.807565)) <= 1.1790426805884822
        || haversineFormula(gp, new GeoPoint(20.996650, 105.819100)) <= 0.4727498201761683
        || haversineFormula(gp, new GeoPoint(20.987390, 105.815072)) <= 0.4727498201761683
        || haversineFormula(gp, new GeoPoint(20.990281, 105.819018)) <= 0.4727498201761683
        || haversineFormula(gp, new GeoPoint(20.996823, 105.827728)) <= 0.4727498201761683
        || haversineFormula(gp, new GeoPoint(20.995164, 105.835103)) <= 0.4727498201761683
        || haversineFormula(gp, new GeoPoint(20.991120, 105.837299)) <= 0.4727498201761683
        || haversineFormula(gp, new GeoPoint(20.994987, 105.838527)) <= 0.40679190106936197
        || haversineFormula(gp, new GeoPoint(20.987873, 105.838196)) <= 0.40679190106936197) {
      return "Thanh Xuân";
    }
    if (haversineFormula(gp, new GeoPoint(20.972992, 105.807498)) <= 1.467641515378333
        || haversineFormula(gp, new GeoPoint(20.948044, 105.807514)) <= 1.467641515378333
        || haversineFormula(gp, new GeoPoint(20.943837, 105.838971)) <= 1.467641515378333
        || haversineFormula(gp, new GeoPoint(20.945852, 105.847750)) <= 1.467641515378333
        || haversineFormula(gp, new GeoPoint(20.941586, 105.872299)) <= 1.467641515378333
        || haversineFormula(gp, new GeoPoint(20.932787, 105.823497)) <= 1.467641515378333) {
      return "Thanh Trì";
    }
    return district;
  }

  public static int longestSubstringPrefix(String str, String ss) {
//    str = removeAccents(str);
//    str = str.toLowerCase();
    for (int i = ss.length() - 1; i >= 0; i--) {
      if (str.contains(ss.substring(0, i + 1))) {
        return i;
      }
    }
    return 0;
  }

  public static int longestCommonSubsequenceLength(String str, String ss) {
    int m = str.length();
    int n = ss.length();
    int[][] L = new int[m + 1][n + 1];
    for (int i = 0; i <= m; i++) {
      for (int j = 0; j <= n; j++) {
        if (i == 0 || j == 0) {
          L[i][j] = 0;
        } else if (str.charAt(i - 1) == ss.charAt(j - 1)) {
          L[i][j] = L[i - 1][j - 1] + 1;
        } else {
          L[i][j] = Math.max(L[i - 1][j], L[i][j - 1]);
        }
      }
    }
    int index = L[m][n];
    int temp = index;
    char[] lcs = new char[index + 1];
    lcs[index] = '\u0000';
    int i = m;
    int j = n;
    while (i > 0 && j > 0) {
      if (str.charAt(i - 1) == ss.charAt(j - 1)) {
        lcs[index - 1] = str.charAt(i - 1);
        i--;
        j--;
        index--;
      } else if (L[i - 1][j] > L[i][j - 1]) {
        i--;
      } else {
        j--;
      }
    }

    return temp;
  }

  public static int getSimilarValue(String str, String ss) {
    String[] splitName = ss.split("\\s+");
    int v = 0;
    boolean isStreak = false;
    for (String s : splitName) {
      if (str.contains(s)) {
        str = str.substring(str.indexOf(s) + s.length());
        if (isStreak)
          v += 2;
        else
          v += 1;
        isStreak = true;
      } else
        isStreak = false;
    }
    return v;
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

    String str = "sieu thi auchan ha dong";
    str = str.toLowerCase();
    str = removeAccents(str);
//    longestCommonSubsequenceLength(str, "vuon hoa dong");
    getSimilarValue(str, "vuon hoa ha dong");
  }
}
