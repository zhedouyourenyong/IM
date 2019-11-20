package com.zzh.controller;


import com.zzh.algorithm.RouteHandle;
import com.zzh.cache.ServerListCache;
import com.zzh.enums.StatusEnum;
import com.zzh.pojo.UserInfo;
import com.zzh.request.LoginRequestVO;
import com.zzh.response.*;
import com.zzh.service.AccountService;
import com.zzh.service.UserInfoCacheService;
import com.zzh.request.OffLineReqVO;
import com.zzh.request.RegisterInfoReqVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;


@Controller("/")
public class AccountController
{
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Resource(name = "LoopHandle")
    RouteHandle routeHandle;
    @Autowired
    ServerListCache cache;
    @Autowired
    AccountService accountService;
    @Autowired
    UserInfoCacheService userInfoCacheService;


    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public LoginResponseVO login (@RequestBody LoginRequestVO loginReqVO) throws Exception
    {
        LoginResponseVO result = new LoginResponseVO();

        StatusEnum status = accountService.login(loginReqVO);
        result.setCode(status.getCode());
        result.setMessage(status.getMessage());

        if(status == StatusEnum.SUCCESS)
        {
            String address = routeHandle.routeServer(cache.getAll(), String.valueOf(loginReqVO.getUserId()));
            String[] tmp = address.split(":");

            ServerInfo serverInfo = new ServerInfo();
            serverInfo.setIp(tmp[0]);
            serverInfo.setServerPort(tmp[1]);
            serverInfo.setHttpPort(tmp[2]);

            result.setServerInfo(serverInfo);

            //保存路由信息
            accountService.saveRouteInfo(loginReqVO, address);
        }
        return result;
    }

    @RequestMapping(value = "registerAccount", method = RequestMethod.POST)
    @ResponseBody()
    public BaseResponse<RegisterInfoRespVO> registerAccount (@RequestBody RegisterInfoReqVO registerInfo) throws Exception
    {
        BaseResponse<RegisterInfoRespVO> result = new BaseResponse<>();

        Map<String, Object> info = accountService.register(registerInfo);
        StatusEnum status = (StatusEnum) info.get("status");

        result.setCode(status.getCode());
        result.setMessage(status.getMessage());

        if(status == StatusEnum.SUCCESS)
        {
            RegisterInfoRespVO resp = new RegisterInfoRespVO((Long) info.get("userId"), registerInfo.getUserName());
            result.setDataBody(resp);
        }
        return result;
    }

    @RequestMapping(value = "offLine", method = RequestMethod.POST)
    @ResponseBody()
    public BaseResponse<NULLBody> offLine (@RequestBody OffLineReqVO reqVO) throws Exception
    {
        BaseResponse<NULLBody> res = new BaseResponse();

        UserInfo cimUserInfo = userInfoCacheService.loadUserInfoByUserId(reqVO.getUserId());

        logger.info("下线用户[{}]", cimUserInfo.toString());
        accountService.offLine(reqVO.getUserId());

        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }
}
