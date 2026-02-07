package com.github.regyl.gfi.service.impl.feed;

import com.github.regyl.gfi.controller.dto.github.repos.UserDataGraphQlResponseDto;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.model.SbomModel;
import com.github.regyl.gfi.model.UserFeedRequestStatuses;
import com.github.regyl.gfi.model.smtp.EmailModel;
import com.github.regyl.gfi.repository.UserFeedRequestRepository;
import com.github.regyl.gfi.service.ScheduledService;
import com.github.regyl.gfi.service.email.EmailService;
import com.github.regyl.gfi.service.feed.CycloneDxService;
import com.github.regyl.gfi.service.github.GithubClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.properties.feature-enabled.feed-generation", havingValue = "true")
public class UserFeedGeneratorServiceImpl implements ScheduledService {

    private final GithubClientService<String, UserDataGraphQlResponseDto> githubClient;
    private final UserFeedRequestRepository repository;
    private final CycloneDxService cycloneDxService;
    private final BiConsumer<SbomModel, Throwable> resultConsumer;
    private final EmailService emailService;

    @Override
    @Scheduled(fixedRate = 60_000, initialDelay = 1_000)
    public void schedule() {
        //since spring's scheduling mechanism have limited core pool size
        //this method should start processing only if it's not already processing
        //another one feed request
        boolean isFree = cycloneDxService.isFree();
        if (!isFree) {
            log.info("Some cdxgen services are still busy, will try again later");
            return;
        }

        Optional<UserFeedRequestEntity> optionalRequest = repository.findOldestByStatus(
                UserFeedRequestStatuses.WAITING_FOR_PROCESS.getValue()
        );
        if (optionalRequest.isEmpty()) {
            log.debug("No user feed request found, will try again later");
            return;
        }

        UserFeedRequestEntity entity = optionalRequest.get();
        log.info("Started feed generation for nickname {}", entity.getNickname());
        repository.updateStatusById(entity.getId(), UserFeedRequestStatuses.PROCESSING);
        process(entity);
    }

    private void process(UserFeedRequestEntity rq) {
        long start = System.nanoTime();

        String nickname = rq.getNickname();
        UserDataGraphQlResponseDto responseDto = getRepos(nickname);
        Queue<String> userRepos = new ArrayDeque<>(responseDto.getRepoUrls());

        while (!userRepos.isEmpty()) {
            Queue<HttpHost> hosts = cycloneDxService.getFreeHosts();
            while (!hosts.isEmpty() && !userRepos.isEmpty()) {
                String url = userRepos.poll();
                HttpHost host = hosts.poll();
                cycloneDxService.getSbom(url, host).whenComplete((dto, throwable) ->
                                resultConsumer.accept(new SbomModel(rq, dto, url), throwable));
            }

            LockSupport.parkNanos(Duration.ofMinutes(1L).toNanos());
        }

        repository.updateStatusById(rq.getId(), UserFeedRequestStatuses.PROCESSED);

        EmailModel emailModel = new EmailModel(
                rq.getEmail(),
                "Your custom feed generated!",
                "Feed generation completed. Please check yagfi.com"
        );
        emailService.send(emailModel);

        long processTime = Duration.ofNanos(System.nanoTime() - start).toMinutes();
        log.info("Finished generating feed for nickname {} and took {} minutes "
                + "(but maybe not everything processed/uploaded yet)", nickname, processTime);
    }

    private UserDataGraphQlResponseDto getRepos(String login) {


        return githubClient.execute(login);
    }
}
