package edu.ptit.da2020.util;

import edu.ptit.da2020.model.entity.Intersection;
import edu.ptit.da2020.util.algorithm.Scorer;

public class HaversineScorer implements Scorer<Intersection> {
    public static void main(String[] args) {
        Intersection a = new Intersection("0", 0, 0);
        Intersection b = new Intersection("0", 0.00, 0.016);
        System.out.println(new HaversineScorer().computeCost(a, b));
    }

    @Override
    public double computeCost(Intersection from, Intersection to) {
        double R = 6372.8; // km

        double dLat = Math.toRadians(to.getLatitude() - from.getLatitude());
        double dLon = Math.toRadians(to.getLongitude() - from.getLongitude());
        double lat1 = Math.toRadians(from.getLatitude());
        double lat2 = Math.toRadians(to.getLatitude());

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}
