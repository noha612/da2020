package edu.ptit.da2020.controller;

import edu.ptit.da2020.model.GeoPoint;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.model.Place;
import edu.ptit.da2020.model.dto.Direction;
import edu.ptit.da2020.model.dto.Location;
import edu.ptit.da2020.service.MapService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/da2020/v1")
public class ApiController {
    @Autowired
    MapService mapService;

    @GetMapping(value = "/places")
    public List<Place> getListPlaceByName(@RequestParam(name = "name") String name) {
        return mapService.findIdByName(name);
    }

    @GetMapping(value = "/locations")
    public Location getLocationByPoint(@RequestParam(name = "lat") double lat, @RequestParam(name = "lng") double lng) {
        return mapService.findLocationByPoint(lat, lng);
    }

    @GetMapping(value = "/directions")
    public Direction getDirection(
            @RequestParam(required = false, name = "from-id") String fromId,
            @RequestParam(required = false, name = "to-id") String toId
    ) {
        if (StringUtils.isNotEmpty(fromId) && StringUtils.isNotEmpty(toId)) {
            Direction direction = new Direction();
            List<Junction> lsIts = mapService.findRoute(fromId, toId);
            direction.setFrom(new GeoPoint(lsIts.get(0).getLat(), lsIts.get(0).getLng()));
            direction.setTo(new GeoPoint(lsIts.get(lsIts.size() - 1).getLat(), lsIts.get(lsIts.size() - 1).getLng()));
            List<GeoPoint> lsCoor = new ArrayList<>();
            for (Junction i : lsIts) {
                lsCoor.add(new GeoPoint(i.getLat(), i.getLng()));
            }
            direction.setJunctions(lsIts);
            log.info(direction.toString());
            return direction;
        }
        return null;
    }

    @GetMapping(value = "/traffics")
    public Integer getTraffic(@RequestParam(name = "road-id") String id) {
        return mapService.getTrafficStatusByRoadId(id);
    }

}
