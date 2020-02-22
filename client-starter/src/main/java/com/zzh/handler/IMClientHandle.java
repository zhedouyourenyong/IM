package com.zzh.handler;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.Message;
import com.zzh.core.ClientMsgListener;
import com.zzh.core.IMClient;
import com.zzh.constant.Constant;
import com.zzh.domain.ClientAckWindow;
import com.zzh.domain.ServerAckWindow;
import com.zzh.protocol.Ack;
import com.zzh.protocol.Internal;
import com.zzh.protocol.Single;
import com.zzh.session.Session;
import com.zzh.util.IdUtil;
import com.zzh.util.NettyAttrUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;


@Slf4j
@ChannelHandler.Sharable
public class IMClientHandle extends SimpleChannelInboundHandler<Message>
{
    private ClientMsgListener msgListener;

    public IMClientHandle(ClientMsgListener msgListener)
    {
        this.msgListener = msgListener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        JSONObject greetMsg = new JSONObject();
        greetMsg.put(Constant.USER_ID, Session.INSTANCE.getUserId());
        greetMsg.put(Constant.CLIENT_ID, IMClient.CLIENT_ID);

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

                ctx.channel().attr(NettyAttrUtil.SERVER_ACK_WIN).set(
                        new ServerAckWindow(IMClient.CLIENT_ID, 12,
                                Duration.ofSeconds(5)));

                msgListener.online();
            }
        });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message messages)
    {
        ClientAckWindow clientAckWindow = ctx.channel().attr(NettyAttrUtil.CLIENT_ACK_WIN).get();

        if (messages instanceof Single.SingleMsg)
        {
            Single.SingleMsg msg = (Single.SingleMsg) messages;
            clientAckWindow.offer(msg.getId(), msg.getSessionId(), msg.getFromId(), msg.getDestId(), msg, ctx.channel(), m -> {
                Single.SingleMsg message = (Single.SingleMsg) m;
                msgListener.read(message);
            });
        } else if (messages instanceof Ack.AckMsg)
        {
            Ack.AckMsg m = (Ack.AckMsg) messages;
            ServerAckWindow.ack(IMClient.CLIENT_ID, m);
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        msgListener.offline();
        ctx.close();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        if ("Connection reset by peer".equals(cause.getMessage()))
        {
            return;
        }
        msgListener.hasException(ctx, cause);
    }
}