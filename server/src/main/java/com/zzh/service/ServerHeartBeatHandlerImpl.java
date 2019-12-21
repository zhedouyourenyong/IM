package com.zzh.service;

import com.zzh.config.ServerConfig;
import com.zzh.kit.HeartBeatHandler;
import com.zzh.pojo.User;
import com.zzh.util.NettyAttrUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 处理没有收到客户端心跳的情况
 */
@Service
public class ServerHeartBeatHandlerImpl implements HeartBeatHandler
{
    private static final Logger logger = LoggerFactory.getLogger(ServerHeartBeatHandlerImpl.class);

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private UserOffLineService routeHandler;


    @Override
    public void process(ChannelHandlerContext ctx) throws Exception
    {
        long heartBeatTime = serverConfig.getHeartTime() * 1000;

        Long lastReadTime = NettyAttrUtil.getLastReaderTime(ctx.channel());
        long now = System.currentTimeMillis();

        if (lastReadTime != null && now - lastReadTime > heartBeatTime)
        {
            String userName = ctx.channel().attr(NettyAttrUtil.USER_NAME).get();
            logger.warn("客户端[{}]心跳超时[{}]ms，需要关闭连接!", userName, now - lastReadTime);
        }

        routeHandler.userOffLine(ctx);
        ctx.channel().close();
    }
}
