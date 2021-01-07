package edu.ptit.da2020.util;

abstract class Mercator {
    final static double RADIUS_MAJOR = 6378.137;
    final static double RADIUS_MINOR = 6356.7523142;

    abstract double yAxisProjection(double input);

    abstract double xAxisProjection(double input);

    abstract double yAxisInverseProjection(double input);

    abstract double xAxisInverseProjection(double input);
}
