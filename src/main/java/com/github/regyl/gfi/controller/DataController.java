package com.github.regyl.gfi.controller;

import com.github.regyl.gfi.dto.request.issue.DataRequestDto;
import com.github.regyl.gfi.dto.response.issue.DataResponseDto;
import com.github.regyl.gfi.dto.response.statistic.LabelStatisticResponseDto;
import com.github.regyl.gfi.service.other.DataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/issues")
public class DataController {

    private final DataService dataService;

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

    @GetMapping("/licenses")
    public Collection<String> findAllLicenses() {
        return dataService.findAllLicenses();
    }

    @GetMapping("/labels")
    public List<LabelStatisticResponseDto> findAllLabels() {
        return dataService.findAllLabels();
    }

    @GetMapping("/issue-languages")
    public Collection<String> findAllIssueLanguages() {
        return dataService.findAllIssueLanguages();
    }

}
