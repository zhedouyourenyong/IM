package com.zzh.handler;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.Message;
import com.zzh.ClientApplication;
import com.zzh.constant.Constant;
import com.zzh.domain.ClientAckWindow;
import com.zzh.domain.ServerAckWindow;
import com.zzh.protocol.Ack;
import com.zzh.protocol.Internal;
import com.zzh.protocol.Single;
import com.zzh.util.IdUtil;
import com.zzh.util.NettyAttrUtil;
import com.zzh.session.SessionHolder;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;


@Slf4j
@ChannelHandler.Sharable
public class IMClientHandle extends SimpleChannelInboundHandler<Message>
{
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        JSONObject greetMsg = new JSONObject();
        greetMsg.put(Constant.USER_ID, SessionHolder.INSTANCE.getUserId());
        greetMsg.put(Constant.CLIENT_ID, ClientApplication.CLIENT_ID);

        Internal.InternalMsg greet = Internal.InternalMsg.newBuilder()
                .setId(IdUtil.snowGenId())
                .setTimeStamp(System.currentTimeMillis())
                .setFrom(Internal.InternalMsg.Module.CLIENT)
                .setDest(Internal.InternalMsg.Module.SERVER)
                .setMsgType(Internal.InternalMsg.MsgType.GREET_REQUEST)
                .setBody(greetMsg.toString())
                .build();
        ctx.channel().writeAndFlush(greet).addListener(future -> {
            if (future.isSuccess())
            {
                ctx.channel().attr(NettyAttrUtil.CLIENT_ACK_WIN).set(new ClientAckWindow());
                ctx.channel().attr(NettyAttrUtil.SERVER_ACK_WIN).set(new ServerAckWindow(ClientApplication.CLIENT_ID, 12, Duration.ofSeconds(5)));
                log.info("greet send success!");
            }
        });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception
    {
        ClientAckWindow clientAckWindow = ctx.channel().attr(NettyAttrUtil.CLIENT_ACK_WIN).get();

        if (msg instanceof Single.SingleMsg)
        {
            Single.SingleMsg singleMsg = (Single.SingleMsg) msg;
            clientAckWindow.offer(singleMsg.getId(), singleMsg.getSessionId(), singleMsg.getFromId(), singleMsg, ctx.channel(), m -> {
                Single.SingleMsg message = (Single.SingleMsg) m;
                System.out.println("收到来自用户:{" + message.getFromId() + "}的消息");
                System.out.println(message.getBody());
            });
        } else if (msg instanceof Ack.AckMsg)
        {
            Ack.AckMsg m = (Ack.AckMsg) msg;
            clientAckWindow.setMsgToConsumed(m.getAckMsgSessionId());
            ServerAckWindow.ack(ClientApplication.CLIENT_ID, m);
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        log.info("网络出现问题,即将关闭连接");
        ctx.close();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        if ("Connection reset by peer".equals(cause.getMessage()))
        {
            return;
        }
        log.error(cause.getMessage(), cause);
        ctx.close();
    }
}