package com.zzh.service.impl;

import com.zzh.constant.Constant;
import com.zzh.enums.StatusEnum;
import com.zzh.request.LoginRequestVO;
import com.zzh.service.AccountService;
import com.zzh.service.UserInfoCacheService;
import com.zzh.request.RegisterInfoReqVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountServiceRedisImpl implements AccountService
{
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceRedisImpl.class);

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserInfoCacheService userInfoCacheService;


    @Override
    public Map<String, Object> register (RegisterInfoReqVO registerInfo) throws Exception
    {
        Map<String, Object> result = new HashMap<>();

        String key = Constant.ACCOUNT_PREFIX + registerInfo.getUserName();
        String id = redisTemplate.opsForValue().get(key);

        if(id != null)
        {
            result.put("status", StatusEnum.REPEAT_REGISTRY);
        } else
        {
            long userId = System.currentTimeMillis();
            String accountId = Constant.ACCOUNT_PREFIX + userId;
            String accountName = Constant.ACCOUNT_PREFIX + registerInfo.getUserName();
            redisTemplate.opsForValue().set(accountId, registerInfo.getUserName());
            redisTemplate.opsForValue().set(accountName, String.valueOf(userId));

            result.put("status", StatusEnum.SUCCESS);
            result.put("userId", userId);
        }

        return result;
    }

    @Override
    public StatusEnum login (LoginRequestVO loginReqVO) throws Exception
    {
        String key = Constant.ACCOUNT_PREFIX + loginReqVO.getUserId();
        String userName = redisTemplate.opsForValue().get(key);
        if(userName == null || !userName.equals(loginReqVO.getUserName()))
        {
            return StatusEnum.ACCOUNT_NOT_MATCH;
        }

        boolean flag = userInfoCacheService.saveAndCheckUserLoginStatus(loginReqVO.getUserId());
        if(!flag)
            return StatusEnum.REPEAT_LOGIN;

        return StatusEnum.SUCCESS;
    }

    @Override
    public void saveRouteInfo (LoginRequestVO loginReqVO, String msg) throws Exception
    {
        redisTemplate.opsForValue().set(Constant.ROUTE_PREFIX + loginReqVO.getUserId(), msg);

    }

    @Override
    public void offLine (Long userId) throws Exception
    {
        //清除登录状态和路由
        redisTemplate.delete(Constant.ROUTE_PREFIX + userId);
        userInfoCacheService.removeLoginStatus(userId);
    }
}
