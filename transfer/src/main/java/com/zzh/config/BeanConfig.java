package com.zzh.config;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2019/12/22 18:17
 */
@Component
public class BeanConfig
{
    private ZkConfig zkConfig;

    @Autowired
    public BeanConfig(ZkConfig zkConfig)
    {
        this.zkConfig = zkConfig;
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

    @Bean
    public ZkClient buildZKClient()
    {
        return new ZkClient(zkConfig.getZkAddress(), zkConfig.getSessionTimeout(), zkConfig.getConnectionTimeout());
    }
}
