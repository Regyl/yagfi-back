package com.github.regyl.gfi.service.impl.source;

import com.github.regyl.gfi.service.source.IssueSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.properties.auto-upload.enabled", havingValue = "true")
public class IssueLoaderServiceImpl {

    private final Collection<IssueSourceService> sourceServices;

    @Scheduled(fixedRateString = "${spring.properties.auto-upload.period-mills}", initialDelay = 1000) //1 hour
    public void upload() {
        sourceServices.forEach(IssueSourceService::upload);
    }
}
