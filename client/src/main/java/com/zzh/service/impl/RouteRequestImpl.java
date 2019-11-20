package com.zzh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzh.config.AppConfig;
import com.zzh.enums.StatusEnum;
import com.zzh.service.RouteRequest;
import com.zzh.request.LoginRequestVO;
import com.zzh.response.LoginResponseVO;
import com.zzh.response.ServerInfo;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RouteRequestImpl implements RouteRequest
{
    @Autowired
    OkHttpClient httpClient;
    private MediaType mediaType = MediaType.parse("application/json");

    @Autowired
    AppConfig appConfig;

    @Override
    public ServerInfo getIMServer (LoginRequestVO loginRequestVO) throws Exception
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", loginRequestVO.getUserId());
        jsonObject.put("userName", loginRequestVO.getUserName());

        RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toString());
        Request request = new Request.Builder()
                .url(appConfig.getServerRouteLoginUrl())
                .post(requestBody)
                .build();

        Response response = httpClient.newCall(request).execute();
        if(!response.isSuccessful())
        {
            throw new IOException("Unexpected code " + response);
        }

        LoginResponseVO responseVO = null;
        ResponseBody responseBody = response.body();
        try
        {
            String json = responseBody.string();
            responseVO = JSON.parseObject(json, LoginResponseVO.class);
            if(!responseVO.getCode().equals(StatusEnum.SUCCESS.getCode()))
            {
                System.out.println(responseVO.getCode()+":"+responseVO.getMessage());
                System.exit(-1);
            }
        } finally
        {
            responseBody.close();
        }
        return responseVO.getServerInfo();
    }
}
