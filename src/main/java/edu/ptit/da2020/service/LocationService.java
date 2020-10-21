package edu.ptit.da2020.service;

import edu.ptit.da2020.config.GraphConfig;
import edu.ptit.da2020.model.entity.LocationEntity;
import edu.ptit.da2020.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    @Autowired
    GraphConfig graphConfig;

    @Autowired
    LocationRepository locationRepository;

    public void insertDBFromXML() {
        for (String locationName : graphConfig.getLocations().keySet()) {
            locationRepository.save(
                    LocationEntity.builder()
                            .name(locationName)
                            .intersectionId(graphConfig.getLocations().get(locationName))
                            .build()
            );
        }
    }
}
