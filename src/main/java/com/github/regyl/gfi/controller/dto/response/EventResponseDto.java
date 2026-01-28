package com.github.regyl.gfi.controller.dto.response;

import java.time.OffsetDateTime;

public record EventResponseDto(
        OffsetDateTime lastAcquired
) {}