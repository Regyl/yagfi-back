package com.github.regyl.gfi.dto.request.issue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageFilterDto {

    private List<String> values;
    private FilterOperator operator;
}
