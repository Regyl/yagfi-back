package com.github.regyl.gfi.model.event;

import com.github.regyl.gfi.model.IssueSources;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;

@Data
@RequiredArgsConstructor
public class IssueSyncCompletedEvent {

    private final IssueSources source;
    private final OffsetDateTime syncTime;
}
