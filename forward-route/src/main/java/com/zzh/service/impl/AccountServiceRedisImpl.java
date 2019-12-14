package com.zzh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zzh.constant.Constant;
import com.zzh.repository.UserDao;
import com.zzh.pojo.User;
import com.zzh.service.AccountService;
import com.zzh.service.RouteInfoService;
import com.zzh.service.UserInfoService;
import com.zzh.service.UserStatusService;
import com.zzh.util.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountServiceRedisImpl implements AccountService
{
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceRedisImpl.class);

    @Autowired
    private UserStatusService userStatusService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RouteInfoService routeInfoService;
    @Autowired
    private UserInfoService userInfoService;

    @Override
    public JSONObject register(String userName, String password)
    {
        JSONObject resp = new JSONObject();
        if (StringUtils.isBlank(userName))
        {
            resp.put(Constant.STATUS, -1);
            resp.put(Constant.MESSAGE, "用户名不能为空");
            return resp;
        }
        if (StringUtils.isBlank(password))
        {
            resp.put(Constant.STATUS, -1);
            resp.put(Constant.MESSAGE, "密码不能为空");
            return resp;
        }
        User user = userDao.selectByName(userName);
        if (user != null)
        {
            resp.put(Constant.STATUS, -1);
            resp.put(Constant.MESSAGE, "用户名已经被注册");
            return resp;
        }

        userDao.addUser(IdUtil.uuid(), userName, password);
        resp.put(Constant.STATUS, 0);

        return resp;
    }


    @Override
    public JSONObject login(String userName, String password) throws Exception
    {
        JSONObject resp = new JSONObject();

        if (StringUtils.isBlank(userName))
        {
            resp.put(Constant.STATUS, -1);
            resp.put(Constant.MESSAGE, "用户名");
            return resp;
        }

        if (StringUtils.isBlank(password))
        {
            resp.put(Constant.STATUS, -1);
            resp.put(Constant.MESSAGE, "密码不能为空");
            return resp;
        }

        /**
         * userId
         */
        User userInfo = userDao.selectByName(userName);
        if (userInfo == null)
        {
            resp.put(Constant.STATUS, -1);
            resp.put(Constant.MESSAGE, "用户名不存在");
            return resp;
        }

        if (!userInfo.getPassword().equals(password))
        {
            resp.put(Constant.STATUS, -1);
            resp.put(Constant.MESSAGE, "密码错误");
            return resp;
        }

        /**
         * 检查是否重复登录，之后将用户存储到redis的用户状态中
         */
        boolean repeatLogin = userStatusService.checkUserLoginStatus(userInfo.getUserId());
        if (repeatLogin)
        {
            resp.put(Constant.STATUS, -1);
            resp.put(Constant.MESSAGE, "重复登录");
            return resp;
        }

        /**
         * 登录成功后存储用户登录状态和用户信息
         */
        userStatusService.saveUserLoginStatus(userInfo.getUserId());
        userInfoService.saveUserInfo(userInfo);

        resp.put(Constant.STATUS, 0);
        resp.put(Constant.USER_ID, userInfo.getUserId());
        return resp;
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
