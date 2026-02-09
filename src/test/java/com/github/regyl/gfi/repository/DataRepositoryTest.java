package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.annotation.DefaultIntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultIntegrationTest
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@Transactional
class DataRepositoryTest {

    @Container
    static PostgreSQLContainer pg = new PostgreSQLContainer("postgres:15.3");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", pg::getJdbcUrl);
        registry.add("spring.datasource.username", pg::getUsername);
        registry.add("spring.datasource.password", pg::getPassword);
    }

    @Autowired
    DataRepository dataRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Nested
    class FindAllLicenses {

        @Test
        void testOrderedByFrequency() {
            insertRepository("repo1", "Apache-2.0");
            insertRepository("repo2", "MIT");
            insertRepository("repo3", "GPL-3.0");
            insertRepository("repo4", "MIT");
            insertRepository("repo5", "MIT");
            insertRepository("repo6", "Apache-2.0");
            insertRepository("repo7", null);

            Collection<String> licenses = dataRepository.findAllLicenses();

            assertThat(licenses).containsExactly("MIT", "Apache-2.0", "GPL-3.0");
        }

        @Test
        void testEmptyWhenNoData() {
            Collection<String> licenses = dataRepository.findAllLicenses();

            assertThat(licenses).isEmpty();
        }
    }

    private void insertRepository(String sourceId, String license) {
        jdbcTemplate.update(
                "INSERT INTO gfi.e_repository_1 "
                        + "(source_id, title, url, stars, license) "
                        + "VALUES (?, ?, ?, ?, ?)",
                sourceId, "title-" + sourceId,
                "https://github.com/" + sourceId, 100, license
        );
    }
}
