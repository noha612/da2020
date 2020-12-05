package edu.ptit.da2020.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static edu.ptit.da2020.constant.BaseConstant.R;

public class MathUtil {
    public static double getAreaByHeronFormula(double a, double b, double c) {
        double p = (a + b + c) / 2;
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }

    public static double getHeightOfTriangle(double a, double b, double c, double x) {
        double S = getAreaByHeronFormula(a, b, c);
        return 2 * S / x;
    }

    //Altitude of C
    public static TwoDimensionCoordinate getAltitudeCoordinateOfTriangle(
            TwoDimensionCoordinate A,
            TwoDimensionCoordinate B,
            TwoDimensionCoordinate C
    ) {
        double d = (B.y - A.y) / (B.x - A.x);
        double x = (A.x * d * d + (C.y - A.y) * d + C.x) / (d * d + 1);
        double y = C.y - (x - C.x) / d;
        return new TwoDimensionCoordinate(x, y);
    }

    public static void main(String[] args) {
//        TwoDimensionCoordinate A = new TwoDimensionCoordinate(2.0, -1.0);
//        TwoDimensionCoordinate B = new TwoDimensionCoordinate(-1.0, 5.0);
//        TwoDimensionCoordinate C = new TwoDimensionCoordinate(5.0, 3.0);
        TwoDimensionCoordinate A = new TwoDimensionCoordinate(2.0, -1.0);
        TwoDimensionCoordinate B = new TwoDimensionCoordinate(2.0, 5.0);
        TwoDimensionCoordinate C = new TwoDimensionCoordinate(2.0, 3.0);
        System.out.println(getAltitudeCoordinateOfTriangle(A, B, C));
    }

    public static double haversineFomular(
            double fromLat, double fromLng,
            double toLat, double toLng
    ) {

        double dLat = Math.toRadians(toLat - fromLat);
        double dLon = Math.toRadians(toLng - fromLng);
        double lat1 = Math.toRadians(fromLat);
        double lat2 = Math.toRadians(toLat);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TwoDimensionCoordinate {
        private double x;
        private double y;
    }
}
