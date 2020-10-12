package edu.ptit.da2020.repository;

import edu.ptit.da2020.model.entity.Intersection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntersectionRepository extends JpaRepository<Intersection, String> {
    List<Intersection> findAll();
    Intersection findFirstById(String id);
}
