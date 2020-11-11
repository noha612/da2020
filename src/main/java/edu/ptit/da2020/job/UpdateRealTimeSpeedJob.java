package edu.ptit.da2020.job;

import org.springframework.scheduling.annotation.Scheduled;

public class UpdateRealTimeSpeedJob {

    @Scheduled(cron = "0 0/20 * * * ?")
    public void doJob() {

    }
}
