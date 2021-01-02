package edu.ptit.da2020.service;

import edu.ptit.da2020.config.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TrafficService {
    @Autowired
    DataLoader dataLoader;

    @Autowired
    RedisTemplate redisTemplate;

    public int getTrafficStatusByRoadId(String id) {
        return dataLoader.getListCongestions().get(id);
    }

    public String update(String v) {
        redisTemplate.opsForValue().set("test", v);
        redisTemplate.opsForValue().set("test" + v, v);
        return redisTemplate.opsForValue().get("test").toString() + redisTemplate.opsForValue().get("test" + v).toString();
    }
}
