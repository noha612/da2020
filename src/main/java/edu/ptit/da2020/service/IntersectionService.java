package edu.ptit.da2020.service;

import edu.ptit.da2020.config.GraphConfiguration;
import edu.ptit.da2020.repository.IntersectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntersectionService {
    @Autowired
    GraphConfiguration graphConfiguration;

    @Autowired
    IntersectionRepository intersectionRepository;

    public void insertDBFromXML() {
        intersectionRepository.saveAll(graphConfiguration.getIntersections());
    }
}
