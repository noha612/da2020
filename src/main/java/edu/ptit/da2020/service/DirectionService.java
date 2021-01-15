package edu.ptit.da2020.service;

import edu.ptit.da2020.model.Junction;
import java.util.List;

public interface DirectionService {

  List<Junction> findRoute(String fromId, String toId);

  List<Junction> findRouteExp(String fromId, String toId, double x);
}
