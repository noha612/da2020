package edu.ptit.da2020.service;

import edu.ptit.da2020.config.GraphConfiguration;
import edu.ptit.da2020.model.Intersection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapService {
    @Autowired
    GraphConfiguration graphConfiguration;

    public List<Intersection> findRoute(String startId, String finishId) {
        return graphConfiguration.getRouteFinder().findRouteAStarAlgorithm(
                graphConfiguration.getMap().getNode(startId),
                graphConfiguration.getMap().getNode(finishId)
        );
    }

    public String findIdByName(String name) {
        for (String i : graphConfiguration.getLocations().keySet()) {
            if (i.contains(name)) return i + " " + graphConfiguration.getLocations().get(i);
        }
        for (String i : graphConfiguration.getLocations().keySet()) {
            if (i.toLowerCase().contains(name.toLowerCase())) return i + " " + graphConfiguration.getLocations().get(i);
        }
        return "Not found id";
    }
}
