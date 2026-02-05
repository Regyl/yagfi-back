package com.github.regyl.gfi.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@UtilityClass
@SuppressWarnings({"unchecked", "rawtypes"})
public class ServicePredicateUtil {

    public <T extends Predicate> T getTargetService(Collection<T> services, Object t) {
        List<T> targetServices = services.stream()
                .filter(service -> service.test(t))
                .toList();
        if (targetServices.isEmpty()) {
            log.error("No target service found for {}", t);
            throw new IllegalArgumentException();
        }

        if (targetServices.size() > 1) {
            log.error("Multiple target services found: {}", targetServices);
            throw new IllegalArgumentException();
        }

        return targetServices.getFirst();
    }

    public <T extends Predicate> T getTargetService(Collection<T> services, T defaultService, Object t) {
        List<T> targetServices = services.stream()
                .filter(service -> service.test(t))
                .toList();
        if (targetServices.size() > 1) {
            log.error("Multiple target services found: {}", targetServices);
            throw new IllegalArgumentException();
        }

        if (targetServices.size() == 1) {
            return targetServices.getFirst();
        }
        return defaultService;
    }

    public <T extends Predicate> Optional<T> getTargetServiceNullSafe(Collection<T> services, Object t) {
        List<T> targetServices = services.stream()
                .filter(service -> service.test(t))
                .toList();
        if (targetServices.isEmpty()) {
            log.error("No target service found for {}", t);
            return Optional.empty();
        }

        if (targetServices.size() > 1) {
            log.error("Multiple target services found: {}", targetServices);
            throw new IllegalArgumentException();
        }

        return Optional.of(targetServices.getFirst());
    }
}
