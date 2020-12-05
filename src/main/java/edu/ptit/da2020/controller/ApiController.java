package edu.ptit.da2020.controller;

import edu.ptit.da2020.config.DataLoader;
import edu.ptit.da2020.model.GeoPoint;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.model.Place;
import edu.ptit.da2020.model.dto.Direction;
import edu.ptit.da2020.model.dto.Location;
import edu.ptit.da2020.service.DirectionService;
import edu.ptit.da2020.service.LocatingService;
import edu.ptit.da2020.service.TrafficService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<Place> getListPlaceByName(String name) {
        return locatingService.findIdByName(name);
    }

    @Override
    public Location getLocationByPoint(double lat, double lng) {
        return locatingService.findLocationByPoint(lat, lng);
    }

    @Override
    public Direction getDirection(String fromId, String toId
    ) {
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
            return direction;
        }
        return null;
    }

    @Override
    public Integer getTraffic(String id) {
        return trafficService.getTrafficStatusByRoadId(id);
    }

}
