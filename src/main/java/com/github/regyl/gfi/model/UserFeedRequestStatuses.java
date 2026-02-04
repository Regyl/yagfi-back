package com.github.regyl.gfi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserFeedRequestStatuses {

    WAITING_FOR_PROCESS("waiting-for-process"),
    PROCESSING("processing"),
    PROCESSED("processed"),

    ;

    private final String value;
}
