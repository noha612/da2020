package edu.ptit.da2020.util;

public class SphericalMercator extends Mercator {

  @Override
  double xAxisProjection(double input) {
    return Math.toRadians(input) * RADIUS_MAJOR;
  }

  @Override
  double yAxisInverseProjection(double input) {
    return Math.toDegrees(2 * Math.atan(Math.exp(input / 6378.1)) - Math.PI / 2);
  }

  @Override
  double xAxisInverseProjection(double input) {
    return Math.toDegrees(input/RADIUS_MAJOR);
  }

  @Override
  double yAxisProjection(double input) {
    return Math.log(Math.tan(Math.PI / 4 + Math.toRadians(input) / 2)) * RADIUS_MAJOR;
  }
}