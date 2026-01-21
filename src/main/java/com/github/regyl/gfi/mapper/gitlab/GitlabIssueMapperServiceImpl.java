package com.github.regyl.gfi.mapper.gitlab;

import com.github.regyl.gfi.controller.dto.gitlab.GitlabIssueDto;
import com.github.regyl.gfi.entity.IssueEntity;
import com.github.regyl.gfi.entity.RepositoryEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.BiFunction;

@Component
public class GitlabIssueMapperServiceImpl implements BiFunction<Map<String, RepositoryEntity>, GitlabIssueDto, IssueEntity> {

    @Override
    public IssueEntity apply(Map<String, RepositoryEntity> repos, GitlabIssueDto dto) {
        RepositoryEntity repository = repos.get(dto.getProject().getId());
        if (repository == null) {
            throw new IllegalStateException("Repository not found for issue: " + dto.getId());
        }
        
        return IssueEntity.builder()
                .sourceId(dto.getId())
                .title(dto.getTitle())
                .url(dto.getWebUrl())
                .updatedAt(dto.getUpdatedAt())
                .createdAt(dto.getCreatedAt())
                .repositoryId(repository.getId())
                .build();
    }
}
