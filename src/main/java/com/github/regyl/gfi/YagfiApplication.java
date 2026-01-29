package com.github.regyl.gfi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.github.regyl.gfi.configuration")
public class YagfiApplication {

   public   static void main(String[] args) {
        SpringApplication.run(YagfiApplication.class, args);
    }

}
