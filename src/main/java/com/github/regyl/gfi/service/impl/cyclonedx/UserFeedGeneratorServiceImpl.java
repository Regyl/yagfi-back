package com.github.regyl.gfi.service.impl.cyclonedx;

import com.github.regyl.gfi.controller.dto.cyclonedx.sbom.SbomResponseDto;
import com.github.regyl.gfi.controller.dto.github.repos.UserDataGraphQlResponseDto;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.model.UserFeedRequestStatuses;
import com.github.regyl.gfi.repository.UserFeedRequestRepository;
import com.github.regyl.gfi.service.ScheduledService;
import com.github.regyl.gfi.service.cyclonedx.CycloneDxProxyService;
import com.github.regyl.gfi.util.ResourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFeedGeneratorServiceImpl implements ScheduledService {

    private static final String QUERY = ResourceUtil.getFilePayload("graphql/github-user-repos-request.graphql");

    private final GraphQlClient githubClient;
    private final UserFeedRequestRepository repository;
    private final CycloneDxProxyService cycloneDxProxyService;
    private final BiConsumer<SbomResponseDto, Throwable> resultConsumer;

    @Override
    @Scheduled(fixedRateString = "${spring.properties.auto-upload.period-mills}", initialDelay = 1000)
    public void schedule() {
        boolean isAnyAlive = cycloneDxProxyService.anyAlive();
        if (!isAnyAlive) {
            log.info("All cdxgen services are busy, will try again later");
            return;
        }

        Optional<UserFeedRequestEntity> optionalRequest = repository.findOldestByStatus(UserFeedRequestStatuses.WAITING_FOR_PROCESS.getValue());
        if (optionalRequest.isEmpty()) {
            log.info("No user feed request found, will try again later");
            return;
        }

        UserFeedRequestEntity entity = optionalRequest.get();
        repository.updateStatusById(entity.getId(), UserFeedRequestStatuses.PROCESSING);
        idk(entity);
    }

    private void idk(UserFeedRequestEntity rq) {
        UserDataGraphQlResponseDto responseDto = getRepos(rq.getNickname());
        Queue<String> userRepos = new ArrayDeque<>(responseDto.getRepoUrls());

        while (!userRepos.isEmpty()) {
            int freeThreads = cycloneDxProxyService.getFreeServiceQuantity();
            for (int i = 0; i < freeThreads; i++) {
                cycloneDxProxyService.getSbom(userRepos.poll())
                        .whenComplete(resultConsumer);
            }

            LockSupport.parkNanos(Duration.ofMinutes(1L).toNanos());
        }
    }

    private UserDataGraphQlResponseDto getRepos(String login) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("login", login);
        ClientGraphQlResponse clientGraphQlResponse = githubClient.document(QUERY)
                .variables(variables)
                .executeSync();
        if (!clientGraphQlResponse.isValid()) {
            log.error("graph ql response is invalid");
        }
        return clientGraphQlResponse.toEntity(UserDataGraphQlResponseDto.class);
    }
}
