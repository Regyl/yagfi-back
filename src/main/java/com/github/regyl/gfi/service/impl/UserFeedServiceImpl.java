package com.github.regyl.gfi.service.impl;

import com.github.regyl.gfi.controller.dto.github.repos.UserDataGraphQlResponseDto;
import com.github.regyl.gfi.feign.CycloneDxFeignClient;
import com.github.regyl.gfi.service.UserFeedService;
import com.github.regyl.gfi.util.ResourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFeedServiceImpl implements UserFeedService {

    private static final String QUERY = ResourceUtil.getFilePayload("graphql/github-user-repos-request.graphql");

    private final GraphQlClient githubClient;
    private final CycloneDxFeignClient cycloneDxFeignClient;

    @Override
    public Object getFeedByNickname(String nickname) {
        UserDataGraphQlResponseDto responseDto = getRepos(nickname);
        List<String> userRepos = responseDto.getRepoUrls();
        List<Object> userRepoDependencies = userRepos.parallelStream()
                .map(dep -> {
                    try {
                        return cycloneDxFeignClient.getProjectSbom(dep);
                    } catch (Exception e) {
                        log.error("getRepos error", e);
                        return null;
                    }
                }).filter(Objects::nonNull)
                .toList();

        return userRepoDependencies;
    }

    private UserDataGraphQlResponseDto getRepos(String login) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("login", login);
        ClientGraphQlResponse clientGraphQlResponse = githubClient.document(QUERY)
                .variables(variables)
                .executeSync();
        if (!clientGraphQlResponse.isValid()) {
            log.error("graph ql response is invalid");
        }
        return clientGraphQlResponse.toEntity(UserDataGraphQlResponseDto.class);
    }
}
