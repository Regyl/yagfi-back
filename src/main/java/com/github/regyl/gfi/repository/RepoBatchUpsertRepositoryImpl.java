package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.entity.RepositoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class RepoBatchUpsertRepositoryImpl implements BatchUpsertRepository<RepositoryEntity> {

    private static final String SQL = "INSERT INTO gfi.e_repository (source_id, title, url, stars, description, language) VALUES (:sourceId, :title, :url, :stars, :description, :language) ON CONFLICT DO NOTHING";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(Collection<RepositoryEntity> entities) {
        // Convert the list of objects to an array of SqlParameterSource
        SqlParameterSource[] batchArgs = entities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(SQL, batchArgs);
    }
}
