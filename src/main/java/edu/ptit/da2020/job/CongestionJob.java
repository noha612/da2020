package edu.ptit.da2020.job;

import edu.ptit.da2020.config.DataLoader;
import java.util.LinkedHashSet;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CongestionJob {

    @Autowired
    DataLoader dataLoader;

    @Autowired
    RedisTemplate redisTemplate;

    @Scheduled(cron = "0 0/1 * * * ?")
    private void update() {
        log.info("START update congestion level...");
        LinkedHashSet<String> keySet = (LinkedHashSet<String>) redisTemplate.opsForHash()
            .keys("CONGEST");
        List<Integer> level = redisTemplate.opsForHash().multiGet("CONGEST", keySet);
        int i = 0;
        for (String k : keySet) {
            dataLoader.getListCongestions().put(k, level.get(i));
            i++;
        }
        log.info("DONE update congestion level...");
    }
}
