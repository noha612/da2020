package edu.ptit.da2020.service;

import edu.ptit.da2020.config.GraphConfig;
import edu.ptit.da2020.model.entity.Intersection;
import edu.ptit.da2020.util.CommonUtils;
import edu.ptit.da2020.util.HaversineScorer;
import edu.ptit.da2020.util.HaversineToTimeScorer;
import edu.ptit.da2020.util.RealTimeScorer;
import edu.ptit.da2020.util.algorithm.RouteFinder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@Slf4j
public class MapService {
    @Autowired
    GraphConfig graphConfig;

    public List<Intersection> findRoute(String startId, String finishId) {
        graphConfig.setRouteFinder(new RouteFinder<>(graphConfig.getMap(), new HaversineScorer(), new HaversineScorer()));
        return graphConfig.getRouteFinder().findRouteAStarAlgorithm(
                graphConfig.getMap().getNode(startId),
                graphConfig.getMap().getNode(finishId)
        );
    }

    @Autowired
    RealTimeScorer realTimeScorer;

    @Autowired
    HaversineToTimeScorer haversineToTimeScorer;

    public List<Intersection> findRouteNewApproach(String startId, String finishId) {
        graphConfig.setRouteFinder(new RouteFinder<>(graphConfig.getMap(), realTimeScorer, haversineToTimeScorer));
        return graphConfig.getRouteFinder().findRouteAStarAlgorithm(
                graphConfig.getMap().getNode(startId),
                graphConfig.getMap().getNode(finishId)
        );
    }

    public List<String> findIdByName(String name) {
        name = CommonUtils.removeAccents(name);
        name = name.toLowerCase();
        String[] nameSplit = name.split("\\s+");
        List<Integer[]> list = new ArrayList<>();
        for (String i : nameSplit) {
            if (graphConfig.getIi().containsKey(i))
                list.add(graphConfig.getIi().get(i));
            else {
                Integer[] integers = new Integer[0];
                for (String s : graphConfig.getIi().keySet()) {
                    if (s.startsWith(i)) {
                        integers = ArrayUtils.addAll(integers, graphConfig.getIi().get(s));
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
                result.add(graphConfig.getLn().get(i));
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

    private static final String EDGE = "HN_edge.txt";

    public String findNearestLocationByCoordinate(double lat, double lng) {
        String result = "hehe";
        double d = -1;
        log.info("start read file " + EDGE);
        try {
            File myObj = new File(EDGE);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine().trim();
                if (StringUtils.isNotEmpty(line)) {
                    String[] temp = line.split(" ");

                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.error("An error occurred " + e);
        }
        log.info("done read file " + EDGE);
        return result;
    }
}
