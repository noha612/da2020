package edu.ptit.da2020.util;

import static edu.ptit.da2020.constant.BaseConstant.R;

import edu.ptit.da2020.model.GeoPoint;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    public static Coordinate getAltitudeCoordinateOfTriangle(
            Coordinate A,
            Coordinate B,
            Coordinate C
    ) {
        projection(A,B,C);
        double d = (B.y - A.y) / (B.x - A.x);
        double x = (A.x * d * d + (C.y - A.y) * d + C.x) / (d * d + 1);
        double y = C.y - (x - C.x) / d;
        Coordinate H = new Coordinate(x, y);
        inverse(H);
        return H;
    }

    public static void main(String[] args) {
//        TwoDimensionCoordinate A = new TwoDimensionCoordinate(2.0, -1.0);
//        TwoDimensionCoordinate B = new TwoDimensionCoordinate(-1.0, 5.0);
//        TwoDimensionCoordinate C = new TwoDimensionCoordinate(5.0, 3.0);
        Coordinate A = new Coordinate(2.0, -1.0);
        Coordinate B = new Coordinate(2.0, 5.0);
        Coordinate C = new Coordinate(2.0, 3.0);
        System.out.println(getAltitudeCoordinateOfTriangle(A, B, C));
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

    public static double haversineFomular(GeoPoint from, GeoPoint to) {

        double dLat = Math.toRadians(to.getLat() - from.getLat());
        double dLon = Math.toRadians(to.getLng() - from.getLng());
        double lat1 = Math.toRadians(from.getLat());
        double lat2 = Math.toRadians(to.getLat());

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
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
    @NoArgsConstructor
    public static class Coordinate {

        private double x;
        private double y;

        public Coordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
