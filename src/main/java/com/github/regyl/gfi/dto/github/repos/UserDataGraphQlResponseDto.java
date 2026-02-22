package com.github.regyl.gfi.dto.github.repos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDataGraphQlResponseDto {

    private GithubUserDto user;

    public List<String> getRepoUrls() {
        if (user == null) {
            return Collections.emptyList();
        }

        List<String> urls = getUrls(user.getRepositories());
        List<String> starredUrls = getUrls(user.getStarredRepositories());
        urls.addAll(starredUrls);
        return urls;
    }

    private List<String> getUrls(RepositoriesConnectionDto dto) {
        if (dto == null) {
            return new ArrayList<>();
        }

        List<RepositoryNodeDto> nodes = dto.getNodes();
        if (CollectionUtils.isEmpty(nodes)) {
            return new ArrayList<>();
        }

        return nodes.stream()
                .map(RepositoryNodeDto::getUrl)
                .collect(Collectors.toList());
    }
}
