package com.zzh.service.impl;

import com.alibaba.fastjson.JSON;
import com.zzh.constant.RedisKey;
import com.zzh.pojo.User;
import com.zzh.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(基于redis提供服务)
 * @Author: Administrator
 * @Date: 2019/12/14 13:02
 */
@Service
public class UserInfoServiceImpl implements UserInfoService
{
    private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void saveUserInfo(User user)
    {
        redisTemplate.opsForValue().set(RedisKey.USER_INFO_PREFIX + user.getUserId(), JSON.toJSONString(user));
    }

    @Override
    public Boolean removeUserInfoByUserId(String userId)
    {
        return redisTemplate.delete(RedisKey.USER_INFO_PREFIX + userId);
    }

    @Override
    public User getUserInfoByUserId(String userId)
    {
        String userInfo = redisTemplate.opsForValue().get(RedisKey.USER_INFO_PREFIX + userId);
        return (User) JSON.parse(userInfo);
    }
}
