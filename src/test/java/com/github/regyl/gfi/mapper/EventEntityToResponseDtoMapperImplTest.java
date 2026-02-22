package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.dto.response.EventResponseDto;
import com.github.regyl.gfi.entity.EventEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultUnitTest
@ExtendWith(MockitoExtension.class)
class EventEntityToResponseDtoMapperImplTest {
    @InjectMocks
    private EventEntityToResponseDtoMapperImpl mapper;

    @Test
    void apply_nullInput_returnsNull() {
        assertThat(mapper.apply(null)).isNull();
    }

    @Test
    void apply_fullyFilledEntity_returnsFullyFilledDto() {
        OffsetDateTime now = OffsetDateTime.now();

        EventEntity entity = EventEntity.builder()
                .source("TEST_SOURCE")
                .lastUpdateDttm(now)
                .build();

        EventResponseDto result = mapper.apply(entity);

        assertThat(result).isNotNull();
        assertThat(result.getSource()).isEqualTo(entity.getSource());
        assertThat(result.getLastUpdateDttm()).isEqualTo(entity.getLastUpdateDttm());
    }
}