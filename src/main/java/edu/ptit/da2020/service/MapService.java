package edu.ptit.da2020.service;

import edu.ptit.da2020.config.GraphConfiguration;
import edu.ptit.da2020.model.Location;
import edu.ptit.da2020.model.entity.Intersection;
import edu.ptit.da2020.util.RealTimeScorer;
import edu.ptit.da2020.util.HaversineScorer;
import edu.ptit.da2020.util.HaversineToTimeScorer;
import edu.ptit.da2020.util.algorithm.RouteFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MapService {
    @Autowired
    GraphConfiguration graphConfiguration;

    public List<Intersection> findRoute(String startId, String finishId) {
        graphConfiguration.setRouteFinder(new RouteFinder<>(graphConfiguration.getMap(), new HaversineScorer(), new HaversineScorer()));
        return graphConfiguration.getRouteFinder().findRouteAStarAlgorithm(
                graphConfiguration.getMap().getNode(startId),
                graphConfiguration.getMap().getNode(finishId)
        );
    }

    @Autowired
    RealTimeScorer realTimeScorer;

    @Autowired
    HaversineToTimeScorer haversineToTimeScorer;

    public List<Intersection> findRouteNewApproach(String startId, String finishId) {
        graphConfiguration.setRouteFinder(new RouteFinder<>(graphConfiguration.getMap(), realTimeScorer, haversineToTimeScorer));
        return graphConfiguration.getRouteFinder().findRouteAStarAlgorithm(
                graphConfiguration.getMap().getNode(startId),
                graphConfiguration.getMap().getNode(finishId)
        );
    }

    public List<Location> findIdByName(String name) {
        List<Location> locations = new ArrayList<>();
        int i = 0;
        Set<String> set = graphConfiguration.getLocations().keySet();
        String[] temp = new String[set.size()];
        set.toArray(temp);
        while (locations.size() < 5 && i < temp.length) {
            if (temp[i].contains(name) || temp[i].toLowerCase().contains(name.toLowerCase())) {
                for (Intersection j : graphConfiguration.getIntersections()) {
                    if (j.getId().equals(graphConfiguration.getLocations().get(temp[i]))) {
                        locations.add(new Location(temp[i], j));
                    }
                }
            }
            i++;
        }
        return locations;
    }

    public Location findNearestLocationByCoordinate(double lat, double lng) {
        double minD = 2;
        Intersection r = null;
        for (Intersection i : graphConfiguration.getIntersections()) {
            if (lat + lng - i.getLatitude() - i.getLongitude() <= 0.016) {
                Intersection a = new Intersection("fk", lat, lng);
                double temp = new HaversineScorer().computeCost(a, i);
                if (minD > temp) {
                    minD = temp;
                    r = i;
                }
            }
        }
        double min = 21;
        Set<Intersection> set = graphConfiguration.getIntersections();
        Intersection[] temp = new Intersection[set.size()];
        set.toArray(temp);
        for (int i = 0; i < temp.length - 1; i++) {
            for (int j = i + 1; j < temp.length; j++) {
                min = Math.min(new HaversineScorer().computeCost(temp[i], temp[j]), min);
                if (min == 0) System.out.println(temp[i] + "\n" + temp[j]);
            }
        }
        System.out.println(min);
        Location re = new Location("nearest", r);
        return re;
    }
}
