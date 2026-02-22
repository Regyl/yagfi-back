package com.github.regyl.gfi.service.impl.other;

import com.github.regyl.gfi.dto.response.statistic.LanguageStatisticResponseDto;
import com.github.regyl.gfi.repository.StatisticRepository;
import com.github.regyl.gfi.service.other.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository repository;

    @Override
    public Collection<LanguageStatisticResponseDto> getLanguageStatistics() {
        return repository.getLanguageStatistics();
    }
}
