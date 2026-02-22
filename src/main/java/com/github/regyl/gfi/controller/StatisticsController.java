package com.github.regyl.gfi.controller;

import com.github.regyl.gfi.dto.response.statistic.LanguageStatisticResponseDto;
import com.github.regyl.gfi.service.other.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticService service;

    @GetMapping("/languages")
    public Collection<LanguageStatisticResponseDto> findAllLanguages() {
        return service.getLanguageStatistics();
    }

}
