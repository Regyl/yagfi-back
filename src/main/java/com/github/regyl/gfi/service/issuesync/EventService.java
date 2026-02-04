package com.github.regyl.gfi.service.issuesync;

import com.github.regyl.gfi.controller.dto.response.EventResponseDto;
import com.github.regyl.gfi.model.IssueSources;

import java.time.OffsetDateTime;
import java.util.List;

public interface EventService {

    void updateLastSyncTime(IssueSources source, OffsetDateTime syncTime);

    List<EventResponseDto> getAllSyncHistory();

    EventResponseDto getSyncHistoryBySource(String source);
}
