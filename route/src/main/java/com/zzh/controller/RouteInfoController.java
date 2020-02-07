package com.zzh.controller;

import com.alibaba.fastjson.JSONObject;
import com.zzh.constant.Constant;
import com.zzh.service.RouteInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: (向服务端提供查询用户路由信息的接口)
 * @Author: Administrator
 * @Date: 2019/12/11 14:51
 */
@Slf4j
@RestController
public class RouteInfoController
{
    private RouteInfoService routeInfoService;

    @Autowired
    public RouteInfoController(RouteInfoService routeInfoService)
    {
        this.routeInfoService = routeInfoService;
    }


    @RequestMapping(value = "/getRouteInfoByUserId", method = RequestMethod.POST)
     public String getRouteInfo(@RequestBody JSONObject msg)
    {
        log.info("Query ServerInfo,{}->{}", msg.getString(Constant.FROM_ID), msg.getString(Constant.TO_ID));

        String routeInfo = null;
        try
        {
            String userId = msg.getString(Constant.USER_ID);
            routeInfo = routeInfoService.getRouteInfoByUserId(userId);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return routeInfo;
    }
}
