package com.github.regyl.gfi.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueRequestDto {

    private Integer page = 0;
    private Integer size = 10;

    private FilterRequestDto filter;

    private OrderDto order;
}
