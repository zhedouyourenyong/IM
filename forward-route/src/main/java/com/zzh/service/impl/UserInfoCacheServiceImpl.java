package com.zzh.service.impl;

import com.zzh.constant.Constant;
import com.zzh.pojo.UserInfo;
import com.zzh.service.UserInfoCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class UserInfoCacheServiceImpl implements UserInfoCacheService
{
    private static final Logger logger = LoggerFactory.getLogger(UserInfoCacheServiceImpl.class);

    /**
     * 本地缓存，为了防止内存撑爆，后期可换为 LRU。
     */
    private final static Map<Long, UserInfo> USER_INFO_MAP = new ConcurrentHashMap<>(1024);


    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public UserInfo loadUserInfoByUserId (Long userId)
    {
        //优先从本地缓存获取
        UserInfo userInfo=USER_INFO_MAP.get(userId);
        if(userInfo!=null)
        {
            return userInfo;
        }

        String userName = redisTemplate.opsForValue().get(Constant.ACCOUNT_PREFIX + userId);
        if(userName!=null)
        {
            userInfo=new UserInfo(userId,userName);
            USER_INFO_MAP.put(userId,userInfo);
        }

        return userInfo;
    }

    @Override
    public boolean saveAndCheckUserLoginStatus (Long userId) throws Exception
    {
        long flag = redisTemplate.opsForSet().add(Constant.LOGIN_STATUS_PREFIX, userId.toString());
        if(flag == 0)
            return false;
        else
            return true;
    }

    @Override
    public void removeLoginStatus (Long userId) throws Exception
    {
        USER_INFO_MAP.remove(userId);
        redisTemplate.opsForSet().remove(Constant.LOGIN_STATUS_PREFIX,userId.toString());
    }
}
