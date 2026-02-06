package com.github.regyl.gfi.controller;

import com.github.regyl.gfi.controller.dto.request.DataRequestDto;
import com.github.regyl.gfi.controller.dto.request.UserFeedRequestDto;
import com.github.regyl.gfi.controller.dto.response.DataResponseDto;
import com.github.regyl.gfi.controller.dto.response.LabelStatisticResponseDto;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.service.feed.UserFeedService;
import com.github.regyl.gfi.service.other.DataService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/issues")
public class DataController {

    private final DataService dataService;
    private final UserFeedService userFeedService;

    @PostMapping("/random")
    public String findRandom(@RequestBody @Valid DataRequestDto filters) {
        return dataService.findRandomIssueUrl(filters);
    }

    @PostMapping
    public DataResponseDto findAll(@RequestBody @Valid DataRequestDto requestDto) {
        return dataService.findAllIssues(requestDto);
    }

    @GetMapping("/languages")
    public Collection<String> findAllLanguages() {
        return dataService.findAllLanguages();
    }

    @GetMapping("/labels")
    public List<LabelStatisticResponseDto> findAllLabels() {
        return dataService.findAllLabels();
    }
    
    @GetMapping("/feed")
    public UserFeedRequestEntity findCustomFeedByNickname(
            @RequestParam("nickname") @NotEmpty String nickname,
            @RequestParam("email") @Email String email
    ) {
        return userFeedService.saveFeedRequest(new UserFeedRequestDto(nickname, email));
    }
}
