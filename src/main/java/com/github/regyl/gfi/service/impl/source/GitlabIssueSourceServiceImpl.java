package com.github.regyl.gfi.service.impl.source;

import com.github.regyl.gfi.listener.event.IssueSyncCompletedEvent;
import com.github.regyl.gfi.model.IssueTables;
import com.github.regyl.gfi.service.source.IssueSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class GitlabIssueSourceServiceImpl implements IssueSourceService {

    private static final String GITLAB = "gitlab";

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void upload(IssueTables table) {
        //todo implement later
        //https://docs.gitlab.com/api/graphql/
        //https://gitlab.com/gitlab-org/gitlab-development-kit/-/issues/1822

        eventPublisher.publishEvent(new IssueSyncCompletedEvent(GITLAB, OffsetDateTime.now()));
    }
