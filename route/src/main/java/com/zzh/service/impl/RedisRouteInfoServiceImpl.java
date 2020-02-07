package com.zzh.service.impl;

import com.zzh.constant.RedisKey;
import com.zzh.service.RouteInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2019/12/11 14:46
 */
@Slf4j
@Service
public class RedisRouteInfoServiceImpl implements RouteInfoService
{
    private StringRedisTemplate redisTemplate;

    @Autowired
    public RedisRouteInfoServiceImpl(StringRedisTemplate redisTemplate)
    {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public String getRouteInfoByUserId(String userId)
    {
        try
        {
            return redisTemplate.opsForValue().get(RedisKey.ROUTE_PREFIX + userId);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void saveRouteInfo(String userId, String routeInfo) throws Exception
    {
        try
        {
            redisTemplate.opsForValue().set(RedisKey.ROUTE_PREFIX + userId, routeInfo);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Boolean removeRouteInfoByUserId(String userId) throws Exception
    {
        try
        {
            return redisTemplate.delete(RedisKey.ROUTE_PREFIX + userId);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
