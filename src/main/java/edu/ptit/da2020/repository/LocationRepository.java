package edu.ptit.da2020.repository;

import edu.ptit.da2020.model.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<LocationEntity, String> {
    LocationEntity findFirstByIntersectionId(String id);
}
