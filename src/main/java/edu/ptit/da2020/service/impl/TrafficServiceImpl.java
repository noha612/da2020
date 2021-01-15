package edu.ptit.da2020.service.impl;

import edu.ptit.da2020.config.DataLoader;
import edu.ptit.da2020.model.dto.AlertDTO;
import edu.ptit.da2020.service.TrafficService;
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

    @Override
    public void update(AlertDTO alertDTO) {

        redisTemplate.opsForSet().add("OBSERVER", alertDTO.getRoadId());

        String key = alertDTO.getRoadId() + ":" + alertDTO.getMobileId();
        redisTemplate.opsForValue()
            .set(key, alertDTO.getTrafficLevel());
        redisTemplate.expire(key, 10, TimeUnit.MINUTES);

        LinkedHashSet<String> keySet = (LinkedHashSet<String>) redisTemplate
            .keys("*" + alertDTO.getRoadId() + "*");
        List<Integer> level = redisTemplate.opsForValue().multiGet(keySet);
        Map<String, Integer> elector = new HashMap<>();
        int i = 0;
        for (String k : keySet) {
            log.info(k + " " + level.get(i));
            elector.put(k.split(":")[1], level.get(i));
            i++;
        }
        log.info(vote(elector) + "");
    }

    private Integer vote(Map<String, Integer> elector) {
        double avg1 = 0;
        double avg2 = 0;
        double avg3 = 0;
        for (Entry<String, Integer> entry : elector.entrySet()) {
            Double score = (Double) redisTemplate.opsForValue().get(entry.getKey());
            if (score == null) {
                score = 1.0;
            }
            if (entry.getValue() == 1) {
                avg1 += score;
            }
            if (entry.getValue() == 2) {
                avg2 += score;
            }
            if (entry.getValue() == 3) {
                avg3 += score;
            }
        }
        if (avg3 > avg1 && avg3 > avg2 && avg3 >= 5) {
            return 3;
        }
        if (avg2 > avg3 && avg2 > avg1 && avg2 >= 5) {
            return 2;
        }
        if (avg1 > avg3 && avg1 > avg2 && avg1 >= 5) {
            return 1;
        }
        return 0;
    }

}
