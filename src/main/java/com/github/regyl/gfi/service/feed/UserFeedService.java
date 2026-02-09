package com.github.regyl.gfi.service.feed;

import com.github.regyl.gfi.controller.dto.request.feed.UserFeedRequestDto;
import com.github.regyl.gfi.controller.dto.response.feed.SourceRepoStatisticResponseDto;
import com.github.regyl.gfi.controller.dto.response.issue.IssueResponseDto;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;

import java.util.Collection;

public interface UserFeedService {

    UserFeedRequestEntity saveFeedRequest(UserFeedRequestDto feedRequestDto);

    Collection<SourceRepoStatisticResponseDto> getSourceRepoStatistics(String nickname);

    Collection<String> getUsersProcessedFeeds();

    Collection<IssueResponseDto> getIssuesBySourceRepo(String sourceRepo);

    Collection<IssueResponseDto> getIssuesByNickname(String nickname);
}
