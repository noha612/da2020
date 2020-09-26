package edu.ptit.da2020.service;

import edu.ptit.da2020.config.GraphConfiguration;
import edu.ptit.da2020.model.entity.LocationEntity;
import edu.ptit.da2020.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    @Autowired
    GraphConfiguration graphConfiguration;

    @Autowired
    LocationRepository locationRepository;

    public void insertDBFromXML() {
        for (String locationName : graphConfiguration.getLocations().keySet()) {
            locationRepository.save(
                    LocationEntity.builder()
                            .name(locationName)
                            .intersectionId(graphConfiguration.getLocations().get(locationName))
                            .build()
            );
        }
    }
}
