package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.entity.UserFeedDependencyEntity;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.model.SbomModel;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SbomModelToUserFeedDependencyMapperImplTest {

    @Test
    void apply_shouldReturnNull() {
        Supplier<OffsetDateTime> dateTimeSupplier = mock(Supplier.class);
        SbomModelToUserFeedDependencyMapperImpl mapper =
                new SbomModelToUserFeedDependencyMapperImpl(dateTimeSupplier);

        UserFeedDependencyEntity resultado = mapper.apply(null, "https://dev/url");

        assertNull(resultado);
        verifyNoInteractions(dateTimeSupplier);
    }

    @Test
    void apply_shouldReturnAllFields() {

        OffsetDateTime fixedNow = OffsetDateTime.parse("2026-02-19T10:15:35-10:00");
        Supplier<OffsetDateTime> dateTimeSupplier = mock(Supplier.class);
        when(dateTimeSupplier.get()).thenReturn(fixedNow);

        SbomModelToUserFeedDependencyMapperImpl mapper =
                new SbomModelToUserFeedDependencyMapperImpl(dateTimeSupplier);

        SbomModel model = mock(SbomModel.class);
        UserFeedRequestEntity rq = mock(UserFeedRequestEntity.class);

        when(model.getRq()).thenReturn(rq);
        when(model.getRepositoryUrl()).thenReturn("https://github.com/org/repo");
        when(rq.getId()).thenReturn(123L); // ✅ FALTAVA ISSO

        String dependencyUrl = "pkg:maven/org.example/lib@1.0.0";

        UserFeedDependencyEntity resultado = mapper.apply(model, dependencyUrl);


        assertNotNull(resultado);
        assertEquals(123L, resultado.getRequestId());
        assertEquals("https://github.com/org/repo", resultado.getSourceRepo());
        assertEquals(dependencyUrl, resultado.getDependencyUrl());
        assertEquals(fixedNow, resultado.getCreated());

        verify(dateTimeSupplier, times(1)).get();
        verify(model, times(1)).getRq();
        verify(model, times(1)).getRepositoryUrl();
        verify(rq, times(1)).getId();


        verifyNoMoreInteractions(dateTimeSupplier, model, rq);
    }
}