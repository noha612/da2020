package edu.ptit.da2020.controller;

import edu.ptit.da2020.model.Place;
import edu.ptit.da2020.model.dto.AlertDTO;
import edu.ptit.da2020.model.dto.Direction;
import edu.ptit.da2020.model.dto.Location;
import edu.ptit.da2020.model.dto.Road;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/da2020/v1")
public interface ApiInterface {

    @GetMapping(value = "/places")
     List<Place> getListPlaceByName(@RequestParam(name = "name") String name);

    @GetMapping(value = "/locations")
     Location getLocationByPoint(@RequestParam(name = "lat") double lat, @RequestParam(name = "lng") double lng);

    @GetMapping(value = "/roads")
    Road getRoadByPoint(@RequestParam(name = "lat") double lat, @RequestParam(name = "lng") double lng);

    @GetMapping(value = "/directions")
     Direction getDirection(
            @RequestParam(required = false, name = "from-id") String fromId,
            @RequestParam(required = false, name = "to-id") String toId
    );

    @GetMapping(value = "/traffics")
     Integer getTraffic(@RequestParam(name = "road-id") String id);

    @GetMapping(value = "/distance")
     double getDistance(
            @RequestParam(required = false, name = "from-id") String fromId,
            @RequestParam(required = false, name = "to-id") String toId,
            @RequestParam(required = false, name = "from-lat") Double fromLat,
            @RequestParam(required = false, name = "from-lng") Double fromLng,
            @RequestParam(required = false, name = "to-lat") Double toLat,
            @RequestParam(required = false, name = "to-lng") Double toLng
    );

    @PostMapping(value = "/alert")
     void updateCongestion(@RequestBody AlertDTO alertDTO);

  @PostMapping(value = "/test")
  void test();
}
