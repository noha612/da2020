package edu.ptit.da2020.service;

import edu.ptit.da2020.config.GraphConfig;
import edu.ptit.da2020.repository.IntersectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntersectionService {
    @Autowired
    GraphConfig graphConfig;

    @Autowired
    IntersectionRepository intersectionRepository;

    public void insertDBFromXML() {
        intersectionRepository.saveAll(graphConfig.getIntersections());
    }
}
