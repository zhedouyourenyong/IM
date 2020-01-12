package com.zzh.client;


import com.zzh.IMServerApplication;
import com.zzh.handler.MsgHandlerDispather;
import com.zzh.protobuf.Protocol;
import com.zzh.service.ServerAckWindow;
import com.zzh.util.IdUtil;
import com.zzh.util.NettyAttrUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 负责transfer与server之间的连接
 * @Author: Administrator
 * @Date: 2019/12/21 15:39
 */
@Component
@ChannelHandler.Sharable
public class ServerTransferHandler extends SimpleChannelInboundHandler<Protocol.Msg>
{
    private static final Logger logger = LoggerFactory.getLogger(ServerTransferHandler.class);

    private static List<ChannelHandlerContext> ctxList = new ArrayList<>();

    private ServerAckWindow serverAckWindow;

    @Autowired
    private MsgHandlerDispather msgHandlerDispather;


    public static ChannelHandlerContext getOneOfTransferCtx(long time)
    {
        if (ctxList.size() == 0)
        {
            logger.warn("connector is not connected to a transfer!");
        }
        return ctxList.get((int) (time % ctxList.size()));
    }

    public static List<ChannelHandlerContext> getCtxList()
    {
        if (ctxList.size() == 0)
        {
            logger.warn("connector is not connected to a transfer!");
        }
        return ctxList;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        logger.info("[ConnectorTransfer] connect to transfer");

        putConnectionId(ctx);
        greetToTransfer(ctx);

        ctxList.add(ctx);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocol.Msg msg) throws Exception
    {
        msgHandlerDispather.processMsg(ctx, msg);
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
        logger.error(cause.getMessage(), cause);
    }

    public void putConnectionId(ChannelHandlerContext ctx)
    {
        ctx.channel().attr(NettyAttrUtil.SERVER_ID).set(IdUtil.uuid());
    }

    private void greetToTransfer(ChannelHandlerContext ctx)
    {
        Protocol.Msg greet = Protocol.Msg.newBuilder()
                .setId(String.valueOf(IdUtil.snowGenId()))
                .setMsgType(Protocol.MsgType.GREET)
                .setFromModule(Protocol.Module.SERVER)
                .setDestModule(Protocol.Module.TRANSFER)
                .setTimeStamp(String.valueOf(System.currentTimeMillis()))
                .setMsgBody(IMServerApplication.SERVER_ID)
                .build();

        serverAckWindow.offer(greet.getId(), greet, msg -> ctx.writeAndFlush(msg))
                .thenAccept(m -> logger.info("[connector] connect to transfer successfully"))
                .exceptionally(e -> {
                    logger.error("[connector] waiting for transfer's response failed", e);
                    return null;
                });
    }
}
