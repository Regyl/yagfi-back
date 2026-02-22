package com.github.regyl.gfi.service.impl.issueload.issuesource.github.runnable;

import com.github.regyl.gfi.dto.github.issue.IssueDataDto;
import com.github.regyl.gfi.dto.request.issue.IssueRequestDto;
import com.github.regyl.gfi.exception.RateLimitExceedException;
import com.github.regyl.gfi.model.IssueTables;
import com.github.regyl.gfi.service.github.GithubClientService;
import com.github.regyl.gfi.service.other.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

@Slf4j
@RequiredArgsConstructor
public class GithubIssueRunnableImpl implements Runnable {

    private static final int CHECKS_BEFORE_EXITING = 3;

    //task params
    private final IssueTables table;
    private final BlockingQueue<IssueRequestDto> queue;

    //services
    private final DataService dataService;
    private final GithubClientService<IssueRequestDto, IssueDataDto> githubClient;

    @Override
    public void run() {
        AtomicInteger counter = new AtomicInteger(0);
        while (true) {
            IssueRequestDto task = queue.poll();

            if (task == null) {
                if (counter.incrementAndGet() == CHECKS_BEFORE_EXITING) {
                    log.info("Task queue is empty, exiting");
                    break;
                }

                LockSupport.parkNanos(Duration.ofSeconds(10).toNanos());
                continue;
            } else {
                counter.set(0);
            }

            try {
                IssueDataDto response = githubClient.execute(task);
                dataService.save(response, table);

                if (response.hasNextPage()) {
                    IssueRequestDto nextTask = new IssueRequestDto(task.getQuery(), response.getEndCursor());
                    queue.offer(nextTask);
                }
            } catch (RateLimitExceedException e) {
                log.error("Exceeded a secondary rate limit, return task to queue");
                queue.offer(task);
                LockSupport.parkNanos(Duration.ofSeconds(60).toNanos());
            } catch (Exception e) {
                log.error("Error processing task for query: {}", task.getQuery(), e);
            }
        }
    }
}


