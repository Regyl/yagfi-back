package com.github.regyl.gfi.service.impl.source;

import com.github.regyl.gfi.model.IssueSources;
import com.github.regyl.gfi.model.IssueTables;
import com.github.regyl.gfi.model.event.IssueSyncCompletedEvent;
import com.github.regyl.gfi.service.source.IssueSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class GitlabIssueSourceServiceImpl implements IssueSourceService {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void upload(IssueTables table) {
        //https://docs.gitlab.com/api/graphql/
        //https://gitlab.com/gitlab-org/gitlab-development-kit/-/issues/1822
    }

    @Override
    public void raiseUploadEvent() {
        eventPublisher.publishEvent(new IssueSyncCompletedEvent(IssueSources.GITLAB, OffsetDateTime.now()));
        log.info("ActionLog.upload All gitlab issues synced successfully");
    }
}
