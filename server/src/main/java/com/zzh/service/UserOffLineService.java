package com.zzh.service;

import com.alibaba.fastjson.JSONObject;
import com.zzh.config.ServerConfig;
import com.zzh.constant.Constant;
import com.zzh.domain.ClientConnectionContext;
import com.zzh.util.NettyAttrUtil;
import io.netty.channel.ChannelHandlerContext;
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
    private OkHttpClient httpClient;
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private ClientConnectionContext clientConnectionContext;

    public void userOffLine(ChannelHandlerContext ctx) throws IOException
    {
        String userName = ctx.channel().attr(NettyAttrUtil.USER_NAME).get();
        String userId = ctx.channel().attr(NettyAttrUtil.USER_ID).get();

        clearRouteInfo(userId);

        clientConnectionContext.removeClientConnection(ctx);
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
