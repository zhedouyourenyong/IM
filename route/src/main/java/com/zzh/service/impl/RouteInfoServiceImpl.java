package com.zzh.service.impl;

import com.zzh.constant.RedisKey;
import com.zzh.service.RouteInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(一句话描述该类的功能)
 * @Author: Administrator
 * @Date: 2019/12/11 14:46
 */
@Service
public class RouteInfoServiceImpl implements RouteInfoService
{
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public String getRouteInfoByUserId(String userId) throws Exception
    {
        String address = redisTemplate.opsForValue().get(RedisKey.ROUTE_PREFIX + userId);
        return address;
    }

    @Override
    public void saveRouteInfo(String userId, String routeInfo) throws Exception
    {
        redisTemplate.opsForValue().set(RedisKey.ROUTE_PREFIX + userId, routeInfo);
    }

    @Override
    public void removeRouteInfoByUserId(String userId) throws Exception
    {
        redisTemplate.delete(RedisKey.ROUTE_PREFIX + userId);
    }
}
