package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.entity.UserFeedDependencyEntity;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.model.SbomModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

@DefaultUnitTest
public class SbomModelToUserFeedDependencyMapperImplTest {

    @InjectMocks
    private SbomModelToUserFeedDependencyMapperImpl sbomModelToUserMapper;

    @Mock
    private Supplier<OffsetDateTime> dateTimeSupplier;

    @Test
    public void testForModelAsNullInput() {
        Assertions.assertNull(sbomModelToUserMapper.apply(null, "https://xyz.com"));
    }

    @Test
    public void testForDependencyUrlAsNullInput() {
        UserFeedRequestEntity userFeedRequestEntity = new UserFeedRequestEntity();
        userFeedRequestEntity.setId(1L);
        SbomModel sbomModel = new SbomModel(userFeedRequestEntity, null, "https://xyz.com");

        Assertions.assertNull(sbomModelToUserMapper.apply(sbomModel, null));
    }

    @Test
    public void testForValidInput() {
        UserFeedRequestEntity userFeedRequestEntity = new UserFeedRequestEntity();
        userFeedRequestEntity.setId(1L);
        SbomModel sbomModel = new SbomModel(userFeedRequestEntity, null, "https://xyz.com");

        Mockito.when(dateTimeSupplier.get()).thenReturn(OffsetDateTime.now());
        UserFeedDependencyEntity userFeedDependencyEntity = sbomModelToUserMapper.apply(sbomModel, "https://xyz.com");

        Assertions.assertNotNull(userFeedDependencyEntity);
        Assertions.assertEquals("https://xyz.com", userFeedDependencyEntity.getDependencyUrl());
    }
}
