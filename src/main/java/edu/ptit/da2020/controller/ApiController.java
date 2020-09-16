package edu.ptit.da2020.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ApiController {
    @PostMapping(value = "/post")
    public String hihi(@RequestBody String x) {
        return x + "hihi";
    }
}
