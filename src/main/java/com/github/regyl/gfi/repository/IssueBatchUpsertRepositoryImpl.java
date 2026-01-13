package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.entity.IssueEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class IssueBatchUpsertRepositoryImpl implements BatchUpsertRepository<IssueEntity> {

    private static final String SQL = "INSERT INTO gfi.e_issue (source_id, title, url, updated_at, created_at, repository_id) VALUES (:sourceId, :title, :url, :updatedAt, :createdAt, :repositoryId) ON CONFLICT DO NOTHING";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(Collection<IssueEntity> entities) {
        // Convert the list of objects to an array of SqlParameterSource
        SqlParameterSource[] batchArgs = entities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(SQL, batchArgs);
    }
}
