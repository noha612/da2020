package edu.ptit.da2020.util;

import edu.ptit.da2020.model.entity.Junction;
import edu.ptit.da2020.util.algorithm.Scorer;

public class HaversineScorer implements Scorer<Junction> {
    public static void main(String[] args) {
        Junction a = new Junction("0", 20.945, 105.742);
        Junction b = new Junction("0", 20.945, 105.911);

        Junction c = new Junction("0", 20.945, 105.742);
        Junction d = new Junction("0", 21.098, 105.742);
        System.out.println(new HaversineScorer().computeCost(a, b) * new HaversineScorer().computeCost(c, d));
    }

    @Override
    public double computeCost(Junction from, Junction to) {
        double R = 6372.8; // km

        double dLat = Math.toRadians(to.getLatitude() - from.getLatitude());
        double dLon = Math.toRadians(to.getLongitude() - from.getLongitude());
        double lat1 = Math.toRadians(from.getLatitude());
        double lat2 = Math.toRadians(to.getLatitude());

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c * 1000;
    }
}
