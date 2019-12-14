package com.zzh.service.impl;

import com.zzh.constant.RedisKey;
import com.zzh.service.UserStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class UserStatusServiceImpl implements UserStatusService
{
    private static final Logger logger = LoggerFactory.getLogger(UserStatusServiceImpl.class);


    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public boolean saveUserLoginStatus(String userId) throws Exception
    {
        long flag = redisTemplate.opsForSet().add(RedisKey.LOGIN_STATUS_PREFIX, userId);
        if (flag == 0)
            return false;
        else
            return true;
    }

    @Override
    public boolean checkUserLoginStatus(String userId) throws Exception
    {
        return redisTemplate.opsForSet().isMember(RedisKey.LOGIN_STATUS_PREFIX, userId);
    }

    @Override
    public void removeUserLoginStatus(String userId) throws Exception
    {
        redisTemplate.opsForSet().remove(RedisKey.LOGIN_STATUS_PREFIX, userId);
    }
}
