package com.errami.springbatchorderprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderJobScheduler {

    private static final Logger logger = LoggerFactory.getLogger(OrderJobScheduler.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importOrdersJob;

    @Scheduled(fixedRate = 10000) // every 10 seconds
//    @Scheduled(cron = "0 0 0 * * *") // second, minute, hour, day of month, month, day(s) of week
    public void runJob() {
        try {
            JobParameters parameters = new JobParametersBuilder()
                    .addLong("startTime", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(importOrdersJob, parameters);
            System.out.println("* Batch job successfully launched.");
        } catch (Exception e) {
            logger.error("Failed to execute batch job.", e);
        }
    }
}