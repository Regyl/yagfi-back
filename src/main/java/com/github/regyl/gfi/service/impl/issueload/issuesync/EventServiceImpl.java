package com.github.regyl.gfi.service.impl.issueload.issuesync;

import com.github.regyl.gfi.dto.response.EventResponseDto;
import com.github.regyl.gfi.entity.EventEntity;
import com.github.regyl.gfi.exception.EventNotFoundException;
import com.github.regyl.gfi.model.IssueSources;
import com.github.regyl.gfi.repository.EventRepository;
import com.github.regyl.gfi.service.issueload.issuesync.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final Function<EventEntity, EventResponseDto> mapper;

    @Override
    public void updateLastSyncTime(IssueSources source, OffsetDateTime syncTime) {
        EventEntity newEvent = EventEntity.builder()
                .source(source.getName())
                .lastUpdateDttm(syncTime)
                .created(OffsetDateTime.now())
                .build();
        eventRepository.insert(newEvent);
    }

    @Override
    public List<EventResponseDto> getAllSyncHistory() {
        return eventRepository.findAll().stream()
                .map(mapper)
                .toList();
    }

    @Override
    public EventResponseDto getSyncHistoryBySource(String source) {
        return eventRepository.findBySource(source)
                .map(mapper)
                .orElseThrow(() -> new EventNotFoundException("Event not found: " + source));
    }
}
