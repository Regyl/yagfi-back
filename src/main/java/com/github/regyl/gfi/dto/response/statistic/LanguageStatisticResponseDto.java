package com.github.regyl.gfi.dto.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageStatisticResponseDto {

    private String language;

    private Long quantity;
}
