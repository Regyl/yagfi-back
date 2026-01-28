package com.github.regyl.gfi.controller;

import com.github.regyl.gfi.controller.dto.request.DataRequestDto;
import com.github.regyl.gfi.controller.dto.response.DataResponseDto;
import com.github.regyl.gfi.service.DataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/issues")
public class DataController {

    private final DataService dataService;

    @GetMapping("/random")
    public String findRandom(@RequestParam Map<String, String> filters)
    {
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
}
