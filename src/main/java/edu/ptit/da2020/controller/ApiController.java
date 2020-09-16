package edu.ptit.da2020.controller;

import edu.ptit.da2020.entity.Station;
import edu.ptit.da2020.service.DatGay;
import edu.ptit.da2020.service.RouteFinderIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/da2020/v1")
public class ApiController {
    @Autowired
    DatGay datGay;

    @PostMapping(value = "/post")
    public String hihi(@RequestBody String x) {
        return x + "hihi";
    }

    @GetMapping(value = "/findRouteTest")
    public List<Station> haha(@RequestParam String startId, @RequestParam String finishId) {
        return new RouteFinderIntegrationTest().findRoute(startId, finishId);
    }

    @GetMapping(value = "/findRoute")
    public List<Station> hehe(@RequestParam String startId, @RequestParam String finishId) {
        return datGay.findRoute(startId, finishId);
    }
}
