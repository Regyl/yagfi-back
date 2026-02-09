package com.github.regyl.gfi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LogEntity extends AbstractEntity {

    private String url;
    private String httpMethod;
    private String requestBody;
    private String country;
    private String os;
    private String browserFamily;
    private String deviceType;
    private String utmSource;
}
