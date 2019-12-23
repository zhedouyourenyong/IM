package com.zzh.controller;

import com.alibaba.fastjson.JSONObject;
import com.zzh.constant.Constant;
import com.zzh.service.RouteInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: (向服务端提供查询用户路由信息的接口)
 * @Author: Administrator
 * @Date: 2019/12/11 14:51
 */
@Controller()
public class RouteInfoController
{
    private static final Logger logger = LoggerFactory.getLogger(RouteInfoController.class);
    @Autowired
    private RouteInfoService routeInfoService;


    @RequestMapping(value = "/getRouteInfoByUserId", method = RequestMethod.POST)
    @ResponseBody
    public String getRouteInfo(@RequestBody JSONObject msg)
    {
        logger.info("Query ServerInfo,{}->{}", msg.getString(Constant.FROM_ID), msg.getString(Constant.TO_ID));

        String routeInfo = null;
        try
        {
            String userId = msg.getString(Constant.USER_ID);
            routeInfo = routeInfoService.getRouteInfoByUserId(userId);
        } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return routeInfo;
    }
}
