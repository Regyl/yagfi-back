package com.github.regyl.gfi.configuration.async;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AsyncConfiguration implements AsyncConfigurer {

    private final AsyncConfigurationProperties configProps;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(configProps.getCorePoolSize());
        executor.setMaxPoolSize(configProps.getCorePoolSize());
        executor.setPrestartAllCoreThreads(false);
        executor.setThreadGroupName("default-group-async-");
        executor.setThreadNamePrefix("default-async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setQueueCapacity(Integer.MAX_VALUE);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new DefaultAsyncUncaughtExceptionHandlerImpl();
    }

    @Bean("issueLoadAsyncExecutor")
    public Executor issueLoadAsyncExecutor(AutoUploadConfigurationProperties configProps) {
        return Executors.newWorkStealingPool(configProps.getCorePoolSize());
    }

    @Bean("cdxgenTaskPool")
    @ConditionalOnProperty(value = "spring.properties.feature-enabled.feed-generation", havingValue = "true")
    public ThreadPoolTaskExecutor cdxgenTaskPool(Collection<HttpHost> cdxgenHosts) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cdxgenHosts.size());
        executor.setMaxPoolSize(cdxgenHosts.size());
        executor.setPrestartAllCoreThreads(false);
        executor.setThreadGroupName("cdxgen-group-async-");
        executor.setThreadNamePrefix("cdxgen-async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
}
