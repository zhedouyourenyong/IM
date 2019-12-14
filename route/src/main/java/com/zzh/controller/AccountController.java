package com.zzh.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzh.algorithm.RouteHandle;
import com.zzh.constant.Constant;
import com.zzh.discovery.impl.ZKServerAddressListener;
import com.zzh.enums.StatusEnum;
import com.zzh.pojo.RouteInfo;
import com.zzh.pojo.User;
import com.zzh.request.LoginRequestVO;
import com.zzh.request.LoginStatusRequest;
import com.zzh.request.OffLineReqVO;
import com.zzh.request.RegisterInfoReqVO;
import com.zzh.response.*;
import com.zzh.service.AccountService;
import com.zzh.service.RouteInfoService;
import com.zzh.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller("/")
public class AccountController
{
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Resource(name = "LoopHandle")
    private RouteHandle routeHandle;
    @Autowired
    private ZKServerAddressListener serverAddressListener;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RouteInfoService routeInfoService;
    @Autowired
    private UserInfoService userInfoService;


    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public LoginResponseVO login(@RequestBody LoginRequestVO loginReqVO) throws Exception
    {
        logger.info(loginReqVO.toString());
        LoginResponseVO result = new LoginResponseVO();
        try
        {
            JSONObject status = accountService.login(loginReqVO.getUserName(), loginReqVO.getPassword());
            result.setCode(status.getString(Constant.STATUS));
            result.setMessage(status.getString(Constant.MESSAGE));

            if (status.getInteger(Constant.STATUS) == 0)
            {
                /**
                 * 192.168.64.81:8899:8081
                 */
                String address = routeHandle.routeServer(serverAddressListener.getAll(), loginReqVO.getUserId());
                String[] tmp = address.split(":");

                RouteInfo routeInfo = new RouteInfo();
                routeInfo.setIp(tmp[0]);
                routeInfo.setServerPort(tmp[1]);
                routeInfo.setHttpPort(tmp[2]);

                String addressJson = JSON.toJSONString(routeInfo);
                result.setRouteInfo(routeInfo);

                //保存路由信息
                routeInfoService.saveRouteInfo(status.getString(Constant.USER_ID), addressJson);
            }
        } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }

        return result;
    }

    @RequestMapping(value = "registerAccount", method = RequestMethod.POST)
    @ResponseBody()
    public BaseResponse<RegisterInfoRespVO> registerAccount(@RequestBody RegisterInfoReqVO registerInfo) throws Exception
    {
        BaseResponse<RegisterInfoRespVO> result = new BaseResponse<>();

        JSONObject info = accountService.register(registerInfo.getUserName(), registerInfo.getPassword());
        StatusEnum status = (StatusEnum) info.get(Constant.STATUS);

        result.setCode(status.getCode());
        result.setMessage(status.getMessage());

        if (status == StatusEnum.SUCCESS)
        {
            RegisterInfoRespVO resp = new RegisterInfoRespVO((Long) info.get("userId"), registerInfo.getUserName());
            result.setDataBody(resp);
        }
        return result;
    }

    @RequestMapping(value = "offLine", method = RequestMethod.POST)
    @ResponseBody()
    public BaseResponse<NULLBody> offLine(@RequestBody OffLineReqVO reqVO) throws Exception
    {
        BaseResponse<NULLBody> res = new BaseResponse();

        User user = userInfoService.getUserInfoByUserId(reqVO.getUserId());

        logger.info("下线用户[{}]", user.toString());
        accountService.offLine(reqVO.getUserId());

        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }
}
