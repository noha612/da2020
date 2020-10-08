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
        double x;
        double y;
    }

    //Altitude of C
    public static TwoDimensionCoordinate getAltitudeCoordinateOfTriangle(
            TwoDimensionCoordinate A,
            TwoDimensionCoordinate B,
            TwoDimensionCoordinate C
    ) {
        double x = C.x + Math.pow(B.y - A.y, 2) * (A.x - C.x) / (Math.pow(B.x + A.x, 2) + Math.pow(B.y - A.y, 2));
        return new TwoDimensionCoordinate();
    }

    public static void main(String[] args) {
        TwoDimensionCoordinate A = new TwoDimensionCoordinate(2.0, -1.0);
        TwoDimensionCoordinate B = new TwoDimensionCoordinate(-1.0, 5.0);
        TwoDimensionCoordinate C = new TwoDimensionCoordinate(5.0, 3.0);
        System.out.println((B.x - A.x) * (1 - C.x) + (B.y - A.y) * (1 - C.y));
        System.out.println(-(B.x - A.x) * (1 - A.y) + (B.y - A.y) * (1 - A.x));
        System.out.println((B.x - A.x) * (1 - C.x) + (B.y - A.y) * (1 - C.y));
        getAltitudeCoordinateOfTriangle(A, B, C);
    }
}
