package com.github.regyl.gfi.configuration.objectmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ObjectMapperFactory {

    @Bean
    @Primary
    public ObjectMapper defaultObjectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }
}
