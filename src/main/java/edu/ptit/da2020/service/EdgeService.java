package edu.ptit.da2020.service;

import edu.ptit.da2020.config.GraphConfiguration;
import edu.ptit.da2020.model.entity.EdgeEntity;
import edu.ptit.da2020.model.entity.Intersection;
import edu.ptit.da2020.repository.EdgeRepository;
import edu.ptit.da2020.repository.IntersectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class EdgeService {

    @Autowired
    GraphConfiguration graphConfiguration;

    @Autowired
    EdgeRepository edgeRepository;

    @Autowired
    IntersectionRepository intersectionRepository;

    public void insertDBFromXML() {
        Map<String, Set<String>> connections = graphConfiguration.getConnections();
        for (String intersactionId1 : connections.keySet()) {
            for (String intersactionId2 : connections.get(intersactionId1)) {
                Intersection i1 = intersectionRepository.findById(intersactionId1).get();
                Intersection i2 = intersectionRepository.findById(intersactionId2).get();

                double R = 6372.8;

                double dLat = Math.toRadians(i2.getLatitude() - i1.getLatitude());
                double dLon = Math.toRadians(i2.getLongitude() - i1.getLongitude());
                double lat1 = Math.toRadians(i1.getLatitude());
                double lat2 = Math.toRadians(i2.getLatitude());

                double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
                double c = 2 * Math.asin(Math.sqrt(a));

                EdgeEntity edgeEntity = EdgeEntity
                        .builder()
                        .intersactionId1(intersactionId1)
                        .intersactionId2(intersactionId2)
                        .haversineScorer(R * c)
                        .build();
                edgeRepository.save(edgeEntity);
            }
        }
    }
}
