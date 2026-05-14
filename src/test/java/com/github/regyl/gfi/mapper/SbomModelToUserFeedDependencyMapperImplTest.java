@Test
void shouldReturnNullWhenModelIsNull() {
    // Given

    // When
    UserFeedDependencyEntity result =
            sbomModelToUserMapper.apply(null, "https://xyz.com");

    // Then
    Assertions.assertNull(result);
}

@Test
void shouldReturnNullWhenDependencyUrlIsNull() {
    // Given
    UserFeedRequestEntity request = new UserFeedRequestEntity();
    request.setId(1L);

    SbomModel model =
            new SbomModel(request, null, "https://xyz.com");

    // When
    UserFeedDependencyEntity result =
            sbomModelToUserMapper.apply(model, null);

    // Then
    Assertions.assertNull(result);
}

@Test
void shouldMapAllFieldsForValidInput() {
    // Given
    OffsetDateTime now = OffsetDateTime.now();

    Mockito.when(dateTimeSupplier.get()).thenReturn(now);

    UserFeedRequestEntity request = new UserFeedRequestEntity();
    request.setId(1L);

    SbomModel model =
            new SbomModel(request, null, "https://github.com/example/repo");

    // When
    UserFeedDependencyEntity result =
            sbomModelToUserMapper.apply(model, "https://dependency.com");

    // Then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(1L, result.getRequestId());
    Assertions.assertEquals(
            "https://github.com/example/repo",
            result.getSourceRepo()
    );
    Assertions.assertEquals(
            "https://dependency.com",
            result.getDependencyUrl()
    );
    Assertions.assertEquals(now, result.getCreated());
}