package com.zzh.config;


import okhttp3.OkHttpClient;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Configuration
public class BeanConfig
{
    @Autowired
    ServerConfig serverConfig;

    @Bean
    public ZkClient buildZKClient()
    {
        return new ZkClient(serverConfig.getZkAddress(), serverConfig.getSessionTimeout(), serverConfig.getConnectionTimeout());
    }

    @Bean
    public ExecutorService threadPool()
    {
        return Executors.newFixedThreadPool(8);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory)
    {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public OkHttpClient okHttpClient()
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        return builder.build();
    }

}
