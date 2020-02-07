package com.zzh.handler;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.Message;
import com.zzh.ClientApplication;
import com.zzh.constant.Constant;
import com.zzh.domain.ClientAckWindow;
import com.zzh.protocol.Ack;
import com.zzh.protocol.Internal;
import com.zzh.protocol.Single;
import com.zzh.util.IdUtil;
import com.zzh.util.SessionHolder;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ChannelHandler.Sharable
public class IMClientHandle extends SimpleChannelInboundHandler<Message>
{
    public static final AttributeKey<ClientAckWindow> CLIENT_ACK_WIN = AttributeKey.valueOf("clientAckWin");

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
                ctx.channel().attr(CLIENT_ACK_WIN).set(new ClientAckWindow());
                log.info("greet send success!");
            }
        });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception
    {
        log.info("receive msg:{}", msg.toString());

        ClientAckWindow ackWindow = ctx.channel().attr(CLIENT_ACK_WIN).get();

        if (msg instanceof Single.SingleMsg)
        {
            Single.SingleMsg singleMsg = (Single.SingleMsg) msg;
            ackWindow.offer(singleMsg.getId(), singleMsg.getSessionId(), singleMsg.getFromId(), singleMsg, ctx.channel(), m -> {
                Single.SingleMsg message = (Single.SingleMsg) m;
                System.out.println("收到来自用户:{" + message.getFromId() + "}的消息");
                System.out.println(message.getBody());
            });
        } else if (msg instanceof Ack.AckMsg)
        {
            Ack.AckMsg m = (Ack.AckMsg) msg;
            log.info("收到ACK:{}", m.toString());
            ackWindow.remove(Long.parseLong(m.getAckMsgId()));
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
    }
}