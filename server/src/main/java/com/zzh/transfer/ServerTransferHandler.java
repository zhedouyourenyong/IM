package com.zzh.transfer;


import com.google.protobuf.Message;
import com.zzh.IMServerApplication;
import com.zzh.handler.MsgHandler;
import com.zzh.handler.MsgHandlerFactory;
import com.zzh.protocol.Internal;
import com.zzh.domain.ServerAckWindow;
import com.zzh.util.IdUtil;
import com.zzh.util.NettyAttrUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 负责transfer与server之间的连接
 * @Author: Administrator
 * @Date: 2019/12/21 15:39
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class ServerTransferHandler extends SimpleChannelInboundHandler<Message>
{
    private static final List<ChannelHandlerContext> ctxList = new ArrayList<>();

    private MsgHandlerFactory handlerFactory;


    @Autowired
    public ServerTransferHandler(MsgHandlerFactory msgHandlerFactory)
    {
        this.handlerFactory = msgHandlerFactory;
    }


    public static ChannelHandlerContext getOneOfTransferCtx(long time)
    {
        if (ctxList.size() == 0)
        {
            log.warn("connector is not connected to a transfer!");
        }
        return ctxList.get((int) (time % ctxList.size()));
    }

    public static List<ChannelHandlerContext> getCtxList()
    {
        if (ctxList.size() == 0)
        {
            log.warn("connector is not connected to a transfer!");
        }
        return ctxList;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        log.info("[ConnectorTransfer] connect to transfer");

        putConnectionId(ctx);
        greetToTransfer(ctx);

        ctxList.add(ctx);
    }


//    @Async("taskPool")
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

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        //todo: reconnect
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        if ("Connection reset by peer".equals(cause.getMessage()))
        {
            return;
        }
        log.error(cause.getMessage(), cause);
    }

    public void putConnectionId(ChannelHandlerContext ctx)
    {
        ctx.channel().attr(NettyAttrUtil.SERVER_ID).set(IMServerApplication.SERVER_ID);
    }

    private void greetToTransfer(ChannelHandlerContext ctx)
    {
        Internal.InternalMsg greet = Internal.InternalMsg.newBuilder()
                .setId(IdUtil.snowGenId())
                .setTimeStamp(System.currentTimeMillis())
                .setFrom(Internal.InternalMsg.Module.SERVER)
                .setDest(Internal.InternalMsg.Module.TRANSFER)
                .setMsgType(Internal.InternalMsg.MsgType.GREET_REQUEST)
                .setBody(IMServerApplication.SERVER_ID)
                .build();
        ctx.channel().writeAndFlush(greet);
    }
}
