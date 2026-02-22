package com.github.regyl.gfi.dto.response.feed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceRepoStatisticResponseDto {

    private String sourceRepo;
    private String dependencyUrl;
    private Long issueCnt;
    private Long stars;
}
