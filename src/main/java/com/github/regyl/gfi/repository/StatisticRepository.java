package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.dto.response.statistic.LanguageStatisticResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

@Mapper
public interface StatisticRepository {

    Collection<LanguageStatisticResponseDto> getLanguageStatistics();
}
