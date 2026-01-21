package com.github.regyl.gfi.configuration.async;

import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
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
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new DefaultAsyncUncaughtExceptionHandlerImpl();
    }

    @Bean("issueLoadAsyncExecutor")
    public ThreadPoolTaskExecutor issueLoadAsyncExecutor() {
        //TODO maybe Executors#workStealing
        //since graphql calls got blocked on I/O
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(configProps.getCorePoolSize());
        executor.setMaxPoolSize(configProps.getMaxPoolSize());
        executor.setPrestartAllCoreThreads(false);
        executor.setThreadGroupName("load-group-async-");
        executor.setThreadNamePrefix("load-async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
