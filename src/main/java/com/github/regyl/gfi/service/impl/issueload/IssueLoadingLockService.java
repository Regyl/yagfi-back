package com.github.regyl.gfi.service.impl.issueload;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class IssueLoadingLockService {

    private final AtomicBoolean issueLoadInProgress = new AtomicBoolean(false);

    public boolean tryAcquireIssueLoadLock() {
        return issueLoadInProgress.compareAndSet(false, true);
    }

    public void releaseIssueLoadLock() {
        issueLoadInProgress.set(false);
    }

    public boolean isIssueLoadInProgress() {
        return issueLoadInProgress.get();
    }
}
