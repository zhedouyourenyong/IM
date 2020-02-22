package com.zzh.userstate.impl;

import com.zzh.constant.RedisKey;
import com.zzh.userstate.UserStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class RedisUserStatusServiceImpl implements UserStatusService
{
    private StringRedisTemplate redisTemplate;

    @Autowired
    public RedisUserStatusServiceImpl(StringRedisTemplate redisTemplate)
    {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public Long saveUserLoginStatus(String userId)
    {
        try
        {
            return redisTemplate.opsForSet().add(RedisKey.LOGIN_STATUS_PREFIX, userId);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Boolean checkUserLoginStatus(String userId)
    {
        try
        {
            return redisTemplate.opsForSet().isMember(RedisKey.LOGIN_STATUS_PREFIX, userId);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Long removeUserLoginStatus(String userId)
    {
        try
        {
            return redisTemplate.opsForSet().remove(RedisKey.LOGIN_STATUS_PREFIX, userId);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
