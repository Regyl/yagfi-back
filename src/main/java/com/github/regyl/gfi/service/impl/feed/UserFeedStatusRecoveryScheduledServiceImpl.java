package com.github.regyl.gfi.service.impl.feed;

import com.github.regyl.gfi.repository.UserFeedRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.properties.feed-generation.enabled", havingValue = "true")
public class UserFeedStatusRecoveryScheduledServiceImpl implements ApplicationListener<ApplicationStartedEvent> {

    private final UserFeedRequestRepository repository;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        repository.resetProcessingRecords();
        log.info("Reset processing user feed requests");
    }
}
