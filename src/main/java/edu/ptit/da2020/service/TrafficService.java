package edu.ptit.da2020.service;

import edu.ptit.da2020.init.DataInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrafficService {
    @Autowired
    DataInit dataInit;

    public int getTrafficStatusByRoadId(String id) {
        return dataInit.getListCongestions().get(id);
    }
}
