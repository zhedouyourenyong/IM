package com.zzh.controller;


import com.zzh.algorithm.RouteHandle;
import com.zzh.cache.ServerListCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller("/")
public class LoginController
{
    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Resource(name = "LoopHandle")
    RouteHandle routeHandle;
    @Autowired
    ServerListCache cache;


    @RequestMapping("login")
    @ResponseBody
    public String login()
    {
        String address=routeHandle.routeServer(cache.getAll(),"");
        return address;
    }
}
