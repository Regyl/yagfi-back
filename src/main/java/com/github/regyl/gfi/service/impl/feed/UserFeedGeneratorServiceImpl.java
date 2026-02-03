package com.github.regyl.gfi.service.impl.feed;

import com.github.regyl.gfi.controller.dto.github.repos.UserDataGraphQlResponseDto;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.model.SbomModel;
import com.github.regyl.gfi.model.UserFeedRequestStatuses;
import com.github.regyl.gfi.repository.UserFeedRequestRepository;
import com.github.regyl.gfi.service.ScheduledService;
import com.github.regyl.gfi.service.feed.cyclonedx.CycloneDxService;
import com.github.regyl.gfi.util.ResourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpHost;
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
    private final CycloneDxService cycloneDxService;
    private final BiConsumer<SbomModel, Throwable> resultConsumer;

    @Override
    @Scheduled(fixedRate = 60_000, initialDelay = 1_000)
    public void schedule() {
        boolean isAnyAlive = cycloneDxService.anyAlive();
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
        log.info("Started feed generation for nickname {}", entity.getNickname());
        repository.updateStatusById(entity.getId(), UserFeedRequestStatuses.PROCESSING);
        process(entity);
    }

    private void process(UserFeedRequestEntity rq) {
        String nickname = rq.getNickname();
        UserDataGraphQlResponseDto responseDto = getRepos(nickname);
        Queue<String> userRepos = new ArrayDeque<>(responseDto.getRepoUrls());

        while (!userRepos.isEmpty()) {
            Queue<HttpHost> hosts = cycloneDxService.getFreeHosts();
            while (!hosts.isEmpty() && !userRepos.isEmpty()) {
                String url = userRepos.poll();
                HttpHost host = hosts.poll();
                cycloneDxService.getSbom(url, host)
                        .whenComplete((dto, throwable) -> resultConsumer.accept(new SbomModel(rq, dto, url), throwable));
            }

            LockSupport.parkNanos(Duration.ofMinutes(1L).toNanos());
        }

        repository.deleteById(rq.getId());
        log.info("Finished generating feed for nickname {}", nickname);
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
