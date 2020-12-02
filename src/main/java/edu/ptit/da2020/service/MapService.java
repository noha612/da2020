package edu.ptit.da2020.service;

import edu.ptit.da2020.init.LoadFile;
import edu.ptit.da2020.init.MapGraph;
import edu.ptit.da2020.model.GeoPoint;
import edu.ptit.da2020.model.Place;
import edu.ptit.da2020.model.dto.Location;
import edu.ptit.da2020.model.entity.Junction;
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

    public List<Junction> findRoute(String startId, String finishId) {
        mapGraph.setRouteFinder(new RouteFinder<>(mapGraph.getGraph(), new HaversineScorer(), new HaversineScorer()));
        return mapGraph.getRouteFinder().findRouteAStarAlgorithm(
                mapGraph.getGraph().getNode(startId),
                mapGraph.getGraph().getNode(finishId),
                loadFile.getListTraffic()
        );
    }

    public List<Place> findIdByName(String name) {
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
            List<Place> result = new ArrayList<>();
            Integer[] li = CommonUtils.intersectionArrays(list);
            for (Integer i : li) {
                Place p = new Place();
                String[] strArr = loadFile.getListName().get(i).split("::");
                p.setId(strArr[1]);
                p.setName(strArr[0]);
                result.add(p);
            }
            result.sort((p1, p2) -> {
                String name1 = CommonUtils.removeAccents(p1.getName());
                name1 = name1.toLowerCase();
                String name2 = CommonUtils.removeAccents(p2.getName());
                name2 = name2.toLowerCase();
                if (name1.indexOf(nameSplit[0]) < name2.indexOf(nameSplit[0]))
                    return -1;
                return 0;
            });
            result = result.size() > 10 ? result.subList(0, 10) : result;
            for (int i = 0; i < result.size(); i++) {
                Place p = result.get(i);
                Double[] coor = loadFile.getListV().get(p.getId());
                p.setLatitude(coor[0]);
                p.setLongitude(coor[1]);
                result.set(i, p);
            }
            return result;
        }
        return null;
    }

    public Location findLocationByPoint(double lat, double lng) {
        Location location = new Location();
        location.setMarker(new GeoPoint(lat, lng));
        Place place = new Place();
        double d = Double.MAX_VALUE;
        double tempDis;
        Location tempResult = new Location();
        tempResult.setMarker(new GeoPoint(lat, lng));

        for (Map.Entry<String, String[]> entry : loadFile.getListE().entrySet()) {

            String idA = entry.getValue()[0];
            double latA = loadFile.getListV().get(entry.getValue()[0])[0];
            double lngA = loadFile.getListV().get(entry.getValue()[0])[1];

            String idB = entry.getValue()[1];
            double latB = loadFile.getListV().get(entry.getValue()[1])[0];
            double lngB = loadFile.getListV().get(entry.getValue()[1])[1];

            double AC = CommonUtils.distance(latA, lat, lngA, lng);
            if (AC == 0) {
                log.info("||| node");
                location.setH(new GeoPoint(latA, lngA));
                place.setId(idA);
                place.setName(idA);
                place.setLatitude(latA);
                place.setLongitude(lngA);
                location.setPlace(place);
                return location;
            }

            double BC = CommonUtils.distance(latB, lat, lngB, lng);
            if (BC == 0) {
                log.info("||| node");
                location.setH(new GeoPoint(latB, lngB));
                place.setId(idB);
                place.setName(idB);
                place.setLatitude(latB);
                place.setLongitude(lngB);
                location.setPlace(place);
                return location;
            }

            double AB = CommonUtils.distance(latA, latB, lngA, lngB);


            if (AC + BC == AB) {
                //TODO: choose A or B next?
                location.setH(new GeoPoint(latA, lngA));
                place.setId(idA);
                place.setName(idA);
                place.setLatitude(latA);
                place.setLongitude(lngA);
                location.setPlace(place);
                return location;
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
                tempResult.setH(new GeoPoint(latH, lngH));
                place.setId(idA);
                place.setName(idA);
                place.setLatitude(latA);
                place.setLongitude(lngA);
                tempResult.setPlace(place);
            } else {
                if (AC > BC) {
                    tempDis = BC;
                    tempResult.setH(new GeoPoint(latB, lngB));
                    place.setId(idB);
                    place.setName(idB);
                    place.setLatitude(latB);
                    place.setLongitude(lngB);
                    tempResult.setPlace(place);
                } else {
                    tempDis = AC;
                    tempResult.setH(new GeoPoint(latA, lngA));
                    place.setId(idA);
                    place.setName(idA);
                    place.setLatitude(latA);
                    place.setLongitude(lngA);
                    tempResult.setPlace(place);
                }
            }

            if (tempDis < d) {
                location = new Location(tempResult.getMarker(), tempResult.getH(), new Place(tempResult.getPlace().getName(), tempResult.getPlace().getId(), tempResult.getPlace().getLatitude(), tempResult.getPlace().getLongitude()));
                log.info(location.toString());
                d = tempDis;
            }
        }
        log.info(location.toString());

        return location;
    }

    public int getTrafficStatusByRoadId(String id) {
        return loadFile.getListTraffic().get(id);
    }
}
