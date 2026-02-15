package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.annotation.DefaultIntegrationTest;
import com.github.regyl.gfi.controller.dto.response.statistic.LabelStatisticResponseDto;
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

import java.sql.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            insertRepository(1L, "repo1", "Apache-2.0");
            insertRepository(2L, "repo2", "MIT");
            insertRepository(3L, "repo3", "GPL-3.0");
            insertRepository(4L, "repo4", "MIT");
            insertRepository(5L, "repo5", "MIT");
            insertRepository(6L, "repo6", "Apache-2.0");
            insertRepository(7L, "repo7", null);

            Collection<String> licenses = dataRepository.findAllLicenses();

            assertThat(licenses).containsExactly("MIT", "Apache-2.0", "GPL-3.0");
        }

        @Test
        void testEmptyWhenNoData() {
            Collection<String> licenses = dataRepository.findAllLicenses();

            assertThat(licenses).isEmpty();
        }
    }

    @Nested
    class FindAllLabelsTest {

        @Test
        void testFindAllLabelsWhenNoData() {
            List<LabelStatisticResponseDto> labels = dataRepository.findAllLabels();

            assertThat(labels).isEmpty();
        }

        @Test
        void testFindAllLabels() {
            insertRepository(1L, "repo1", "Apache-2.0");
            insertRepository(2L, "repo2", "MIT");

            insertIssue("sourceId1", 1L, List.of("Label1", "Label2"));
            insertIssue("sourceId2", 1L, List.of("Label1"));
            insertIssue("sourceId3", 1L, List.of("Label1"));
            insertIssue("sourceId4", 2L, List.of("Label1"));
            insertIssue("sourceId5", 2L, List.of("Label2"));

            List<LabelStatisticResponseDto> labels = dataRepository.findAllLabels();

            Map<String, Long> result =
                    labels.stream()
                            .collect(Collectors.toMap(
                                    LabelStatisticResponseDto::getLabel,
                                    LabelStatisticResponseDto::getCount
                            ));


            assertThat(result)
                    .hasSize(2)
                    .containsEntry("Label1", 4L)
                    .containsEntry("Label2", 2L);
        }
    }

    private void insertRepository(Long id, String sourceId, String license) {
        jdbcTemplate.update(
                "INSERT INTO gfi.e_repository_1 "
                        + "(id, source_id, title, url, stars, license) "
                        + "VALUES (?, ?, ?, ?, ?, ?)",
                id, sourceId, "title-" + sourceId,
                "https://github.com/" + sourceId, 100, license
        );
    }

    private void insertIssue(
            String sourceId,
            Long repositoryId,
            List<String> labels
    ) {
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(
                    "INSERT INTO gfi.e_issue_1 "
                            + "(source_id, title, url, updated_at, created_at, repository_id, labels, language) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );

            ps.setString(1, sourceId);
            ps.setString(2, "title-" + sourceId);
            ps.setString(3, "https://github.com/" + sourceId);
            ps.setObject(4, java.time.OffsetDateTime.now());
            ps.setObject(5, java.time.OffsetDateTime.now());
            ps.setLong(6, repositoryId);

            if (labels != null && !labels.isEmpty()) {
                Array sqlArray = connection.createArrayOf("VARCHAR", labels.toArray(new String[0]));
                ps.setArray(7, sqlArray);
            } else {
                ps.setArray(7, null);
            }

            ps.setString(8, "EN");

            return ps;
        });
    }
}
