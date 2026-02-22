package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.dto.response.feed.SourceRepoStatisticResponseDto;
import com.github.regyl.gfi.dto.response.issue.IssueResponseDto;
import com.github.regyl.gfi.entity.UserFeedDependencyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface UserFeedDependencyRepository {

    void saveAll(List<UserFeedDependencyEntity> entities);

    Collection<SourceRepoStatisticResponseDto> findSourceRepoStatisticsByRequestId(@Param("requestId") Long requestId);

    Collection<IssueResponseDto> findIssuesBySourceRepo(@Param("sourceRepo") String sourceRepo);

    Collection<IssueResponseDto> findIssuesByRequestId(@Param("requestId") Long requestId);
}
