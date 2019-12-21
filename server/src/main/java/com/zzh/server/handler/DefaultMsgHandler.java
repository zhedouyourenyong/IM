package com.zzh.server.handler;

import com.zzh.kit.HeartBeatHandler;
import com.zzh.protobuf.Msg;
import com.zzh.service.UserOffLineService;
import com.zzh.service.ServerHeartBeatHandlerImpl;
import com.zzh.util.NettyAttrUtil;
import com.zzh.util.SpringBeanFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @version v1.0
 * @ProjectName: im
 * @Description: (处理protobuf消息)
 * @Author: Administrator
 * @Date: 2019/12/10 10:30
 */
@Component
@ChannelHandler.Sharable
public class DefaultMsgHandler extends SimpleChannelInboundHandler<Msg.Protocol>
{
    private static final Logger logger = LoggerFactory.getLogger(DefaultMsgHandler.class);

    @Autowired
    private MsgHandlerDispather msgHandlerDispather;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Msg.Protocol msg) throws Exception
    {
        logger.info("receive msg={}", msg.toString());

        try
        {
            msgHandlerDispather.processMsg(ctx, msg);
        } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 可能出现业务判断离线后再次触发 channelInactive
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        String userName = ctx.channel().attr(NettyAttrUtil.USER_NAME).get();

        logger.warn("[{}]触发 channelInactive 掉线!", userName);

        UserOffLineService routeHandler = SpringBeanFactory.getBean(UserOffLineService.class);
        routeHandler.userOffLine(ctx);

        ctx.channel().close();

        super.channelInactive(ctx);
    }

    /**
     * 触发这个方法代表服务端在规定时间内没有收到客户端心跳包
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
        if (evt instanceof IdleStateEvent)
        {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE)
            {
                logger.info("定时检测客户端端是否存活");
                HeartBeatHandler heartBeatHandler = SpringBeanFactory.getBean(ServerHeartBeatHandlerImpl.class);
                heartBeatHandler.process(ctx);
            }
        }
        super.userEventTriggered(ctx, evt);
    }


    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        if ("Connection reset by peer".equals(cause.getMessage()))
        {
            return;
        }
        logger.error(cause.getMessage(), cause);
    }
}
