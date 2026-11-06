package com.example.desporto24.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class JynloPushAsyncConfiguration {
    @Bean("jynloPushExecutor")
    public Executor jynloPushExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(1);
        pool.setMaxPoolSize(2);
        pool.setQueueCapacity(100);
        pool.setThreadNamePrefix("jynlo-push-");
        pool.initialize();
        return pool;
    }
}
