package com.zzh.service;

import com.alibaba.fastjson.JSONObject;
import com.zzh.config.ServerConfig;
import com.zzh.constant.Constant;
import com.zzh.domain.ClientConnectionContext;
import com.zzh.util.NettyAttrUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class UserOffLineService
{
    private final MediaType mediaType = MediaType.parse("application/json");

    private OkHttpClient httpClient;
    private ServerConfig serverConfig;
    private ClientConnectionContext connContext;

    @Autowired
    public UserOffLineService(OkHttpClient httpClient, ServerConfig serverConfig, ClientConnectionContext connContext)
    {
        this.httpClient = httpClient;
        this.serverConfig = serverConfig;
        this.connContext = connContext;
    }


    @Async("taskPool")
    public void userOffLine(ChannelHandlerContext ctx) throws IOException
    {
        String userId = ctx.channel().attr(NettyAttrUtil.USER_ID).get();

        clearRouteInfo(userId);

        connContext.removeClientConnection(ctx);
    }

    /**
     * 通知路由层 清理 用户的状态/路由/userInfo
     *
     * @param userId
     * @throws IOException
     */
    private void clearRouteInfo(String userId) throws IOException
    {
        JSONObject info = new JSONObject();
        info.put(Constant.USER_ID, userId);
        info.put(Constant.MESSAGE, "offLine");

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
