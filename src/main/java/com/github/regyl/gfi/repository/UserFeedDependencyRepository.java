package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.controller.dto.response.feed.SourceRepoStatisticResponseDto;
import com.github.regyl.gfi.entity.UserFeedDependencyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface UserFeedDependencyRepository {

    void saveAll(List<UserFeedDependencyEntity> entities);

    Collection<SourceRepoStatisticResponseDto> findSourceRepoStatisticsByRequestId(@Param("requestId") Long requestId);
}
