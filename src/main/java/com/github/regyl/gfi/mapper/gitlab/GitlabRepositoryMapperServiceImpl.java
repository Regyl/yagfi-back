package com.github.regyl.gfi.mapper.gitlab;

import com.github.regyl.gfi.entity.RepositoryEntity;
import com.github.regyl.gfi.model.IssueSource;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class GitlabRepositoryMapperServiceImpl implements Function<GitlabProjectDto, RepositoryEntity> {

    @Override
    public RepositoryEntity apply(GitlabProjectDto dto) {

        
        return RepositoryEntity.builder()
                .sourceId(dto.getId())
                .title(dto.getFullPath())
                .url(dto.getWebUrl())
                .stars(dto.getStarCount() != null ? dto.getStarCount() : 0)
                .language(primaryLanguage)
                .description(dto.getDescription())
                .source(IssueSource.GITLAB.getValue())
                .build();
    }
}
