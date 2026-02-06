package com.github.regyl.gfi.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenseFilterDto {

    private List<String> values;
    private FilterOperator operator;
}

