package com.github.regyl.gfi.service.impl.issueload.issuesync;

import com.github.regyl.gfi.model.event.IssueSyncCompletedModel;
import com.github.regyl.gfi.service.issueload.issuesync.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IssueSyncEventListener {

    private final EventService eventService;

    @EventListener
    public void handleIssueSyncCompleted(IssueSyncCompletedModel event) {
        eventService.updateLastSyncTime(event.getSource(), event.getSyncTime());
    }
}
