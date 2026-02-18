package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.annotation.DefaultIntegrationTest;
import com.github.regyl.gfi.controller.dto.request.issue.DataRequestDto;
import com.github.regyl.gfi.controller.dto.response.issue.IssueResponseDto;
import com.github.regyl.gfi.repository.DataRepository;
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
import java.util.List;

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

    @Nested
    class FindAllIssueLanguages {

        @Test
        void testOrderedByFrequency() {
            long repoId = insertRepositoryWithDetails("main-repo", "Java", "MIT", 100);

            insertIssue("issue1", "Issue 1", repoId, new String[]{}, "Java");
            insertIssue("issue2", "Issue 2", repoId, new String[]{}, "Python");
            insertIssue("issue3", "Issue 3", repoId, new String[]{}, "Java");
            insertIssue("issue4", "Issue 4", repoId, new String[]{}, "JavaScript");
            insertIssue("issue5", "Issue 5", repoId, new String[]{}, "Java");
            insertIssue("issue6", "Issue 6", repoId, new String[]{}, "Python");
            insertIssue("issue7", "Issue 7", repoId, new String[]{}, null);

            Collection<String> languages = dataRepository.findAllIssueLanguages();
            assertThat(languages).containsExactly("Java", "Python", "JavaScript");
        }

        @Test
        void testEmptyWhenNoData() {
            Collection<String> languages = dataRepository.findAllIssueLanguages();

            assertThat(languages).isEmpty();
        }
    }

    @Nested
    class FindAllIssues {

        @Test
        void testReturnsIssuesWithNoFilters() {
            long repoId1 = insertRepositoryWithDetails("repo1", "Java", "MIT", 100);
            long repoId2 = insertRepositoryWithDetails("repo2", "Python", "Apache-2.0", 200);
            
            insertIssue("issue1", "First Issue", repoId1, new String[]{"bug", "good-first-issue"}, "Java");
            insertIssue("issue2", "Second Issue", repoId2, new String[]{"enhancement"}, "Python");
            
            DataRequestDto request = new DataRequestDto();
            List<IssueResponseDto> issues = dataRepository.findAllIssues(request);
            
            assertThat(issues).hasSize(2);
        }

        @Test
        void testEmptyWhenNoData() {
            DataRequestDto request = new DataRequestDto();
            List<IssueResponseDto> issues = dataRepository.findAllIssues(request);
            
            assertThat(issues).isEmpty();
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

    private long insertRepositoryWithDetails(String sourceId, String language, String license, int stars) {
        jdbcTemplate.update(
                "INSERT INTO gfi.e_repository_1 "
                        + "(source_id, title, url, stars, description, language, license) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                sourceId, "title-" + sourceId,
                "https://github.com/" + sourceId, stars, "Description for " + sourceId, language, license
        );
        return jdbcTemplate.queryForObject(
                "SELECT id FROM gfi.e_repository_1 WHERE source_id = ?",
                Long.class, sourceId
        );
    }

    private void insertIssue(String sourceId, String title, long repositoryId, String[] labels, String language) {
        jdbcTemplate.update(
                "INSERT INTO gfi.e_issue_1 "
                        + "(source_id, title, url, updated_at, created_at, repository_id, labels, language) "
                        + "VALUES (?, ?, ?, NOW(), NOW(), ?, ?, ?)",
                sourceId, title,
                "https://github.com/test/" + sourceId, repositoryId, labels, language
        );
    }
}
