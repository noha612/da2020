package edu.ptit.da2020.service.impl;

import edu.ptit.da2020.config.DataLoader;
import edu.ptit.da2020.model.GeoPoint;
import edu.ptit.da2020.model.Place;
import edu.ptit.da2020.model.dto.Location;
import edu.ptit.da2020.model.dto.Road;
import edu.ptit.da2020.service.LocatingService;
import edu.ptit.da2020.util.CommonUtil;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LocatingServiceImpl implements LocatingService {

    @Autowired
    DataLoader dataLoader;

    @Autowired
    RedisTemplate redisTemplate;

    @SuppressWarnings("unchecked")
    @Override
    public List<Place> findIdByName(String name) {
        name = CommonUtil.removeAccents(name);
        name = name.toLowerCase();
        String[] nameSplit = name.split("\\s+");
        List<Integer[]> list = new ArrayList<>();
        for (String i : nameSplit) {
            if (dataLoader.getIi().containsKey(i)) {
                list.add(dataLoader.getIi().get(i));
            } else {
                Integer[] integers = new Integer[0];
                for (String s : dataLoader.getIi().keySet()) {
                    if (s.startsWith(i)) {
                        integers = ArrayUtils.addAll(integers, dataLoader.getIi().get(s));
                        if (integers.length > 10) {
                            list.add(integers);
                            break;
                        }
                    }
                }
            }
        }
        if (!list.isEmpty()) {
            List<Integer[]> subList = list.subList(0, list.size() - 1);
            String key = toKey(list);
            String cache = (String) redisTemplate.opsForValue().get(toKey(subList));
            if (StringUtils.isNotBlank(cache)) {
                log.info("hehe");
                list = list.subList(list.size() - 1, list.size());
                list.add(fromValue(cache));
            }
            Integer[] li = CommonUtil.intersectionArrays(list);
            redisTemplate.opsForValue().set(key, toValue(li));
            redisTemplate.expire(key, 10, TimeUnit.SECONDS);

            List<Place> result = new ArrayList<>();
            for (Integer i : li) {
                Place p = new Place();
                String[] strArr = dataLoader.getListName().get(i).split("::");
                p.setId(strArr[1]);
                p.setName(strArr[0].split("~")[0].trim());
                p.setSubName(strArr[0].split("~")[1].trim());
                result.add(p);
            }
            String finalName = name;
            result.sort((p1, p2) -> {
                String name1 = CommonUtil.removeAccents(p1.getName());
                name1 = name1.toLowerCase();
                String name2 = CommonUtil.removeAccents(p2.getName());
                name2 = name2.toLowerCase();
                if (CommonUtil.getSimilarValue(name1, finalName) > CommonUtil
                        .getSimilarValue(name2, finalName)) {
                    return -1;
                }
                if (CommonUtil.getSimilarValue(name1, finalName) < CommonUtil
                        .getSimilarValue(name2, finalName)) {
                    return 1;
                }
                return Integer.compare(name1.length(), name2.length());
            });
            result = result.size() > 8 ? result.subList(0, 8) : result;
            for (int i = 0; i < result.size(); i++) {
                Place p = result.get(i);
                Double[] coor = dataLoader.getListV().get(p.getId());
                p.setLat(coor[0]);
                p.setLng(coor[1]);
                result.set(i, p);
            }
            log.info(result.toString());
            return result;
        }
        return null;
    }

    @Override
    public Location findLocationByPoint(double lat, double lng) {
        Location location = new Location();
        location.setMarker(new GeoPoint(lat, lng));
        Place place = new Place();
        double d = Double.MAX_VALUE;
        double tempDis;
        Location tempResult = new Location();
        tempResult.setMarker(new GeoPoint(lat, lng));

        for (Map.Entry<String, String[]> entry : dataLoader.getListE().entrySet()) {

            String idA = entry.getValue()[0];
            double latA = dataLoader.getListV().get(entry.getValue()[0])[0];
            double lngA = dataLoader.getListV().get(entry.getValue()[0])[1];

            String idB = entry.getValue()[1];
            double latB = dataLoader.getListV().get(entry.getValue()[1])[0];
            double lngB = dataLoader.getListV().get(entry.getValue()[1])[1];

            double AC = CommonUtil.distance(latA, lat, lngA, lng);
            if (AC == 0) {
                log.info("||| node");
                location.setH(new GeoPoint(latA, lngA));
                place.setId(idA);
                place.setName(dataLoader.getListVN().get(idA));
                place.setLat(latA);
                place.setLng(lngA);
                location.setPlace(place);
                return location;
            }
            double BC = CommonUtil.distance(latB, lat, lngB, lng);
            if (BC == 0) {
                log.info("||| node");
                location.setH(new GeoPoint(latB, lngB));
                place.setId(idB);
                place.setName(dataLoader.getListVN().get(idB));
                place.setLat(latB);
                place.setLng(lngB);
                location.setPlace(place);
                return location;
            }
            double AB = CommonUtil.distance(latA, latB, lngA, lngB);
            if (AC + BC == AB) {
                location.setH(new GeoPoint(latA, lngA));
                place.setId(idA);
                place.setName(dataLoader.getListVN().get(idA));
                place.setLat(latA);
                place.setLng(lngA);
                location.setPlace(place);
                return location;
            }
            if (
                    BC * BC <= AC * AC + AB * AB &&
                            AC * AC <= BC * BC + AB * AB
            ) {
                CommonUtil.Coordinate A = new CommonUtil.Coordinate(latA, lngA);
                CommonUtil.Coordinate B = new CommonUtil.Coordinate(latB, lngB);
                CommonUtil.Coordinate C = new CommonUtil.Coordinate(lat, lng);
                CommonUtil.Coordinate td = CommonUtil.getAltitudeCoordinateOfTriangle(A, B, C);
                double latH = td.getX();
                double lngH = td.getY();
                tempDis = CommonUtil.distance(lat, latH, lng, lngH);
                tempResult.setH(new GeoPoint(latH, lngH));
                place.setId(idA);
                place.setName(dataLoader.getListVN().get(idA));
                place.setLat(latA);
                place.setLng(lngA);
                tempResult.setPlace(place);
            } else {
                if (AC > BC) {
                    tempDis = BC;
                    tempResult.setH(new GeoPoint(latB, lngB));
                    place.setId(idB);
                    place.setName(dataLoader.getListVN().get(idB));
                    place.setLat(latB);
                    place.setLng(lngB);
                    tempResult.setPlace(place);
                } else {
                    tempDis = AC;
                    tempResult.setH(new GeoPoint(latA, lngA));
                    place.setId(idA);
                    place.setName(dataLoader.getListVN().get(idA));
                    place.setLat(latA);
                    place.setLng(lngA);
                    tempResult.setPlace(place);
                }
            }

            if (tempDis < d) {
                location = new Location(tempResult.getMarker(), tempResult.getH(),
                        new Place(tempResult.getPlace().getName(), tempResult.getPlace().getId(),
                                tempResult.getPlace().getLat(), tempResult.getPlace().getLng()));
                d = tempDis;
            }
        }
        log.info(location.toString());

        return location;
    }

    @Override
    public Road findRoadByPoint(double lat, double lng) {
        Road road = new Road();
        double d = Double.MAX_VALUE;
        double tempDis = d;

        for (Map.Entry<String, String[]> entry : dataLoader.getListE().entrySet()) {

            String idA = entry.getValue()[0];
            double latA = dataLoader.getListV().get(entry.getValue()[0])[0];
            double lngA = dataLoader.getListV().get(entry.getValue()[0])[1];

            String idB = entry.getValue()[1];
            double latB = dataLoader.getListV().get(entry.getValue()[1])[0];
            double lngB = dataLoader.getListV().get(entry.getValue()[1])[1];

            double AC = CommonUtil.distance(latA, lat, lngA, lng);
            double BC = CommonUtil.distance(latB, lat, lngB, lng);
            double AB = CommonUtil.distance(latA, latB, lngA, lngB);

            if (AC + BC == AB) {
                return new Road(idA + "_" + idB, new GeoPoint(latA, lngA), new GeoPoint(latB, lngB));
            }
            if (
                    BC * BC <= AC * AC + AB * AB &&
                            AC * AC <= BC * BC + AB * AB
            ) {
                CommonUtil.Coordinate A = new CommonUtil.Coordinate(latA, lngA);
                CommonUtil.Coordinate B = new CommonUtil.Coordinate(latB, lngB);
                CommonUtil.Coordinate C = new CommonUtil.Coordinate(lat, lng);
                CommonUtil.Coordinate td = CommonUtil.getAltitudeCoordinateOfTriangle(A, B, C);

                double latH = td.getX();
                double lngH = td.getY();
                tempDis = CommonUtil.distance(lat, latH, lng, lngH);
                if (tempDis < 0.01 && tempDis < d) {
                    log.info(tempDis + "");
                    d = tempDis;
                    road = new Road(idA + "_" + idB, new GeoPoint(latA, lngA),
                            new GeoPoint(latB, lngB));
                }
            }
        }
        log.info(road.toString());
        return road;
    }

    private String toValue(Integer[] li) {
        StringBuilder s = new StringBuilder();
        for (Integer i : li) {
            s.append(i).append(" ");
        }
        return s.toString().trim();
    }

    private Integer[] fromValue(String li) {
        List<Integer> r = new ArrayList<>();
        for (String i : li.split(" ")) {
            r.add(Integer.parseInt(i));
        }
        return r.toArray(new Integer[0]);
    }

    @SneakyThrows
    private String toKey(List<Integer[]> list) {
        StringBuilder s = new StringBuilder();
        for (Integer[] i : list) {
            s.append(toValue(i)).append(" ");
        }
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(s.toString().getBytes());
        return Base64.getEncoder().encodeToString((messageDigest.digest()));
    }

    public static void main(String[] args) {
        System.out.println(21.013927086732753 / 20.991101806616804);
        System.out.println(105.79862568617128 / 105.80007709924588);

        System.out.println(21.008125302295525 / 20.985563516334967);
        System.out.println(105.75604232966695 / 105.75603143346574);
    }
}
