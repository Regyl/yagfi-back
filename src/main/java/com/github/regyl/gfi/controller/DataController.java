package com.github.regyl.gfi.controller;

import com.github.regyl.gfi.controller.dto.request.DataRequestDto;
import com.github.regyl.gfi.controller.dto.response.DataResponseDto;
import com.github.regyl.gfi.service.DataService;
import com.github.regyl.gfi.service.UserFeedService;
import jakarta.validation.Valid;
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
@RequestMapping("/issues")
public class DataController {

    private final DataService dataService;
    private final UserFeedService userFeedService;

    @PostMapping
    public DataResponseDto findAll(@RequestBody @Valid DataRequestDto requestDto) {
        return dataService.findAllIssues(requestDto);
    }

    @GetMapping("/languages")
    public Collection<String> findAllLanguages() {
        return dataService.findAllLanguages();
    }

    @GetMapping("/feed")
    public Object findCustomFeedByNickname(@RequestParam("nickname") String nickname) {
        return userFeedService.getFeedByNickname(nickname);
    }
}
