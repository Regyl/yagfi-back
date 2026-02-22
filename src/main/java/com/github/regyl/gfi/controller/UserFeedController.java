package com.github.regyl.gfi.controller;

import com.github.regyl.gfi.dto.request.feed.UserFeedRequestDto;
import com.github.regyl.gfi.dto.response.feed.SourceRepoStatisticResponseDto;
import com.github.regyl.gfi.dto.response.issue.IssueResponseDto;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.service.feed.UserFeedService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed")
public class UserFeedController {

    private final UserFeedService userFeedService;

    @PostMapping("/generate")
    public UserFeedRequestEntity findCustomFeedByNickname(@RequestBody @Valid UserFeedRequestDto dto) {
        return userFeedService.saveFeedRequest(dto);
    }

    @GetMapping("/repositories")
    public Collection<SourceRepoStatisticResponseDto> getSourceRepoStatistics(
            @RequestParam("nickname") @NotEmpty String nickname) {
        return userFeedService.getSourceRepoStatistics(nickname);
    }

    @GetMapping("/users")
    public Collection<String> getUsersProcessedFeeds() {
        return userFeedService.getUsersProcessedFeeds();
    }

    @GetMapping("/issues")
    public Collection<IssueResponseDto> getIssuesBySourceRepo(
            @RequestParam("sourceRepo") @NotEmpty String sourceRepo) {
        return userFeedService.getIssuesBySourceRepo(sourceRepo);
    }

    @GetMapping("/feed-issues")
    public Collection<IssueResponseDto> getIssuesByNickname(
            @RequestParam("nickname") @NotEmpty String nickname) {
        return userFeedService.getIssuesByNickname(nickname);
    }
}
