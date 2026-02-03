package com.github.regyl.gfi.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelStatisticResponseDto {
    private String label;        // "good-first-iss"
    private Long count;          // 156
}