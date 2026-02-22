package com.github.regyl.gfi.service.impl.feed;

import com.github.regyl.gfi.dto.request.feed.UserFeedRequestDto;
import com.github.regyl.gfi.dto.response.feed.SourceRepoStatisticResponseDto;
import com.github.regyl.gfi.dto.response.issue.IssueResponseDto;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.model.UserFeedRequestStatuses;
import com.github.regyl.gfi.repository.UserFeedDependencyRepository;
import com.github.regyl.gfi.repository.UserFeedRequestRepository;
import com.github.regyl.gfi.service.feed.UserFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

/**
 * Implementation of user feed service.
 * Provides read-only access to feed data and handles feed request creation.
 * Feed generation itself is performed asynchronously by UserFeedGeneratorServiceImpl.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserFeedServiceImpl implements UserFeedService {

    private final UserFeedRequestRepository userFeedRequestRepository;
    private final Function<UserFeedRequestDto, UserFeedRequestEntity> feedRequestMapper;
    private final UserFeedDependencyRepository userFeedDependencyRepository;

    @Override
    public UserFeedRequestEntity saveFeedRequest(UserFeedRequestDto feedRequestDto) {
        UserFeedRequestEntity entity = feedRequestMapper.apply(feedRequestDto);
        userFeedRequestRepository.save(entity);
        return entity;
    }

    @Override
    public Collection<SourceRepoStatisticResponseDto> getSourceRepoStatistics(String nickname) {
        Optional<UserFeedRequestEntity> optionalRequest = userFeedRequestRepository.findByNickname(nickname);
        if (optionalRequest.isEmpty()) {
            throw new IllegalArgumentException("User feed request not found");
        }

        return userFeedDependencyRepository.findSourceRepoStatisticsByRequestId(optionalRequest.get().getId());
    }

    @Override
    public Collection<String> getUsersProcessedFeeds() {
        return userFeedRequestRepository.findNicknamesByStatus(UserFeedRequestStatuses.PROCESSED.getValue());
    }

    @Override
    public Collection<IssueResponseDto> getIssuesBySourceRepo(String sourceRepo) {
        return userFeedDependencyRepository.findIssuesBySourceRepo(sourceRepo);
    }

    @Override
    public Collection<IssueResponseDto> getIssuesByNickname(String nickname) {
        Optional<UserFeedRequestEntity> optionalRequest = userFeedRequestRepository.findByNickname(nickname);
        if (optionalRequest.isEmpty()) {
            throw new IllegalArgumentException("User feed request not found for nickname: " + nickname);
        }

        return userFeedDependencyRepository.findIssuesByRequestId(optionalRequest.get().getId());
    }
}
