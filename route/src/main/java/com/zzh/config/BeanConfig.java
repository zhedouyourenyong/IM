package com.zzh.config;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class BeanConfig
{
    private RouteConfig routeConfig;

    @Autowired
    public BeanConfig(RouteConfig routeConfig)
    {
        this.routeConfig = routeConfig;
    }

    @Bean
    public ZkClient buildZKClient ()
    {
        return new ZkClient(routeConfig.getZkAddress(), routeConfig.getSessionTimeout(), routeConfig.getConnectionTimeout());
    }

    @Bean
    public LoadingCache<String, String> buildCache ()
    {
        return CacheBuilder.newBuilder()
                .build(new CacheLoader<String, String>()
                {
                    @Override
                    public String load (String s) throws Exception
                    {
                        return null;
                    }
                });
    }


    @Bean
    public RedisTemplate<String, String> redisTemplate (RedisConnectionFactory factory)
    {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
