package com.github.regyl.gfi.service.github;

public interface GithubClientService<T, S> {

    S execute(T rq);
}
