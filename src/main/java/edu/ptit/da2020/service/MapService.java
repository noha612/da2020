package edu.ptit.da2020.service;

import edu.ptit.da2020.init.LoadFile;
import edu.ptit.da2020.init.MapGraph;
import edu.ptit.da2020.model.entity.Intersection;
import edu.ptit.da2020.util.CommonUtils;
import edu.ptit.da2020.util.HaversineScorer;
import edu.ptit.da2020.util.HaversineToTimeScorer;
import edu.ptit.da2020.util.algorithm.RouteFinder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MapService {
    private static final String EDGE = "src/main/resources/map/HN_edge.txt";
    @Autowired
    LoadFile loadFile;

    @Autowired
    MapGraph mapGraph;

    @Autowired
    HaversineToTimeScorer haversineToTimeScorer;

    public List<Intersection> findRoute(String startId, String finishId) {
        mapGraph.setRouteFinder(new RouteFinder<>(mapGraph.getGraph(), new HaversineScorer(), new HaversineScorer()));
        return mapGraph.getRouteFinder().findRouteAStarAlgorithm(
                mapGraph.getGraph().getNode(startId),
                mapGraph.getGraph().getNode(finishId)
        );
    }

    public List<Intersection> findRouteNewApproach(String startId, String finishId) {
        mapGraph.setRouteFinder(new RouteFinder<>(mapGraph.getGraph(), haversineToTimeScorer, haversineToTimeScorer));
        return mapGraph.getRouteFinder().findRouteAStarAlgorithm(
                mapGraph.getGraph().getNode(startId),
                mapGraph.getGraph().getNode(finishId)
        );
    }

    public List<String> findIdByName(String name) {
        name = CommonUtils.removeAccents(name);
        name = name.toLowerCase();
        String[] nameSplit = name.split("\\s+");
        List<Integer[]> list = new ArrayList<>();
        for (String i : nameSplit) {
            if (loadFile.getIi().containsKey(i))
                list.add(loadFile.getIi().get(i));
            else {
                Integer[] integers = new Integer[0];
                for (String s : loadFile.getIi().keySet()) {
                    if (s.startsWith(i)) {
                        integers = ArrayUtils.addAll(integers, loadFile.getIi().get(s));
                        if (integers.length > 10) {
                            list.add(integers);
                            break;
                        }
                    }
                }
            }
        }
        if (list.size() > 0) {
            ArrayList<String> result = new ArrayList<>();
            Integer[] li = CommonUtils.intersectionArrays(list);
            for (Integer i : li) {
                result.add(loadFile.getListName().get(i));
            }
            result.sort((s1, s2) -> {
                s1 = CommonUtils.removeAccents(s1);
                s1 = s1.toLowerCase();
                s2 = CommonUtils.removeAccents(s2);
                s2 = s2.toLowerCase();
                if (s1.indexOf(nameSplit[0]) < s2.indexOf(nameSplit[0]))
                    return -1;
                return 0;
            });
            return result.size() > 10 ? result.subList(0, 10) : result;

        }
        return null;
    }

    public String findNearestLocationByCoordinate(double lat, double lng) {
        String result = "hehe";
        double d = Double.MAX_VALUE;
        for (Intersection i : mapGraph.getNodes()) {
            double lat1 = i.getLatitude();
            double lng1 = i.getLongitude();
            double tempDis = CommonUtils.distance(lat, lat1, lng, lng1);
            if (tempDis < d) {
                result = i.getId();
                d = tempDis;
            }
        }
        return result;
    }
}
