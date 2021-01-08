package edu.ptit.da2020.util;

public class Mercator {

    static final double RADIUS_MAJOR = 6371;


    public static double xAxisProjection(double input) {
        return Math.toRadians(input) * RADIUS_MAJOR + Math.toRadians(-0.001545);
    }

    public static double yAxisProjection(double input) {
        return Math.log(Math.tan(Math.PI / 4 + Math.toRadians(input) / 2)) * RADIUS_MAJOR;
    }

    public static double xAxisInverseProjection(double input) {
        return Math.toDegrees((input - Math.toRadians(-0.001545)) / RADIUS_MAJOR);
    }

    public static double yAxisInverseProjection(double input) {
        return Math.toDegrees(2 * Math.atan(Math.exp(input / 6378.1)) - Math.PI / 2);
    }

    public static void main(String[] args) {
        System.out.println(xAxisProjection(21.0283568));
        System.out.println(yAxisProjection(105.8513213));
        System.out.println(Math.toRadians(105.8513213));
        System.out.println(Math.toDegrees(Math.toRadians(105.8513213)));

        System.out.println(xAxisInverseProjection(44));
        System.out.println(yAxisInverseProjection(22));

        double lat = Math.toRadians(21.0283568);
        System.out.println(lat);
        double y = 6371 * Math.log(Math.tan(Math.PI / 4 + lat / 2));
        System.out.println(y);
        double y1 = 2 * Math.atan(Math.exp(y / 6371)) - Math.PI / 2;
        System.out.println(Math.toDegrees(y1));
    }
}
