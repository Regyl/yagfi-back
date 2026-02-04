package com.github.regyl.gfi.configuration.schedule;

import com.github.regyl.gfi.service.ScheduledService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableScheduling
public class ScheduledConfiguration {

    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService taskScheduler(List<ScheduledService> scheduledServices) {
        int threads = 5;
        if (scheduledServices.size() > threads) {
            throw new IllegalStateException("Scheduled thread pool should be greater than thread count");
        }

        return Executors.newScheduledThreadPool(5);
    }
}
