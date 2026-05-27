package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.entity.UserFeedDependencyEntity;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.model.SbomModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DefaultUnitTest
class SbomModelToUserFeedDependencyMapperImplTest {

    @InjectMocks
    private SbomModelToUserFeedDependencyMapperImpl mapper;

    @Mock
    private Supplier<OffsetDateTime> dateTimeSupplier;

    private static final String DEPENDENCY_URL = "https://xyz.com";
    private static final String REPOSITORY_URL = "https://repo.git";
    private static final Long REQUEST_ID = 1L;
    private static final OffsetDateTime FIXED_TIME = OffsetDateTime.parse("2024-01-01T10:00:00Z");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(dateTimeSupplier.get()).thenReturn(FIXED_TIME);
    }

    @Test
    @DisplayName("Returns null when model is null")
    void shouldReturnNullWhenModelIsNull() {
        assertNull(mapper.apply(null, DEPENDENCY_URL));
    }

    @Test
    @DisplayName("Returns null when dependencyUrl is null or empty")
    void shouldReturnNullWhenDependencyUrlIsNullOrEmpty() {
        SbomModel sbomModel = new SbomModel(mockUserFeedRequestEntity(), null, REPOSITORY_URL);
        assertNull(mapper.apply(sbomModel, null));
        assertNull(mapper.apply(sbomModel, ""));
    }

    @Test
    @DisplayName("Correctly maps SbomModel and dependencyUrl to UserFeedDependencyEntity")
    void shouldMapValidInputToEntity() {
        UserFeedRequestEntity userFeedRequestEntity = mockUserFeedRequestEntity();
        SbomModel sbomModel = new SbomModel(userFeedRequestEntity, null, REPOSITORY_URL);

        UserFeedDependencyEntity result = mapper.apply(sbomModel, DEPENDENCY_URL);

        assertNotNull(result);
        assertEquals(REQUEST_ID, result.getRequestId());
        assertEquals(REPOSITORY_URL, result.getSourceRepo());
        assertEquals(DEPENDENCY_URL, result.getDependencyUrl());
        assertEquals(FIXED_TIME, result.getCreated());
    }

    private UserFeedRequestEntity mockUserFeedRequestEntity() {
        UserFeedRequestEntity entity = new UserFeedRequestEntity();
        entity.setId(REQUEST_ID);
        return entity;
    }
}
