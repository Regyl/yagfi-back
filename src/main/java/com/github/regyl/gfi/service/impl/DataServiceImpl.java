package com.github.regyl.gfi.service.impl;

import com.github.regyl.gfi.controller.dto.github.GithubIssueDto;
import com.github.regyl.gfi.controller.dto.github.GithubRepositoryDto;
import com.github.regyl.gfi.controller.dto.github.GithubSearchDto;
import com.github.regyl.gfi.controller.dto.github.IssueData;
import com.github.regyl.gfi.controller.dto.request.DataRequestDto;
import com.github.regyl.gfi.controller.dto.response.DataResponseDto;
import com.github.regyl.gfi.controller.dto.response.IssueResponseDto;
import com.github.regyl.gfi.entity.IssueEntity;
import com.github.regyl.gfi.entity.RepositoryEntity;
import com.github.regyl.gfi.exception.IssueNotFoundException;
import com.github.regyl.gfi.model.IssueTables;
import com.github.regyl.gfi.repository.DataRepository;
import com.github.regyl.gfi.repository.IssueRepository;
import com.github.regyl.gfi.repository.RepoRepository;
import com.github.regyl.gfi.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private final IssueRepository issueRepository;
    private final RepoRepository repoRepository;
    private final BiFunction<Map<String, RepositoryEntity>, GithubIssueDto, IssueEntity> issueMapper;
    private final Function<GithubRepositoryDto, RepositoryEntity> repoMapper;
    private final DataRepository dataRepository;

    @Async
    @Override
    public void save(IssueData response, IssueTables table) {
        if (response == null || response.getSearch() == null || CollectionUtils.isEmpty(response.getSearch().getNodes())) {
            return;
        }

        GithubSearchDto search = response.getSearch();
        Set<RepositoryEntity> repos = search.getNodes().stream()
                .map(GithubIssueDto::getRepository)
                .map(repoMapper)
                .collect(Collectors.toSet());
        repoRepository.saveAll(repos, table.getRepoTableName());

        //fetching because previous "insert on conflict do nothing" does not return IDs for already existing entities
        //FIXME but maybe change the FK from repository_id to repository_source_id to improve performance
        Map<String, RepositoryEntity> repoCollection = repoRepository.findAll(table.getRepoTableName()).stream()
                .collect(Collectors.toMap(RepositoryEntity::getSourceId, repo -> repo));

        List<IssueEntity> issues = search.getNodes().stream()
                .map(issue -> issueMapper.apply(repoCollection, issue))
                .toList();

        log.info("Issues loaded: {}", issues.size());
        issueRepository.saveAll(issues, table.getIssueTableName());
    }

    @Override
    public DataResponseDto findAllIssues(DataRequestDto requestDto) {
        Collection<IssueResponseDto> issues = dataRepository.findAllIssues(requestDto);
        return new DataResponseDto(issues);
    }

    @Override
    @Cacheable(cacheNames = "languages")
    public Collection<String> findAllLanguages() {
        return dataRepository.findAllLanguages();
    }

    @Override
    public String findRandomIssueUrl(DataRequestDto filters) {
        String link = issueRepository.findRandomIssueLink(filters);

        if (link == null) {
            // Χρησιμοποιούμε τη νέα μας κλάση εδώ
            throw new IssueNotFoundException("Random issue link not found!");
        }
        return link;
    }
}
