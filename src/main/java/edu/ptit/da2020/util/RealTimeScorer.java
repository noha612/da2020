package edu.ptit.da2020.util;

import edu.ptit.da2020.init.MapGraph;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.util.algorithm.Scorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RealTimeScorer implements Scorer<Junction> {
    @Autowired
    MapGraph mapGraph;

    @Override
    public double computeCost(Junction from, Junction to) {
        double R = 6372.8; // km

        double dLat = Math.toRadians(to.getLat() - from.getLat());
        double dLon = Math.toRadians(to.getLng() - from.getLng());
        double lat1 = Math.toRadians(from.getLat());
        double lat2 = Math.toRadians(to.getLat());

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return R * c;
    }
}
