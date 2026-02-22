package com.github.regyl.gfi.service.feed;

import com.github.regyl.gfi.dto.request.feed.UserFeedRequestDto;
import com.github.regyl.gfi.dto.response.feed.SourceRepoStatisticResponseDto;
import com.github.regyl.gfi.dto.response.issue.IssueResponseDto;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;

import java.util.Collection;

/**
 * Service for managing user feed generation requests and retrieving feed data.
 * Handles the lifecycle of personalized issue feeds based on user's GitHub repositories.
 */
public interface UserFeedService {

    /**
     * Saves a new feed generation request. The request will be processed asynchronously
     * by the scheduled feed generator service.
     *
     * @param feedRequestDto user feed request data
     * @return saved entity with generated ID
     */
    UserFeedRequestEntity saveFeedRequest(UserFeedRequestDto feedRequestDto);

    /**
     * Returns statistics about source repositories for a given user's feed.
     * Statistics show how many dependencies were found for each source repository.
     *
     * @param nickname GitHub username
     * @return collection of source repository statistics, ordered by dependency count (desc)
     * @throws IllegalArgumentException if no feed request exists for the given nickname
     */
    Collection<SourceRepoStatisticResponseDto> getSourceRepoStatistics(String nickname);

    /**
     * Returns list of nicknames for users whose feeds have been successfully processed.
     * Used to display available feeds in the UI.
     *
     * @return collection of nicknames, ordered by feed creation date (asc)
     */
    Collection<String> getUsersProcessedFeeds();

    /**
     * Returns issues from repositories that are dependencies of the specified source repository.
     * Used when user selects a specific source repository to view related issues.
     *
     * @param sourceRepo GitHub repository URL
     * @return collection of issues from dependency repositories
     */
    Collection<IssueResponseDto> getIssuesBySourceRepo(String sourceRepo);

    /**
     * Returns all issues from dependency repositories for a user's feed.
     * Issues are aggregated across all dependencies found in the user's repositories.
     *
     * @param nickname GitHub username
     * @return collection of issues from all dependency repositories
     * @throws IllegalArgumentException if no feed request exists for the given nickname
     */
    Collection<IssueResponseDto> getIssuesByNickname(String nickname);
}
