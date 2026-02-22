package com.github.regyl.gfi.service.impl.issueload.issuesource.github;

import com.github.regyl.gfi.dto.request.issue.IssueRequestDto;
import com.github.regyl.gfi.model.IssueSources;
import com.github.regyl.gfi.model.IssueTables;
import com.github.regyl.gfi.model.event.IssueSyncCompletedEvent;
import com.github.regyl.gfi.service.issueload.IssueSourceService;
import com.github.regyl.gfi.service.issueload.issuesource.github.GithubQueryBuilderService;
import com.github.regyl.gfi.service.issueload.issuesource.github.runnable.RunnableManagerService;
import com.github.regyl.gfi.service.other.LabelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubIssueSourceServiceImpl implements IssueSourceService {

    private final LabelService labelService;
    private final RunnableManagerService runnableManagerService;
    private final ApplicationEventPublisher eventPublisher;
    private final GithubQueryBuilderService queryBuilderService;

    @Override
    public void raiseUploadEvent() {
        eventPublisher.publishEvent(new IssueSyncCompletedEvent(IssueSources.GITHUB, OffsetDateTime.now()));
        log.info("All github issues synced successfully");
    }

    @Override
    public Collection<CompletableFuture<Void>> upload(IssueTables table) {
        BlockingQueue<IssueRequestDto> queries = labelService.findAll().stream()
                .flatMap(label -> queryBuilderService.apply(label).stream())
                .map(query -> new IssueRequestDto(query, null))
                .collect(Collectors.toCollection(LinkedBlockingQueue::new));
        log.info("Created {} different queries", queries.size());

        return runnableManagerService.apply(table, queries);
    }
}
