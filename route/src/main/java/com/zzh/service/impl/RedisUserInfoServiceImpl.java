package com.zzh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzh.constant.RedisKey;
import com.zzh.pojo.User;
import com.zzh.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2019/12/14 13:02
 */
@Slf4j
@Service
public class RedisUserInfoServiceImpl implements UserInfoService
{
    private StringRedisTemplate redisTemplate;

    @Autowired
    public RedisUserInfoServiceImpl(StringRedisTemplate redisTemplate)
    {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveUserInfo(User user)
    {
        try
        {
            redisTemplate.opsForValue().set(RedisKey.USER_INFO_PREFIX + user.getUserId(), JSON.toJSONString(user));
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Boolean removeUserInfoByUserId(String userId)
    {
        try
        {
            return redisTemplate.delete(RedisKey.USER_INFO_PREFIX + userId);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public User getUserInfoByUserId(String userId)
    {
        try
        {
            String userInfo = redisTemplate.opsForValue().get(RedisKey.USER_INFO_PREFIX + userId);
            return JSONObject.parseObject(userInfo, User.class);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
