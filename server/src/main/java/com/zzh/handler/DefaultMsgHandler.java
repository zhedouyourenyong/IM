package com.zzh.handler;

import com.zzh.handler.service.*;
import com.zzh.kit.HeartBeatHandler;
import com.zzh.pojo.User;
import com.zzh.protobuf.Msg;
import com.zzh.service.UserOffLineService;
import com.zzh.service.ServerHeartBeatHandlerImpl;
import com.zzh.util.ClientChannelContext;
import com.zzh.util.SpringBeanFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(处理protobuf消息)
 * @Author: Administrator
 * @Date: 2019/12/10 10:30
 */
@Component
@ChannelHandler.Sharable
public class DefaultMsgHandler extends SimpleChannelInboundHandler<Msg.Protocol>
{
    private static final Logger logger = LoggerFactory.getLogger(DefaultMsgHandler.class);
    private final Map<Msg.MsgType, MsgHandlerInterface> msgDispather = new HashMap<>();

    public DefaultMsgHandler()
    {
        msgDispather.put(Msg.MsgType.GREET, SpringBeanFactory.getBean(GreetService.class));
        msgDispather.put(Msg.MsgType.ACK, SpringBeanFactory.getBean(AckService.class));
        msgDispather.put(Msg.MsgType.HEART, SpringBeanFactory.getBean(HeartService.class));
        msgDispather.put(Msg.MsgType.SINGLE, SpringBeanFactory.getBean(SingleMsgService.class));
        msgDispather.put(Msg.MsgType.GROUP, SpringBeanFactory.getBean(GroupMsgService.class));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Msg.Protocol msg) throws Exception
    {
        logger.info("receive msg={}", msg.toString());

        try
        {
            MsgHandlerInterface msgHandler = msgDispather.get(msg.getMsgHead().getMsgType());
            if (null != msgHandler)
            {
                msgHandler.process(ctx.channel(), msg);
            }
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
        User userInfo = ClientChannelContext.getUserInfo((NioSocketChannel) ctx.channel());
        if (userInfo != null)
        {
            logger.warn("[{}]触发 channelInactive 掉线!", userInfo.getUserName());

            UserOffLineService routeHandler = SpringBeanFactory.getBean(UserOffLineService.class);
            routeHandler.userOffLine(userInfo, ctx.channel());

            ctx.channel().close();
        }

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
