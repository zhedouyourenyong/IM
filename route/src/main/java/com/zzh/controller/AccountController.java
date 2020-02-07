package com.zzh.controller;


import com.alibaba.fastjson.JSONObject;
import com.zzh.http.request.OffLineRequest;
import com.zzh.http.request.RegisterRequest;
import com.zzh.loadbalance.LoadBalance;
import com.zzh.constant.Constant;
import com.zzh.http.BaseResponse;
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
    public LoginResponse login(@RequestBody LoginRequestVO loginReqVO) throws Exception
    {
        log.info(loginReqVO.toString());

        LoginResponse result = new LoginResponse();
        try
        {
            JSONObject status = accountService.login(loginReqVO.getName(), loginReqVO.getPassword());
            result.setSuccess(status.getBoolean(Constant.SUCCESS));
            result.setReason(status.getString(Constant.MESSAGE));

            if (status.getBoolean(Constant.SUCCESS))
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
                result.setUserId(status.getString(Constant.USER_ID));

                //保存路由信息
                routeInfoService.saveRouteInfo(result.getUserId(), addressJson);

                return result;
            }
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setReason(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/registerAccount", method = RequestMethod.POST)
    public BaseResponse<RegisterResponse> registerAccount(@RequestBody RegisterRequest registerInfo) throws Exception
    {
        BaseResponse<RegisterResponse> result = new BaseResponse<>();

        JSONObject info = accountService.register(registerInfo.getUserName(), registerInfo.getPassword());
        StatusEnum status = (StatusEnum) info.get(Constant.SUCCESS);

        result.setCode(status.getCode());
        result.setMessage(status.getMessage());

        if (status == StatusEnum.SUCCESS)
        {
            RegisterResponse resp = new RegisterResponse((Long) info.get("userId"), registerInfo.getUserName());
            result.setDataBody(resp);
        }
        return result;
    }

    /**
     * 由server调用
     *
     * @param reqVO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/offLine", method = RequestMethod.POST)
    public BaseResponse offLine(@RequestBody OffLineRequest reqVO) throws Exception
    {
        BaseResponse res = new BaseResponse();

        User user = userInfoService.getUserInfoByUserId(reqVO.getUserId());

        log.info("下线用户[{}]", user.toString());
        accountService.offLine(reqVO.getUserId());

        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }
}
