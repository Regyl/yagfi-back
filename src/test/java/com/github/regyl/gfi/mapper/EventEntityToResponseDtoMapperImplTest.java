package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.controller.dto.response.EventResponseDto;
import com.github.regyl.gfi.entity.EventEntity;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventEntityToResponseDtoMapperImplTest {

    private final EventEntityToResponseDtoMapperImpl mapper =
            new EventEntityToResponseDtoMapperImpl();

    @Test
    void apply_nullInput_returnsNull() {
        assertNull(mapper.apply(null));
    }

    @Test
    void apply_fullyFilledEntity_returnsFullyFilledDto() {
        OffsetDateTime now = OffsetDateTime.now();

        EventEntity entity = EventEntity.builder()
                .source("TEST_SOURCE")
                .lastUpdateDttm(now)
                .build();

        EventResponseDto result = mapper.apply(entity);

        assertNotNull(result);
        assertEquals(entity.getSource(), result.getSource());
        assertEquals(entity.getLastUpdateDttm(), result.getLastUpdateDttm());
    }
}