package edu.ptit.da2020.util;

public class Mercator {

    static final double RADIUS_MAJOR = 6371;
    static final double RADIUS_MINOR = 6371;


    public static double xAxisProjection(double input) {
        return Math.toRadians(input) * RADIUS_MAJOR;
    }

    public static double yAxisProjection(double input) {
        return Math.log(Math.tan(Math.PI / 4 + Math.toRadians(input) / 2)) * RADIUS_MAJOR;
    }

    public static double xAxisInverseProjection(double input) {
        return Math.toDegrees((input) / RADIUS_MAJOR);
    }

    public static double yAxisInverseProjection(double input) {
        return Math.toDegrees(2 * Math.atan(Math.exp(input / 6378.1)) - Math.PI / 2);
    }
}
