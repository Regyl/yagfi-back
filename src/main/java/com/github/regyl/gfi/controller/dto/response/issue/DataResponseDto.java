package com.github.regyl.gfi.controller.dto.response.issue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataResponseDto {

    private Collection<IssueResponseDto> issues;
}
