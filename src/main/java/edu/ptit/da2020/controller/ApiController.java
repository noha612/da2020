package edu.ptit.da2020.controller;

import edu.ptit.da2020.model.Location;
import edu.ptit.da2020.model.entity.Intersection;
import edu.ptit.da2020.repository.EdgeRepository;
import edu.ptit.da2020.service.EdgeService;
import edu.ptit.da2020.service.IntersectionService;
import edu.ptit.da2020.service.LocationService;
import edu.ptit.da2020.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/da2020/v1")
public class ApiController {
    @Autowired
    MapService mapService;

    @Autowired
    IntersectionService intersectionService;

    @Autowired
    LocationService locationService;

    @Autowired
    EdgeService edgeService;

    @PostMapping(value = "/intersections")
    public String insertIntersections() {
//        intersectionService.insertDBFromXML();
        return "ok!";
    }

    @PostMapping(value = "/locations")
    public String insertLocations() {
//        locationService.insertDBFromXML();
        return "ok!";
    }

    @PostMapping(value = "/edges")
    public String insertEdges() {
//        edgeService.insertDBFromXML();
        return "ok!";
    }

    @GetMapping(value = "/routes")
    public List<Intersection> hehe(@RequestParam String startId, @RequestParam String finishId) {
//        return mapService.findRoute(startId, finishId);
        return mapService.findRouteNewApproach(startId, finishId);
    }

    @GetMapping(value = "/locations")
    public List<Location> hehe(@RequestParam String name) {
        return mapService.findIdByName(name);
    }

    @Autowired
    EdgeRepository edgeRepository;

    @GetMapping(value = "/location")
    public Location getNearestLocationByCoordinate(@RequestParam double lat, @RequestParam double lng) {
        return mapService.findNearestLocationByCoordinate( lat,  lng);
//        System.out.println(edgeRepository.getEstimateSpeedByIntersactionIdFromAndIntersactionIdTo("2291276248", "2291276149"));
//        return null;
    }

}
