package com.github.regyl.gfi.service.other;

import com.github.regyl.gfi.controller.dto.response.statistic.LanguageStatisticResponseDto;

import java.util.Collection;

public interface StatisticService {

    Collection<LanguageStatisticResponseDto> getLanguageStatistics();
}
