package edu.ptit.da2020.job;

import edu.ptit.da2020.config.DataLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CongestionJob {
    @Autowired
    DataLoader dataLoader;

    @Scheduled(cron = "0 0/5 * * * ?")
    private void update() {
        log.info("update congestion level...");
        dataLoader.loadCongestion();
    }
}
