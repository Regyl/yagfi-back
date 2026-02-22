package com.github.regyl.gfi.service.impl.other;

import com.github.regyl.gfi.dto.github.issue.GithubIssueDto;
import com.github.regyl.gfi.dto.github.issue.GithubRepositoryDto;
import com.github.regyl.gfi.dto.github.issue.GithubSearchDto;
import com.github.regyl.gfi.dto.github.issue.IssueDataDto;
import com.github.regyl.gfi.dto.request.issue.DataRequestDto;
import com.github.regyl.gfi.dto.response.issue.DataResponseDto;
import com.github.regyl.gfi.dto.response.issue.IssueResponseDto;
import com.github.regyl.gfi.dto.response.statistic.LabelStatisticResponseDto;
import com.github.regyl.gfi.entity.IssueEntity;
import com.github.regyl.gfi.entity.RepositoryEntity;
import com.github.regyl.gfi.model.IssueTables;
import com.github.regyl.gfi.repository.DataRepository;
import com.github.regyl.gfi.repository.IssueRepository;
import com.github.regyl.gfi.repository.RepoRepository;
import com.github.regyl.gfi.service.other.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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

    @Override
    public void save(IssueDataDto response, IssueTables table) {
        if (response == null || response.getSearch() == null
                || CollectionUtils.isEmpty(response.getSearch().getNodes())) {
            return;
        }

        GithubSearchDto search = response.getSearch();
        Set<RepositoryEntity> repos = search.getNodes().stream()
                .map(GithubIssueDto::getRepository)
                .map(repoMapper)
                .collect(Collectors.toSet());
        repoRepository.saveAll(repos, table.getRepoTableName());

        //fetching because previous "insert on conflict do nothing" does not return IDs for already existing entities
        //but maybe change the FK from repository_id to repository_source_id to improve performance
        Map<String, RepositoryEntity> repoCollection = repoRepository.findAll(table.getRepoTableName()).stream()
                .collect(Collectors.toMap(RepositoryEntity::getSourceId, repo -> repo));

        List<IssueEntity> issues = search.getNodes().stream()
                .map(issue -> issueMapper.apply(repoCollection, issue))
                .toList();

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
    @Cacheable(cacheNames = "licenses")
    public Collection<String> findAllLicenses() {
        return dataRepository.findAllLicenses();
    }

    @Override
    public String findRandomIssueUrl(DataRequestDto filters) {
        return dataRepository.findRandomIssueLink(filters);
    }
  
    @Override
    @Cacheable(cacheNames = "labels")  
    public List<LabelStatisticResponseDto> findAllLabels() {
        return dataRepository.findAllLabels();
    }

    @Override
    @Cacheable(cacheNames = "issue-languages")
    public Collection<String> findAllIssueLanguages() {
        return dataRepository.findAllIssueLanguages();
    }

}
