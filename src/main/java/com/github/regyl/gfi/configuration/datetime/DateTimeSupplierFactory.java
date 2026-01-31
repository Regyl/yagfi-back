package com.github.regyl.gfi.configuration.datetime;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.function.Supplier;

@Component
public class DateTimeSupplierFactory {

    public static final ZoneOffset ZONE_OFFSET = ZoneOffset.UTC;
    public static final Clock ZONE_OFFSET_CLOCK = Clock.system(ZONE_OFFSET);

    @Bean
    public Supplier<OffsetDateTime> dateTimeSupplier() {
        return () -> OffsetDateTime.now(ZONE_OFFSET);
    }

    @Bean
    public Supplier<LocalDate> dateSupplier() {
        return () -> LocalDate.now(ZONE_OFFSET);
    }
}
