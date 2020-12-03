package edu.ptit.da2020.service;

import edu.ptit.da2020.init.LoadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrafficService {
    @Autowired
    LoadFile loadFile;

    public int getTrafficStatusByRoadId(String id) {
        return loadFile.getListCongestions().get(id);
    }
}
