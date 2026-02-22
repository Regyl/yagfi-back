package com.github.regyl.gfi.service.impl.github;

import com.github.regyl.gfi.dto.github.repos.UserDataGraphQlResponseDto;
import com.github.regyl.gfi.util.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class GithubUserRepositoriesClientServiceImpl
        extends AbstractGraphQlGithubClientService<String, UserDataGraphQlResponseDto> {

    private static final String QUERY = ResourceUtil.getFilePayload("graphql/github-user-repos-request.graphql");

    @Override
    protected String getQuery() {
        return QUERY;
    }

    @Override
    protected Class<UserDataGraphQlResponseDto> getReturnType() {
        return UserDataGraphQlResponseDto.class;
    }

    @Override
    protected Map<String, Object> toVariables(String rq) {
        return Map.of("login", rq);
    }
}
