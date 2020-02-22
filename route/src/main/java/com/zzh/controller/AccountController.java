package com.zzh.controller;


import com.alibaba.fastjson.JSONObject;
import com.zzh.http.request.OffLineRequest;
import com.zzh.http.request.RegisterRequest;
import com.zzh.loadbalance.LoadBalance;
import com.zzh.http.response.BaseResponse;
import com.zzh.pojo.User;
import com.zzh.discovery.ServerAddressCache;
import com.zzh.enums.StatusEnum;
import com.zzh.pojo.RouteInfo;
import com.zzh.http.request.LoginRequestVO;
import com.zzh.http.response.*;
import com.zzh.service.AccountService;
import com.zzh.service.RouteInfoService;
import com.zzh.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Slf4j
@RestController
public class AccountController
{
    @Resource(name = "LoopHandle")
    private LoadBalance routeHandle;

    private ServerAddressCache serverAddressCache;
    private AccountService accountService;
    private RouteInfoService routeInfoService;
    private UserInfoService userInfoService;

    @Autowired
    public AccountController(ServerAddressCache serverAddressCache, AccountService accountService, RouteInfoService routeInfoService, UserInfoService userInfoService)
    {
        this.serverAddressCache = serverAddressCache;
        this.accountService = accountService;
        this.routeInfoService = routeInfoService;
        this.userInfoService = userInfoService;
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResponse login(@RequestBody LoginRequestVO loginReqVO) throws Exception
    {
        log.info(loginReqVO.toString());
        BaseResponse response = new BaseResponse();
        LoginResponse result = new LoginResponse();

        try
        {
            StatusEnum status = accountService.login(loginReqVO.getName(), loginReqVO.getPassword());
            response.setStatusEnum(status);
            if (status == StatusEnum.SUCCESS)
            {
                /**
                 * ip地址:Netty端口号:Server唯一识别码
                 */
                String address = routeHandle.routeServer(serverAddressCache.getAll());
                JSONObject url = JSONObject.parseObject(address);
                RouteInfo routeInfo = new RouteInfo();
                routeInfo.setIp(url.getString("ip"));
                routeInfo.setPort(url.getInteger("serverPort"));
                routeInfo.setServerId(url.getString("serverId"));
                String addressJson = JSONObject.toJSONString(routeInfo);

                result.setRouteInfo(routeInfo);
                result.setUserId(accountService.getUserIdByName(loginReqVO.getName()));

                //保存路由信息
                routeInfoService.saveRouteInfo(String.valueOf(result.getUserId()), addressJson);

                response.setDataBody(JSONObject.toJSONString(result));
                return response;
            }
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return response;
    }

    @RequestMapping(value = "/registerAccount", method = RequestMethod.POST)
    public BaseResponse registerAccount(@RequestBody RegisterRequest registerInfo) throws Exception
    {
        BaseResponse response = new BaseResponse();
        StatusEnum status = accountService.register(registerInfo.getUserName(), registerInfo.getPassword());
        response.setStatusEnum(status);
        return response;
    }

    /**
     * 由server调用
     *
     * @param reqVO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/offLine", method = RequestMethod.POST)
    public void offLine(@RequestBody OffLineRequest reqVO) throws Exception
    {
        User user = userInfoService.getUserInfoByUserId(reqVO.getUserId());
        log.info("下线用户[{}]", user.toString());
        accountService.offLine(reqVO.getUserId());
    }
}
