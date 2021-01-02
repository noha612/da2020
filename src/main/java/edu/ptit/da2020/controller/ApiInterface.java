package edu.ptit.da2020.controller;

import edu.ptit.da2020.model.Place;
import edu.ptit.da2020.model.dto.Direction;
import edu.ptit.da2020.model.dto.Location;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/da2020/v1")
public interface ApiInterface {

    @GetMapping(value = "/places")
    public List<Place> getListPlaceByName(@RequestParam(name = "name") String name);

    @GetMapping(value = "/locations")
    public Location getLocationByPoint(@RequestParam(name = "lat") double lat, @RequestParam(name = "lng") double lng);


    @GetMapping(value = "/directions")
    public Direction getDirection(
            @RequestParam(required = false, name = "from-id") String fromId,
            @RequestParam(required = false, name = "to-id") String toId,
            @RequestParam(required = false, name = "x") Double x
    );

    @GetMapping(value = "/traffics")
    public Integer getTraffic(@RequestParam(name = "road-id") String id);

    @GetMapping(value = "/distance")
    public double getDistance(
            @RequestParam(required = false, name = "from-id") String fromId,
            @RequestParam(required = false, name = "to-id") String toId,
            @RequestParam(required = false, name = "from-lat") Double fromLat,
            @RequestParam(required = false, name = "from-lng") Double fromLng,
            @RequestParam(required = false, name = "to-lat") Double toLat,
            @RequestParam(required = false, name = "to-lng") Double toLng
    );

    @PostMapping(value = "/redis")
    public String updateCongestion(@RequestBody String s);
}
