package com.zzh.handler;

import com.google.protobuf.Message;
import com.zzh.service.UserOffLineService;
import com.zzh.util.NettyAttrUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 消息处理
 * @Author: Administrator
 * @Date: 2019/12/10 10:30
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<Message>
{
    private MsgHandlerFactory handlerFactory;
    private UserOffLineService userOffLineService;

    @Autowired
    public MessageHandler(MsgHandlerFactory handlerFactory, UserOffLineService userOffLineService)
    {

        this.handlerFactory = handlerFactory;
        this.userOffLineService = userOffLineService;
    }

    @Async("taskPool")
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception
    {
        MsgHandler msgHandler = handlerFactory.getHandlerByMsgClass(msg.getClass());
        if (msgHandler != null)
        {
            msgHandler.process(ctx, msg);
        } else
        {
            log.error("get msgHandler fail! ClassName:{},msg:{}", msg.getClass().getName(), msg.toString());
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
        String userId = ctx.channel().attr(NettyAttrUtil.USER_ID).get();
        log.info("[{}]触发 channelInactive 掉线!", userId);

        try
        {
            userOffLineService.userOffLine(ctx);
            ctx.channel().close();
        } catch (Exception e)
        {
            log.error(e.getMessage());
        }

        super.channelInactive(ctx);
    }

    /**
     * 服务端在规定时间内没有收到客户端心跳包
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
                log.info("定时检测客户端端是否存活");
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
        String clientId = ctx.channel().attr(NettyAttrUtil.CLIENT_ID).get();
        log.error("clientId:{" + clientId + "} 发生异常,即将关闭连接", cause);
        ctx.close();
    }
}
