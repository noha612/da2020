package edu.ptit.da2020.util;

public class EllipticalMercator extends Mercator {
    @Override
    double yAxisProjection(double input) {

        input = Math.min(Math.max(input, -89.5), 89.5);
        double earthDimensionalRateNormalized = 1.0 - Math.pow(RADIUS_MINOR / RADIUS_MAJOR, 2);

        double inputOnEarthProj = Math.sqrt(earthDimensionalRateNormalized) *
                Math.sin(Math.toRadians(input));

        inputOnEarthProj = Math.pow(((1.0 - inputOnEarthProj) / (1.0 + inputOnEarthProj)),
                0.5 * Math.sqrt(earthDimensionalRateNormalized));

        double inputOnEarthProjNormalized =
            Math.tan(0.5 * ((Math.PI * 0.5) - Math.toRadians(input))) / inputOnEarthProj;

        return (-1) * RADIUS_MAJOR * Math.log(inputOnEarthProjNormalized);
    }

    @Override
    double xAxisProjection(double input) {
        return RADIUS_MAJOR * Math.toRadians(input);
    }

    @Override
    double yAxisInverseProjection(double input) {
        return 0;
    }

    @Override
    double xAxisInverseProjection(double input) {
        return 0;
    }

    public static void main(String[] args) {
//        System.out.println(new EllipticalMercator().xAxisProjection(21.0283568));
//        System.out.println(new EllipticalMercator().yAxisProjection(105.8513213));
//        System.out.println(Math.toRadians(105.8513213));
//        System.out.println(Math.toDegrees(Math.toRadians(105.8513213)));


        double lat = Math.toRadians(21.0283568);
        System.out.println(lat);
        double y = 6378.1 * Math.log(Math.tan(Math.PI / 4 + lat / 2));
        System.out.println(y);
        double y1 = 2 * Math.atan(Math.exp(y / 6378.1)) - Math.PI / 2;
        System.out.println(Math.toDegrees(y1));
    }
}

