package com.github.regyl.gfi.service.event;

import com.github.regyl.gfi.controller.dto.response.EventResponseDto;

import java.time.OffsetDateTime;
import java.util.List;

public interface EventService {

    void updateLastSyncTime(String source, OffsetDateTime syncTime);

    List<EventResponseDto> getAllSyncHistory();

    EventResponseDto getSyncHistoryBySource(String source);
}
