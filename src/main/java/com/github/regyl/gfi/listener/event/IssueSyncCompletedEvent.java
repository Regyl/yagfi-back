package com.github.regyl.gfi.listener.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class IssueSyncCompletedEvent {

    private final String source;
    private final OffsetDateTime syncTime;
}
