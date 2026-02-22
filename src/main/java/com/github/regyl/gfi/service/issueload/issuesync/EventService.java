package com.github.regyl.gfi.service.issueload.issuesync;

import com.github.regyl.gfi.dto.response.EventResponseDto;
import com.github.regyl.gfi.model.IssueSources;

import java.time.OffsetDateTime;
import java.util.List;

public interface EventService {

    void updateLastSyncTime(IssueSources source, OffsetDateTime syncTime);

    List<EventResponseDto> getAllSyncHistory();

    EventResponseDto getSyncHistoryBySource(String source);
}
