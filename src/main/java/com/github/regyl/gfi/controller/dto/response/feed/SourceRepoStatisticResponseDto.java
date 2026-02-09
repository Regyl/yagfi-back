package com.github.regyl.gfi.controller.dto.response.feed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceRepoStatisticResponseDto {

    private String sourceRepo;
    private Long count;
}
