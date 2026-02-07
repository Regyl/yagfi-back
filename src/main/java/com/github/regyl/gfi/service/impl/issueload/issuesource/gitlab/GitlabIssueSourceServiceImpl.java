package com.github.regyl.gfi.service.impl.issueload.issuesource.gitlab;

import com.github.regyl.gfi.model.IssueSources;
import com.github.regyl.gfi.model.IssueTables;
import com.github.regyl.gfi.model.event.IssueSyncCompletedEvent;
import com.github.regyl.gfi.service.issueload.IssueSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class GitlabIssueSourceServiceImpl implements IssueSourceService {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Collection<CompletableFuture<Void>> upload(IssueTables table) {
        //https://docs.gitlab.com/api/graphql/
        //https://gitlab.com/gitlab-org/gitlab-development-kit/-/issues/1822
        return Collections.emptyList();
    }

    @Override
    public void raiseUploadEvent() {
        eventPublisher.publishEvent(new IssueSyncCompletedEvent(IssueSources.GITLAB, OffsetDateTime.now()));
        log.info("All gitlab issues synced successfully");
    }
}
