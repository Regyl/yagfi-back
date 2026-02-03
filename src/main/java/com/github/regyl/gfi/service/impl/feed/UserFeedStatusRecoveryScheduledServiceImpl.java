package com.github.regyl.gfi.service.impl.feed;

import com.github.regyl.gfi.repository.UserFeedRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFeedStatusRecoveryScheduledServiceImpl implements ApplicationListener<ApplicationStartedEvent> {

    private final UserFeedRequestRepository repository;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        repository.resetProcessingRecords();
        log.info("Reset processing user feed requests");
    }
}
