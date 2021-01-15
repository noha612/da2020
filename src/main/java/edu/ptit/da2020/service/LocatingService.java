package edu.ptit.da2020.service;

import edu.ptit.da2020.model.Place;
import edu.ptit.da2020.model.dto.Location;
import edu.ptit.da2020.model.dto.Road;
import java.util.List;

public interface LocatingService {

  List<Place> findIdByName(String name);

  Location findLocationByPoint(double lat, double lng);

  Road findRoadByPoint(double lat, double lng);
}
