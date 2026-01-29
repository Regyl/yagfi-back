package com.github.regyl.gfi.controller.dto.github.repos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

        //FIXME check npe/empty collection
        List<String> urls = user.getRepositories().getNodes().stream()
                .map(RepositoryNodeDto::getUrl)
                .collect(Collectors.toList());

        //FIXME check npe/empty collection
        List<String> starredUrls = user.getStarredRepositories().getNodes().stream()
                .map(RepositoryNodeDto::getUrl)
                .toList();
        urls.addAll(starredUrls);
        return urls;
    }
}
