package com.github.regyl.gfi.service.impl;

import com.github.regyl.gfi.controller.dto.github.GithubIssueDto;
import com.github.regyl.gfi.controller.dto.github.GithubRepositoryDto;
import com.github.regyl.gfi.controller.dto.github.GithubSearchDto;
import com.github.regyl.gfi.controller.dto.github.IssueData;
import com.github.regyl.gfi.controller.dto.request.IssueRequestDto;
import com.github.regyl.gfi.entity.IssueEntity;
import com.github.regyl.gfi.entity.RepositoryEntity;
import com.github.regyl.gfi.repository.BatchUpsertRepository;
import com.github.regyl.gfi.repository.IssueJpaRepository;
import com.github.regyl.gfi.repository.RepoJpaRepository;
import com.github.regyl.gfi.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

    private final BatchUpsertRepository<IssueEntity> issueRepository;
    private final BatchUpsertRepository<RepositoryEntity> repoRepository;
    private final RepoJpaRepository jpaRepoRepository;
    private final IssueJpaRepository issueJpaRepository;
    private final BiFunction<Map<String, RepositoryEntity>, GithubIssueDto, IssueEntity> issueMapper;
    private final Function<GithubRepositoryDto, RepositoryEntity> repoMapper;

    @Async
    @Override
    public void save(IssueData response) {
        if (response == null || response.getSearch() == null || CollectionUtils.isEmpty(response.getSearch().getNodes())) {
            return;
        }

        GithubSearchDto search = response.getSearch();
        Set<RepositoryEntity> repos = search.getNodes().stream()
                .map(GithubIssueDto::getRepository)
                .map(repoMapper)
                .collect(Collectors.toSet());
        repoRepository.saveAll(repos);
        Map<String, RepositoryEntity> repoCollection = jpaRepoRepository.findAll().stream() //FIXME switch to returning ids in previous step
                .collect(Collectors.toMap(RepositoryEntity::getSourceId, repo -> repo));

        List<IssueEntity> issues = search.getNodes().stream()
                .map(issue -> issueMapper.apply(repoCollection, issue))
                .toList();

        log.info("Issues found: {}", issues.size());
        issueRepository.saveAll(issues);
    }

    @Override
    public Page<IssueEntity> findAllIssues(IssueRequestDto requestDto) {
        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());
        return issueJpaRepository.findAll(pageable);
    }
}
