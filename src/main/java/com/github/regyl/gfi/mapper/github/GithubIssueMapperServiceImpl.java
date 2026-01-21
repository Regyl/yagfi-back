package com.github.regyl.gfi.mapper.github;

import com.github.regyl.gfi.controller.dto.github.GithubIssueDto;
import com.github.regyl.gfi.entity.IssueEntity;
import com.github.regyl.gfi.entity.RepositoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class GithubIssueMapperServiceImpl implements BiFunction<Map<String, RepositoryEntity>, GithubIssueDto, IssueEntity> {

    @Override
    public IssueEntity apply(Map<String, RepositoryEntity> repos, GithubIssueDto dto) {
        return IssueEntity.builder()
                .sourceId(dto.getId())
                .title(dto.getTitle())
                .url(dto.getUrl())
                .updatedAt(dto.getUpdatedAt())
                .createdAt(dto.getCreatedAt())
                .repositoryId(repos.get(dto.getRepository().getId()).getId())
                .build();
    }
}
