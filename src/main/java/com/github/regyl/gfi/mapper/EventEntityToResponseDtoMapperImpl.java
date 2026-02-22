package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.dto.response.EventResponseDto;
import com.github.regyl.gfi.entity.EventEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class EventEntityToResponseDtoMapperImpl implements Function<EventEntity, EventResponseDto> {

    @Override
    public EventResponseDto apply(EventEntity entity) {
        if (entity == null) {
            return null;
        }
        return EventResponseDto.builder()
                .source(entity.getSource())
                .lastUpdateDttm(entity.getLastUpdateDttm())
                .build();
    }
}
