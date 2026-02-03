package com.github.regyl.gfi.service.impl.event;

import com.github.regyl.gfi.controller.dto.response.EventResponseDto;
import com.github.regyl.gfi.entity.EventEntity;
import com.github.regyl.gfi.exception.EventNotFoundException;
import com.github.regyl.gfi.repository.EventRepository;
import com.github.regyl.gfi.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public void updateLastSyncTime(String source, OffsetDateTime syncTime) {
        Optional<EventEntity> existingEvent = Optional
                .ofNullable(eventRepository.findBySource(source).orElse(null));

        existingEvent.ifPresentOrElse(event -> {
                    event.setLastUpdateDttm(syncTime);
                    eventRepository.update(event);
                }, () -> {
                    EventEntity newEvent = EventEntity.builder()
                            .source(source)
                            .lastUpdateDttm(syncTime)
                            .created(OffsetDateTime.now())
                            .build();
                    eventRepository.insert(newEvent);
                }
        );
    }

    @Override
    public List<EventResponseDto> getAllSyncHistory() {
        return eventRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public EventResponseDto getSyncHistoryBySource(String source) {
        return eventRepository.findBySource(source)
                .map(this::toDto)
                .orElseThrow(() -> new EventNotFoundException("Event not found: " + source));
    }

    private EventResponseDto toDto(EventEntity entity) {
        return EventResponseDto.builder()
                .source(entity.getSource())
                .lastUpdateDttm(entity.getLastUpdateDttm())
                .build();
    }
}
