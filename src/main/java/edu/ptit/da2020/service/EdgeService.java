package edu.ptit.da2020.service;

import edu.ptit.da2020.config.GraphConfig;
import edu.ptit.da2020.model.entity.EdgeEntity;
import edu.ptit.da2020.model.entity.Intersection;
import edu.ptit.da2020.repository.EdgeRepository;
import edu.ptit.da2020.repository.IntersectionRepository;
import edu.ptit.da2020.repository.LocationRepository;
import edu.ptit.da2020.util.HaversineScorer;
import edu.ptit.da2020.util.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class EdgeService {

    @Autowired
    GraphConfig graphConfig;

    @Autowired
    EdgeRepository edgeRepository;

    @Autowired
    IntersectionRepository intersectionRepository;

    @Autowired
    LocationRepository locationRepository;

    public void insertDBFromXML() {
        Map<String, Set<String>> connections = graphConfig.getConnections();
        for (String intersactionId1 : connections.keySet()) {
            for (String intersactionId2 : connections.get(intersactionId1)) {
                Intersection i1 = intersectionRepository.findFirstById(intersactionId1);
                Intersection i2 = intersectionRepository.findFirstById(intersactionId2);

                double R = 6372.8;

                double dLat = Math.toRadians(i2.getLatitude() - i1.getLatitude());
                double dLon = Math.toRadians(i2.getLongitude() - i1.getLongitude());
                double lat1 = Math.toRadians(i1.getLatitude());
                double lat2 = Math.toRadians(i2.getLatitude());

                double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
                double c = 2 * Math.asin(Math.sqrt(a));

                EdgeEntity edgeEntity = EdgeEntity
                        .builder()
                        .intersactionIdFrom(intersactionId1)
                        .intersactionIdTo(intersactionId2)
                        .haversineScorer(R * c)
                        .build();
                edgeRepository.save(edgeEntity);
            }
        }
    }

    public double getRealTimeSpeed(Intersection from, Intersection to) {
        EdgeEntity edgeEntity = edgeRepository.getEstimateSpeedByIntersactionIdFromAndIntersactionIdTo(from.getId(), to.getId());
        return edgeEntity.getEstimateSpeed();
    }

    //TODO refactor
    public void abc(Intersection C) {
        for (EdgeEntity e : edgeRepository.findAll()) {
            Intersection A = intersectionRepository.findFirstById(e.getIntersactionIdFrom());
            Intersection B = intersectionRepository.findFirstById(e.getIntersactionIdTo());

            double AB = new HaversineScorer().computeCost(A, B);
            double BC = new HaversineScorer().computeCost(B, C);
            double CA = new HaversineScorer().computeCost(C, A);
            if (AB * AB <= BC * BC + CA * CA) {
//                double CH = MathUtil.getHeightOfTriangle(AB, BC, CA, CA); //ngu vcl
                MathUtil.TwoDimensionCoordinate tdA = new MathUtil.TwoDimensionCoordinate(A.getLatitude(), A.getLongitude());
                MathUtil.TwoDimensionCoordinate tdB = new MathUtil.TwoDimensionCoordinate(B.getLatitude(), B.getLongitude());
                MathUtil.TwoDimensionCoordinate tdC = new MathUtil.TwoDimensionCoordinate(C.getLatitude(), C.getLongitude());

                MathUtil.TwoDimensionCoordinate tdH = MathUtil.getAltitudeCoordinateOfTriangle(tdA, tdB, tdC);
                //return tdH
            } else {
                //return BC < CA ? B : A;
            }
        }
    }
}
