package edu.ptit.da2020.service;

import edu.ptit.da2020.config.DataLoader;
import edu.ptit.da2020.model.dto.AlertDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TrafficService {
    @Autowired
    DataLoader dataLoader;

    @Autowired
    RedisTemplate redisTemplate;

    public int getTrafficStatusByRoadId(String id) {
        return dataLoader.getListCongestions().get(id);
    }

    public void update(AlertDTO alertDTO) {
        redisTemplate.opsForHash().put(alertDTO.getRoadId(), alertDTO.getMobileId(), alertDTO.getTrafficLevel());
    }
}
