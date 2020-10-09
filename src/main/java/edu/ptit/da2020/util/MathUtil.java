package edu.ptit.da2020.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MathUtil {
    public static double getAreaByHeronFormula(double a, double b, double c) {
        double p = (a + b + c) / 2;
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }

    public static double getHeightOfTriangle(double x, double S) {
        return 2 * S / x;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TwoDimensionCoordinate {
        private double x;
        private double y;
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
        TwoDimensionCoordinate A = new TwoDimensionCoordinate(2.0, -1.0);
        TwoDimensionCoordinate B = new TwoDimensionCoordinate(-1.0, 5.0);
        TwoDimensionCoordinate C = new TwoDimensionCoordinate(5.0, 3.0);
        System.out.println(getAltitudeCoordinateOfTriangle(A, B, C));
    }
}
