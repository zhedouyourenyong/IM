package com.zzh.service;

import com.alibaba.fastjson.JSONObject;
import com.zzh.config.ServerConfig;
import com.zzh.pojo.User;
import com.zzh.util.ClientChannelContext;
import io.netty.channel.Channel;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserOffLineService
{
    private final static Logger logger = LoggerFactory.getLogger(UserOffLineService.class);
    private final MediaType mediaType = MediaType.parse("application/json");

    @Autowired
    OkHttpClient httpClient;
    @Autowired
    ServerConfig serverConfig;

    public void userOffLine(User userInfo, Channel channel) throws IOException
    {
        if (userInfo != null)
        {
            logger.info("用户{}下线", userInfo.getUserName());
            clearRouteInfo(userInfo);
            ClientChannelContext.removeSession(userInfo.getUserId());
        }
        ClientChannelContext.remove(channel);
    }

    //清楚路由层保存的用户登录状态和路由信息
    private void clearRouteInfo(User userInfo) throws IOException
    {
        JSONObject info = new JSONObject();
        info.put("userId", userInfo.getUserId());
        info.put("msg", "offLine");

        RequestBody requestBody = RequestBody.create(mediaType, info.toString());
        Request request = new Request.Builder()
                .post(requestBody)
                .url(serverConfig.getOffLineURL())
                .build();

        Response response = null;
        try
        {
            response = httpClient.newCall(request).execute();
            if (!response.isSuccessful())
            {
                throw new IOException("Unexpected code " + response);
            }
        } finally
        {
            response.body().close();
        }
    }
}
