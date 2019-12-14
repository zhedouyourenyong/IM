package com.zzh.service;

import com.zzh.config.ServerConfig;
import com.zzh.kit.HeartBeatHandler;
import com.zzh.pojo.User;
import com.zzh.util.NettyAttrUtil;
import com.zzh.util.ClientChannelContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ServerHeartBeatHandlerImpl implements HeartBeatHandler
{
    private static final Logger logger = LoggerFactory.getLogger(ServerHeartBeatHandlerImpl.class);

    @Autowired
    ServerConfig serverConfig;

    @Autowired
    UserOffLineService routeHandler;


    @Override
    public void process(ChannelHandlerContext ctx) throws Exception
    {
        long heartBeatTime = serverConfig.getHeartTime() * 1000;

        Long lastReadTime = NettyAttrUtil.getLastReaderTime(ctx.channel());
        long now = System.currentTimeMillis();

        User userInfo = null;
        if (lastReadTime != null && now - lastReadTime > heartBeatTime)
        {
            userInfo = ClientChannelContext.getUserInfo((NioSocketChannel) ctx.channel());
            if (userInfo != null)
            {
                logger.warn("客户端[{}]心跳超时[{}]ms，需要关闭连接!", userInfo.getUserName(), now - lastReadTime);
            }
        }

        routeHandler.userOffLine(userInfo, ctx.channel());
        ctx.channel().close();
    }
}
