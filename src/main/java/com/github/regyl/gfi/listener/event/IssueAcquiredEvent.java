package com.github.regyl.gfi.listener.event;

import org.springframework.context.ApplicationEvent;

import java.time.OffsetDateTime;

public class IssueAcquiredEvent extends ApplicationEvent {

    private final Long issueId;
    private final OffsetDateTime acquiredAt;

    public IssueAcquiredEvent(Object source, Long issueId, OffsetDateTime acquiredAt) {
        super(source);
        this.issueId = issueId;
        this.acquiredAt = acquiredAt;
    }

    public Long getIssueId() {
        return issueId;
    }

    public OffsetDateTime getAcquiredAt() {
        return acquiredAt;
    }

}
