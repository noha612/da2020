package edu.ptit.da2020.service;

import edu.ptit.da2020.config.GraphConfiguration;
import edu.ptit.da2020.model.Intersection;
import edu.ptit.da2020.model.Location;
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

    public Location findIdByName(String name) {
        for (String i : graphConfiguration.getLocations().keySet()) {
            if (i.contains(name)) {
                for (Intersection j : graphConfiguration.getIntersections()) {
                    if (j.getId().equals(graphConfiguration.getLocations().get(i))) {
                        return new Location(i, j);
                    }
                }
            }
        }
        for (String i : graphConfiguration.getLocations().keySet()) {
            if (i.toLowerCase().contains(name.toLowerCase())) {
                for (Intersection j : graphConfiguration.getIntersections()) {
                    if (j.getId().equals(graphConfiguration.getLocations().get(i))) {
                        return new Location(i, j);
                    }
                }
            }
        }
        return null;
    }
}
