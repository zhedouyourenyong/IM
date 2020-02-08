package com.zzh.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzh.config.RouteConfig;
import com.zzh.exception.ImException;
import com.zzh.http.request.LoginRequestVO;
import com.zzh.http.response.LoginResponse;
import com.zzh.pojo.RouteInfo;
import com.zzh.session.SessionHolder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 用户登录
 * @Author: Administrator
 * @Date: 2020/1/31 20:39
 */
@Slf4j
@Service
public class UserApi
{
    private MediaType mediaType = MediaType.parse("application/json");

    private OkHttpClient httpClient;
    private RouteConfig routeConfig;

    @Autowired
    public UserApi(OkHttpClient httpClient, RouteConfig routeConfig)
    {
        this.httpClient = httpClient;
        this.routeConfig = routeConfig;
    }


    public RouteInfo login(String userName, String password)
    {
        LoginRequestVO loginRequestVO = new LoginRequestVO(userName, password);
        LoginResponse loginResponse = getIMServer(loginRequestVO);
        if (loginResponse == null || !loginResponse.isSuccess())
        {
            throw new ImException("登录失败,msg:" + loginResponse.getReason());
        }
        log.info("登录成功,获取的服务器地址:{}", loginResponse.getRouteInfo().toString());

        setSession(loginResponse);

        return loginResponse.getRouteInfo();
    }

    private LoginResponse getIMServer(LoginRequestVO loginRequestVO)
    {

        RequestBody requestBody = RequestBody.create(mediaType, JSONObject.toJSONString(loginRequestVO));

        Request request = new Request.Builder()
                .url(routeConfig.getUrl())
                .post(requestBody)
                .build();


        LoginResponse loginResponse = null;
        ResponseBody body = null;
        try
        {
            Response response = httpClient.newCall(request).execute();
            if (!response.isSuccessful())
            {
                throw new IOException("Unexpected code " + response);
            }
            body = response.body();
            String content = body.string();
            loginResponse = JSON.parseObject(content, LoginResponse.class);

        } catch (Exception e)
        {
            log.error("login error", e);
        } finally
        {
            if (body != null)
            {
                body.close();
            }
        }

        return loginResponse;
    }

    private void setSession(LoginResponse loginResponse)
    {
        SessionHolder.INSTANCE.setServiceInfo(loginResponse.getRouteInfo().toString());
        SessionHolder.INSTANCE.setStartDate(new Date());
        SessionHolder.INSTANCE.setUserId(loginResponse.getUserId());
    }

}
