package edu.ptit.da2020.service.impl;

import edu.ptit.da2020.config.DataLoader;
import edu.ptit.da2020.constant.BaseConstant;
import edu.ptit.da2020.model.dto.AlertDTO;
import edu.ptit.da2020.service.TrafficService;

import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TrafficServiceImpl implements TrafficService {

    @Autowired
    DataLoader dataLoader;

    @Autowired
    RedisTemplate redisTemplate;

    public Integer getTrafficStatusByRoadId(String id) {
        return dataLoader.getListCongestions().get(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(AlertDTO alertDTO) {

        String key = alertDTO.getRoadId() + ":" + alertDTO.getMobileId();
        redisTemplate.opsForValue().set(key, alertDTO.getTrafficLevel());
        redisTemplate.expire(key, 10, TimeUnit.MINUTES);

        LinkedHashSet<String> keySet = (LinkedHashSet<String>) redisTemplate
                .keys("*" + alertDTO.getRoadId() + "*");
        List<String> level = redisTemplate.opsForValue().multiGet(keySet);
        Map<String, String> elector = new HashMap<>();
        int i = 0;
        for (String k : keySet) {
            elector.put(k.split(":")[1], level.get(i));
            i++;
        }
        String result = vote(elector);
        if (!BaseConstant.UNDEFINED.equalsIgnoreCase(result)) {
            for (Entry<String, String> entry : elector.entrySet()) {
                Double score = (Double) redisTemplate.opsForValue().get(entry.getKey());
                score += 0.25;
                redisTemplate.opsForValue().set(entry.getKey(), score);
                redisTemplate.opsForHash().put("CONGEST", alertDTO.getRoadId(), result);
                redisTemplate.opsForHash().put("OBSERVER", alertDTO.getRoadId(), LocalDateTime.now().plusMinutes(1));
            }
            for (String k : keySet) redisTemplate.delete(k);
        }
    }

    private String vote(Map<String, String> elector) {
        double avg1 = 0;
        double avg2 = 0;
        double avg3 = 0;
        for (Entry<String, String> entry : elector.entrySet()) {
            Double score = (Double) redisTemplate.opsForValue().get(entry.getKey());
            if (score == null) {
                score = 1.0;
            }
            if (BaseConstant.SPEED_SMOOTH.equalsIgnoreCase(entry.getValue())) {
                avg1 += score;
            }
            if (BaseConstant.SPEED_MILD.equalsIgnoreCase(entry.getValue())) {
                avg2 += score;
            }
            if (BaseConstant.SPEED_HEAVY.equalsIgnoreCase(entry.getValue())) {
                avg3 += score;
            }
        }
        if (avg3 > avg1 && avg3 > avg2 && avg3 >= 5) {
            return BaseConstant.SPEED_HEAVY;
        }
        if (avg2 > avg3 && avg2 > avg1 && avg2 >= 5) {
            return BaseConstant.SPEED_MILD;
        }
        if (avg1 > avg3 && avg1 > avg2 && avg1 >= 5) {
            return BaseConstant.SPEED_SMOOTH;
        }
        return BaseConstant.UNDEFINED;
    }

}
