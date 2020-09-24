package edu.ptit.da2020.service;

import edu.ptit.da2020.config.GraphConfiguration;
import edu.ptit.da2020.model.Intersection;
import edu.ptit.da2020.model.Location;
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
        while (locations.size() < 5 || i == temp.length) {
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
}
