package com.github.regyl.gfi.configuration.feign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.github.regyl.gfi.feign")
public class FeignConfiguration {
}
