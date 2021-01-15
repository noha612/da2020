package edu.ptit.da2020.service;

import edu.ptit.da2020.model.dto.AlertDTO;

public interface TrafficService {

  Integer getTrafficStatusByRoadId(String id);

  void update(AlertDTO alertDTO);
}
