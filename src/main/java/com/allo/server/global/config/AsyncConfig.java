package com.allo.server.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "imageUploadExecutor")
    public Executor imageUploadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setThreadGroupName("imageUploadExecutor");
        executor.setCorePoolSize(5); // 동시에 실행시킬 쓰레드의 개수 설정, 이미지를 최대 5개까지 업로드할 수 있기 때문에 corePoolSize == 5
        executor.setMaxPoolSize(20); // 쓰레드 풀의 최대 사이즈 설정
        executor.setQueueCapacity(50); // 쓰레드 풀 큐의 사이즈 설정
        executor.initialize();

        return executor;
    }
}
