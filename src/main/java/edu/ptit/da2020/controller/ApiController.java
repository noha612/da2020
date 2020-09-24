package edu.ptit.da2020.controller;

import edu.ptit.da2020.model.Intersection;
import edu.ptit.da2020.model.Location;
import edu.ptit.da2020.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/da2020/v1")
public class ApiController {
    @Autowired
    MapService mapService;

    @PostMapping(value = "/post")
    public String hihi(@RequestBody String x) {
        return x + "hihi";
    }

    @GetMapping(value = "/findRoute")
    public List<Intersection> hehe(@RequestParam String startId, @RequestParam String finishId) {
        return mapService.findRoute(startId, finishId);
    }

    @GetMapping(value = "/findStation")
    public List<Location> hehe(@RequestParam String name) {
        return mapService.findIdByName(name);
    }
}
