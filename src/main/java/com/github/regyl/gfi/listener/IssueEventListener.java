package com.github.regyl.gfi.listener;

import com.github.regyl.gfi.entity.EventEntity;
import com.github.regyl.gfi.listener.event.IssueAcquiredEvent;
import com.github.regyl.gfi.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IssueEventListener {

    private final EventRepository eventRepository;

    @EventListener
    public void handleIssueAcquired(IssueAcquiredEvent req) {
        EventEntity event = EventEntity.builder()
                .issueId(req.getIssueId())
                .lastAcquired(req.getAcquiredAt())
                .build();
        eventRepository.insertEvent(event.getIssueId(), event.getLastAcquired());
    }
}
