package edu.ptit.da2020.service;

import edu.ptit.da2020.config.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrafficService {
    @Autowired
    DataLoader dataLoader;

    public int getTrafficStatusByRoadId(String id) {
        return dataLoader.getListCongestions().get(id);
    }
}
