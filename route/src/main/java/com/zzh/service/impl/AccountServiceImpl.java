package com.zzh.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.zzh.constant.RedisKey;
import com.zzh.enums.StatusEnum;
import com.zzh.pojo.User;
import com.zzh.mapper.UserDao;
import com.zzh.service.AccountService;
import com.zzh.service.RouteInfoService;
import com.zzh.service.UserInfoService;
import com.zzh.userstate.UserStatusService;
import com.zzh.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService
{
    private UserStatusService userStatusService;
    private UserDao userDao;
    private RouteInfoService routeInfoService;
    private UserInfoService userInfoService;
    private StringRedisTemplate redisTemplate;

    @Autowired
    public AccountServiceImpl(UserStatusService userStatusService, UserDao userDao, RouteInfoService routeInfoService, UserInfoService userInfoService, StringRedisTemplate redisTemplate)
    {
        this.userStatusService = userStatusService;
        this.userDao = userDao;
        this.routeInfoService = routeInfoService;
        this.userInfoService = userInfoService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public StatusEnum register(String userName, String password)
    {
        JSONObject resp = new JSONObject();
        if (StringUtils.isBlank(userName))
        {
            return StatusEnum.NAME_IS_NULL;
        }
        if (StringUtils.isBlank(password))
        {
            return StatusEnum.PASSWORD_IS_NULL;
        }
        User user = userDao.selectByName(userName);
        if (user != null)
        {
            return StatusEnum.NAME_REPEAT;
        }

        userDao.addUser(IdUtil.uuid(), userName, password);
        return StatusEnum.SUCCESS;
    }


    /**
     * 检验成功后，将用户登录状态和用户信息保存在redis总，并返回userId.
     *
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    @Override
    public StatusEnum login(String userName, String password) throws Exception
    {
        if (StringUtils.isBlank(userName))
        {
            return StatusEnum.NAME_IS_NULL;
        }

        if (StringUtils.isBlank(password))
        {
            return StatusEnum.PASSWORD_IS_NULL;
        }

        /**
         * userId
         */
        User userInfo = userDao.selectByName(userName);
        if (userInfo == null)
        {
            return StatusEnum.NAME_NOT_EXIST;
        }

        if (!userInfo.getPassword().equals(password))
        {
            return StatusEnum.PASSWORD_ERROR;
        }

        /**
         * 检查是否重复登录，之后将用户存储到redis的用户状态中
         */
        boolean repeatLogin = userStatusService.checkUserLoginStatus(userInfo.getUserId());
        if (repeatLogin)
        {
            return StatusEnum.LOGIN_REPEAT;
        }

        /**
         * 登录成功后存储用户登录状态和用户信息
         */
        setUserIdByName(userName, Long.parseLong(userInfo.getUserId()));

        userStatusService.saveUserLoginStatus(userInfo.getUserId());
        userInfoService.saveUserInfo(userInfo);

        return StatusEnum.SUCCESS;
    }

    @Override
    public Long getUserIdByName(String name)
    {
        try
        {
            return Long.parseLong(Objects.requireNonNull(redisTemplate.opsForValue().get(RedisKey.NAME_MAP_ID + name)));
        } catch (Exception e)
        {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public void setUserIdByName(String name, Long id)
    {
        try
        {
            redisTemplate.opsForValue().set(RedisKey.NAME_MAP_ID + name, String.valueOf(id));
        } catch (Exception e)
        {
            log.error(e.getMessage());
        }
    }

    /**
     * 清除登录状态和路由
     *
     * @param userId 下线用户ID
     * @throws Exception
     */
    @Override
    public void offLine(String userId) throws Exception
    {
        routeInfoService.removeRouteInfoByUserId(userId);
        userStatusService.removeUserLoginStatus(userId);
        userInfoService.removeUserInfoByUserId(userId);
    }

}
