package edu.ptit.da2020.service;

import edu.ptit.da2020.init.LoadFile;
import edu.ptit.da2020.init.MapGraph;
import edu.ptit.da2020.model.entity.Intersection;
import edu.ptit.da2020.util.CommonUtils;
import edu.ptit.da2020.util.HaversineScorer;
import edu.ptit.da2020.util.HaversineToTimeScorer;
import edu.ptit.da2020.util.MathUtil;
import edu.ptit.da2020.util.algorithm.RouteFinder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        double tempDis;
        String tempResult;

        for (Map.Entry<String, String[]> entry : loadFile.getListE().entrySet()) {

            double latA = loadFile.getListV().get(entry.getValue()[0])[0];
            double lngA = loadFile.getListV().get(entry.getValue()[0])[1];

            double latB = loadFile.getListV().get(entry.getValue()[1])[0];
            double lngB = loadFile.getListV().get(entry.getValue()[1])[1];

            double AC = CommonUtils.distance(latA, lat, lngA, lng);
            if (AC == 0) {
                log.info("||| node");
                return latA + "_" + lngA;
            }

            double BC = CommonUtils.distance(latB, lat, lngB, lng);
            if (BC == 0) {
                log.info("||| node");
                return latB + "_" + lngB;
            }

            double AB = CommonUtils.distance(latA, latB, lngA, lngB);


            if (AC + BC == AB) {
                //TODO: choose A or B next?
                return lat + "_" + lng + "->A?B";
            }

            if (
                    BC * BC <= AC * AC + AB * AB &&
                            AC * AC <= BC * BC + AB * AB
            ) {
                MathUtil.TwoDimensionCoordinate A = new MathUtil.TwoDimensionCoordinate(latA, lngA);
                MathUtil.TwoDimensionCoordinate B = new MathUtil.TwoDimensionCoordinate(latB, lngB);
                MathUtil.TwoDimensionCoordinate C = new MathUtil.TwoDimensionCoordinate(lat, lng);
                MathUtil.TwoDimensionCoordinate td = MathUtil.getAltitudeCoordinateOfTriangle(A, B, C);

                double latH = td.getX();
                double lngH = td.getY();
                tempDis = CommonUtils.distance(lat, latH, lng, lngH);
                //TODO: choose A or B next?
                tempResult = latH + "_" + lngH + "->A?B";
            } else {
                tempDis = AC;
                tempResult = latA + "_" + lngA;
                if (AC > BC) {
                    tempDis = BC;
                    tempResult = latB + "_" + lngB;
                }
            }

            if (tempDis < d) {
                result = tempResult;
                d = tempDis;
            }
        }

        return result;
    }
}
