package com.github.regyl.gfi.mapper

import com.github.regyl.gfi.annotation.DefaultSpockUnitTest
import com.github.regyl.gfi.dto.response.EventResponseDto
import com.github.regyl.gfi.entity.EventEntity
import spock.lang.Specification

import java.time.OffsetDateTime

@DefaultSpockUnitTest
class EventEntityToResponseDtoMapperImplTest extends Specification {

    static EventEntityToResponseDtoMapperImpl mapper

    def setupSpec() {
        mapper = new EventEntityToResponseDtoMapperImpl()
    }

    def 'apply fullyFilledEntity returnsFullyFilledDto'() {
        given:
        OffsetDateTime now = OffsetDateTime.now()
        EventEntity entity = EventEntity.builder()
                .source("TEST_SOURCE")
                .lastUpdateDttm(now)
                .build()

        when:
        EventResponseDto result = mapper.apply(entity)

        then:
        result != null
        result.getSource() == entity.getSource()
        result.getLastUpdateDttm() == entity.getLastUpdateDttm()
    }

    def 'null input should be null output'() {
        when:
        EventResponseDto result = mapper.apply(null)

        then:
        result == null
    }
}
