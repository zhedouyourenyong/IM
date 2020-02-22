package com.zzh.core;


import com.alibaba.fastjson.JSONObject;
import com.zzh.enums.StatusEnum;
import com.zzh.exception.ImException;
import com.zzh.http.request.LoginRequestVO;
import com.zzh.http.response.BaseResponse;
import com.zzh.http.response.LoginResponse;
import com.zzh.pojo.RouteInfo;
import com.zzh.session.Session;
import com.zzh.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 用户登录
 * @Author: Administrator
 * @Date: 2020/1/31 20:39
 */
@Slf4j
public class UserApi
{
    public RouteInfo login(String routeUrl, String userName, String password)
    {
        LoginRequestVO loginRequestVO = new LoginRequestVO(userName, password);
        LoginResponse loginResponse = getIMServer(routeUrl, JSONObject.toJSONString(loginRequestVO));

        log.info("登录成功,获取的服务器地址:{}", loginResponse.getRouteInfo().toString());
        setSession(loginResponse);
        return loginResponse.getRouteInfo();
    }

    private LoginResponse getIMServer(String routeURL, String loginRequest)
    {
        BaseResponse response = HttpUtil.doPostSync(routeURL, loginRequest, BaseResponse.class);
        if (response == null || !(response.getStatusEnum() == StatusEnum.SUCCESS))
        {
            throw new ImException("登录失败,msg:" + response.getStatusEnum());
        }
        return JSONObject.parseObject(response.getDataBody(), LoginResponse.class);
    }

    private void setSession(LoginResponse loginResponse)
    {
        Session.INSTANCE.setServiceInfo(loginResponse.getRouteInfo().toString());
        Session.INSTANCE.setStartDate(new Date());
        Session.INSTANCE.setUserId(String.valueOf(loginResponse.getUserId()));
    }

}
