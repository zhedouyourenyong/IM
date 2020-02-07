package com.zzh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 异步任务配置类
 * @Author: Administrator
 * @Date: 2020/1/29 21:18
 */
@EnableAsync(proxyTargetClass = true)
@Configuration
public class ExecutorConfig
{
    @Bean
    public Executor taskPool()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("TaskPool-");
        executor.initialize();
        return executor;
    }
}
