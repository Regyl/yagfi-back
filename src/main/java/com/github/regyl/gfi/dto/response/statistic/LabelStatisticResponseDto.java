package com.github.regyl.gfi.dto.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelStatisticResponseDto {
    private String label;
    private Long count;         
}