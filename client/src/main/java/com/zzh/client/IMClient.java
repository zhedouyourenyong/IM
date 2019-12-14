package com.zzh.client;

import com.zzh.request.LoginRequestVO;
import com.zzh.pojo.RouteInfo;
import com.zzh.service.RouteRequest;
import com.zzh.util.SessionHolder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class IMClient
{
    private static final Logger logger = LoggerFactory.getLogger(IMClient.class);

    private ChannelFuture channelFuture;

    @Autowired
    RouteRequest routeRequest;

    @Value("${im.userId}")
    private String userId;

    @Value("${im.userName}")
    private String userName;


    public void start ()
    {
        try
        {
            RouteInfo routeInfo = login();
            startClient(routeInfo);
        } catch (Exception e)
        {
            logger.error("start IMClient failure", e);
        }
    }


    private RouteInfo login () throws Exception
    {
        LoginRequestVO loginRequestVO = new LoginRequestVO(userId, userName);
        RouteInfo routeInfo = routeRequest.getIMServer(loginRequestVO);
        String str = routeInfo.toString();

        SessionHolder.INSTANCE.setServiceInfo(str);
        SessionHolder.INSTANCE.setStartDate(new Date());
        SessionHolder.INSTANCE.saveUserInfo(Long.parseLong(userId), userName);

        logger.info("server address:{}", str);
        return routeInfo;
    }


    private void startClient (RouteInfo routeInfo)
    {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new IMClientInitializer());

        try
        {
            channelFuture = bootstrap.connect(routeInfo.getIp(), Integer.parseInt(routeInfo.getServerPort())).sync();

        } catch (Exception e)
        {
            //todo 做个失败重连
            logger.error("connect server fail", e);
        }

        if(channelFuture.isSuccess())
        {
            logger.info("IM Client start success");
        }
    }

}
