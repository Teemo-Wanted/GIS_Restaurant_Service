package com.wanted.restaurant.util.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wanted.restaurant.util.openAPI.OpenAPIPipeline;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataPipelineScheduler {

    private final OpenAPIPipeline openAPIPipeline;

    public DataPipelineScheduler(OpenAPIPipeline openAPIPipeline) {
        this.openAPIPipeline = openAPIPipeline;
    }

    @Scheduled(cron = "0 30 9 ? * 1", zone = "Asia/Seoul")
    public void runScheduler() {
        log.info("================ start running scheduler ================");
        try {
            openAPIPipeline.pipeline();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
