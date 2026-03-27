package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.entity.UserFeedDependencyEntity;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.model.SbomModel;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SbomModelToUserFeedDependencyMapperImplTest {

    // ✅ Test 1: Null input
    @Test
    void shouldThrowExceptionWhenModelIsNull() {
        // Given
        SbomModelToUserFeedDependencyMapperImpl mapper =
                new SbomModelToUserFeedDependencyMapperImpl(() -> OffsetDateTime.now());

        // When & Then
        assertThrows(NullPointerException.class, () ->
                mapper.apply(null, "dep-url")
        );
    }

    // ✅ Test 2: Fully-filled input
    @Test
    void shouldMapAllFieldsCorrectly() {
        // Given
        UserFeedRequestEntity rq = new UserFeedRequestEntity();
        rq.setId(100L);

        SbomModel model = new SbomModel(rq, null, "https://repo.com");

        OffsetDateTime fixedTime = OffsetDateTime.now();
        Supplier<OffsetDateTime> supplier = () -> fixedTime;

        SbomModelToUserFeedDependencyMapperImpl mapper =
                new SbomModelToUserFeedDependencyMapperImpl(supplier);

        // When
        UserFeedDependencyEntity result =
                mapper.apply(model, "https://dependency.com");

        // Then
        assertNotNull(result);
        assertEquals(100L, result.getRequestId());
        assertEquals("https://repo.com", result.getSourceRepo());
        assertEquals("https://dependency.com", result.getDependencyUrl());
        assertEquals(fixedTime, result.getCreated());
    }
}