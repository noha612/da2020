package edu.ptit.da2020.controller;

import edu.ptit.da2020.model.Coordinate;
import edu.ptit.da2020.model.dto.Direction;
import edu.ptit.da2020.model.dto.Moving;
import edu.ptit.da2020.model.dto.Transport;
import edu.ptit.da2020.model.entity.Intersection;
import edu.ptit.da2020.service.MapService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/da2020/v1")
public class ApiController {
    @Autowired
    MapService mapService;

    @GetMapping(value = "/routes")
    public List<Intersection> hehe(@RequestParam String startId, @RequestParam String finishId) {
//        return mapService.findRoute(startId, finishId);
        return mapService.findRouteNewApproach(startId, finishId);
    }

    @GetMapping(value = "/locations")
    public List<String> hehe(@RequestParam String name) {
        return mapService.findIdByName(name);
    }

    @GetMapping(value = "/location")
    public String getNearestLocationByCoordinate(@RequestParam double lat, @RequestParam double lng) {
        return mapService.findNearestLocationByCoordinate(lat, lng);
//        System.out.println(edgeRepository.getEstimateSpeedByIntersactionIdFromAndIntersactionIdTo("2291276248", "2291276149"));
//        return null;
    }

    @GetMapping(value = "/directions")
    public Direction getDirection(
            @RequestParam(required = false) String fromId,
            @RequestParam(required = false) String toId,
            @RequestParam(required = false) String fromLat,
            @RequestParam(required = false) String toLat,
            @RequestParam(required = false) String fromLng,
            @RequestParam(required = false) String toLng
    ) {
        if (StringUtils.isNotEmpty(fromId) && StringUtils.isNotEmpty(toId)) {
            Direction direction = new Direction();
            List<Intersection> lsIts = mapService.findRoute(fromId, toId);
            direction.setFrom(new Coordinate(lsIts.get(0).getLatitude(), lsIts.get(0).getLongitude()));
            direction.setTo(new Coordinate(lsIts.get(lsIts.size() - 1).getLatitude(), lsIts.get(lsIts.size() - 1).getLongitude()));
            List<Coordinate> lsCoor = new ArrayList<>();
            for (Intersection i : lsIts) {
                lsCoor.add(new Coordinate(i.getLatitude(), i.getLongitude()));
            }
            Moving mv = new Transport();
            mv.setRoute(lsCoor);
            List<Moving> mvs = new ArrayList<>();
            mvs.add(mv);
            direction.setMovings(mvs);
            return direction;
        }
        if (StringUtils.isNotEmpty(fromLat) && StringUtils.isNotEmpty(toLat)
                && StringUtils.isNotEmpty(fromLng) && StringUtils.isNotEmpty(toLng)) {
            Direction direction = new Direction();
            return direction;
        }
        return null;
    }

}
