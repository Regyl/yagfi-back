package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.controller.dto.response.EventResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.OffsetDateTime;

@Mapper
public interface EventRepository {

    EventResponseDto findLatestEvent();

    void insertEvent(@Param("issueId") Long issueId,
                     @Param("lastAcquired") OffsetDateTime lastAcquired);
}
