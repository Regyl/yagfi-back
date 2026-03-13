package com.github.regyl.gfi.service.impl.issueload;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class LoadLocker {

    private final AtomicBoolean metadataLoading = new AtomicBoolean(true);

    public void startLoading() {
        metadataLoading.set(true);
    }

    public void stopLoading() {
        metadataLoading.set(false);
    }

    public boolean getLoadingInfo() {
        return metadataLoading.get();
    }
}
