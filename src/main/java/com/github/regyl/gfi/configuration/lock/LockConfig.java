package com.github.regyl.gfi.configuration.lock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

@Configuration
public class LockConfig {

    @Bean
    public CountDownLatch metadataLatch() {
        return new CountDownLatch(1);
    }
}
