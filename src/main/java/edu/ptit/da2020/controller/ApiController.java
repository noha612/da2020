package edu.ptit.da2020.controller;

import edu.ptit.da2020.config.DataLoader;
import edu.ptit.da2020.model.GeoPoint;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.model.Place;
import edu.ptit.da2020.model.dto.AlertDTO;
import edu.ptit.da2020.model.dto.Direction;
import edu.ptit.da2020.model.dto.Location;
import edu.ptit.da2020.model.dto.Road;
import edu.ptit.da2020.service.DirectionService;
import edu.ptit.da2020.service.LocatingService;
import edu.ptit.da2020.service.TrafficService;
import edu.ptit.da2020.util.MathUtil;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApiController implements ApiInterface {
    @Autowired
    DirectionService directionService;

    @Autowired
    LocatingService locatingService;

    @Autowired
    TrafficService trafficService;

    @Autowired
    DataLoader dataLoader;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public List<Place> getListPlaceByName(String name) {
        return locatingService.findIdByName(name);
    }

    @Override
    public Location getLocationByPoint(double lat, double lng) {
        Location l = locatingService.findLocationByPoint(lat, lng);
        if (!l.getH().getLng().equals(l.getMarker().getLng()) && !l.getH().getLat().equals(l.getMarker().getLat())) {
            l.getPlace().setName("gần " + l.getPlace().getName());
        }
        return l;
    }

    @Override
    public Road getRoadByPoint(double lat, double lng) {
        return locatingService.findRoadByPoint(lat,lng);
    }

    @Override
    public Direction getDirection(String fromId, String toId) {
        if (StringUtils.isNotEmpty(fromId) && StringUtils.isNotEmpty(toId)) {
            Direction direction = new Direction();
            List<Junction> lsIts = directionService.findRoute(fromId, toId);
            direction.setFrom(new GeoPoint(lsIts.get(0).getLat(), lsIts.get(0).getLng()));
            direction.setTo(new GeoPoint(lsIts.get(lsIts.size() - 1).getLat(), lsIts.get(lsIts.size() - 1).getLng()));
            direction.setJunctions(lsIts);
            Map<String, Integer> traffics = new LinkedHashMap<>();
            for (int i = 0; i < lsIts.size() - 1; i++) {
                traffics.put(lsIts.get(i).getId() + "_" + lsIts.get(i + 1).getId(), dataLoader.getListCongestions().get(lsIts.get(i).getId() + "_" + lsIts.get(i + 1).getId()));
            }
            direction.setTraffics(traffics);
            log.info(direction.toString());
            log.info(direction.getJunctions().size() + "");
            log.info(direction.length() + "");
            return direction;
        }
        return null;
    }

    @Override
    public Integer getTraffic(String id) {
        return trafficService.getTrafficStatusByRoadId(id);
    }

    @Override
    public double getDistance(String fromId, String toId, Double fromLat, Double fromLng, Double toLat, Double toLng) {
        if (StringUtils.isNotEmpty(fromId) && StringUtils.isNotEmpty(toId)) {
            GeoPoint from = new GeoPoint(dataLoader.getListV().get(fromId)[0], dataLoader.getListV().get(fromId)[1]);
            GeoPoint to = new GeoPoint(dataLoader.getListV().get(toId)[0], dataLoader.getListV().get(toId)[1]);
            return MathUtil.haversineFomular(from, to);
        } else return MathUtil.haversineFomular(fromLat, fromLng, toLat, toLng);
    }

    @Override
    public void updateCongestion(AlertDTO alertDTO) {
        trafficService.update(alertDTO);
    }

    @Override
    public void test() {
        redisTemplate.opsForHash().put("CONGEST", "id1", 1);
        redisTemplate.opsForHash().put("CONGEST", "id2", 1);
        redisTemplate.opsForHash().put("CONGEST", "id2", 2);
        redisTemplate.opsForHash().put("CONGEST", "id3", 3);

        LinkedHashSet<String> keySet = (LinkedHashSet<String>) redisTemplate.keys("*roadId*");
        List<Integer> level = redisTemplate.opsForValue().multiGet(keySet);
        int i = 0;
        for (String k : keySet) {
            log.info(k + " " + level.get(i));
            i++;
        }
    }
}
