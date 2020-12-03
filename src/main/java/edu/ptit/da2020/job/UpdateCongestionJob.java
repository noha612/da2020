package edu.ptit.da2020.job;

import edu.ptit.da2020.init.DataInit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdateCongestionJob {
    @Autowired
    DataInit dataInit;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void update() {
        log.info("update congestion level...");
        dataInit.loadCongestion();
    }
}
