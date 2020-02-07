package com.zzh.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.Message;
import com.zzh.domain.ClientConnection;
import com.zzh.domain.ClientConnectionContext;
import com.zzh.domain.ServerAckWindow;
import com.zzh.handler.MsgHandler;
import com.zzh.protocol.Internal;
import com.zzh.util.NettyAttrUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2020/1/31 21:40
 */
@Slf4j
@Component
public class InternalMsgHandler implements MsgHandler
{
    private ClientConnectionContext clientContext;

    @Autowired
    public InternalMsgHandler(ClientConnectionContext clientContext)
    {
        this.clientContext = clientContext;
    }

    @Override
    public void process(ChannelHandlerContext ctx, Message msg) throws Exception
    {
        log.info("internal msg:{}", msg.toString());

        Internal.InternalMsg message = (Internal.InternalMsg) msg;
        if (message.getMsgType() == Internal.InternalMsg.MsgType.GREET_REQUEST)
        {
            JSONObject greet = JSONObject.parseObject(message.getBody());
            String userId = greet.getString("userId");
            String clientId = greet.getString("clientId");

            ctx.channel().attr(NettyAttrUtil.USER_ID).set(userId);
            ctx.channel().attr(NettyAttrUtil.CLIENT_ID).set(clientId);

            ClientConnection clientConnection = addChannelToContext(ctx, userId, clientId);
            initServerAckWindow(clientConnection);

            log.info("客户端[{}]上线成功", userId);
        }
    }


    private ClientConnection addChannelToContext(ChannelHandlerContext ctx, String userId, String clientId)
    {
        ClientConnection clientConnection = new ClientConnection(userId, clientId, ctx);
        clientContext.addClientConnection(clientConnection);
        return clientConnection;
    }

    private ServerAckWindow initServerAckWindow(ClientConnection clientConnection)
    {
        return new ServerAckWindow(clientConnection.getClientId(), 1024, Duration.ofSeconds(5));
    }

    @Override
    public Class<?> getProcessedType()
    {
        return Internal.InternalMsg.class;
    }
}
