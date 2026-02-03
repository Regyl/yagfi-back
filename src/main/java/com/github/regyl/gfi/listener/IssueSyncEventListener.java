package com.github.regyl.gfi.listener;

import com.github.regyl.gfi.listener.event.IssueSyncCompletedEvent;
import com.github.regyl.gfi.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IssueSyncEventListener {

    private final EventService eventService;

    @EventListener
    public void handleIssueSyncCompleted(IssueSyncCompletedEvent event) {
        eventService.updateLastSyncTime(event.getSource(), event.getSyncTime());
    }
}
