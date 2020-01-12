package com.zzh.handler.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzh.constant.Constant;
import com.zzh.domain.ClientConnection;
import com.zzh.domain.ClientConnectionContext;
import com.zzh.protobuf.Protocol;
import com.zzh.handler.ServerHandlerInterface;
import com.zzh.service.ServerAckWindow;
import com.zzh.util.IdUtil;
import com.zzh.util.NettyAttrUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 处理来自客户端的greet消息
 * @Author: Administrator
 * @Date: 2019/12/10 10:36
 */
@Service
public class GreetService implements ServerHandlerInterface
{
    private static final Logger logger = LoggerFactory.getLogger(GreetService.class);

    @Autowired
    private ClientConnectionContext clientConnectionContext;


    @Override
    public void process(ChannelHandlerContext ctx, Protocol.Msg msg) throws Exception
    {
        logger.info("begin process GreetMsg");

        JSONObject userInfo = JSON.parseObject(msg.getMsgBody());
        String userId = userInfo.getString(Constant.USER_ID);
        String userName = userInfo.getString(Constant.USER_NAME);
        String clientId = userInfo.getString(Constant.CLIENT_ID);

        if (userId == null || userName == null || clientId == null)
        {
            throw new Exception("userId or userName or clientId not in the greet msg");
        }

        addUserInfoToChannelAttr(ctx, userId, userName, clientId);

        ClientConnection clientConnection = addChannelToContext(ctx, userId, clientId);
        initServerAckWindow(clientConnection);

        JSONObject greetResp = initTid(ctx);

        ctx.channel().writeAndFlush(getGreetResponse(greetResp));
        logger.info("客户端[{}]上线成功", userInfo.getString(Constant.USER_ID));
    }

    @Override
    public Protocol.MsgType getMsgType()
    {
        return Protocol.MsgType.GREET;
    }


    private JSONObject initTid(ChannelHandlerContext ctx)
    {
        AtomicLong tid = new AtomicLong();
        ctx.channel().attr(NettyAttrUtil.TID_GENERATOR).set(tid);
        JSONObject greetResp = new JSONObject();
        greetResp.put(Constant.ITd, tid);
        return greetResp;
    }

    private ClientConnection addChannelToContext(ChannelHandlerContext ctx, String userId, String clientId)
    {
        ClientConnection clientConnection = new ClientConnection(userId, clientId, ctx);
        clientConnectionContext.addClientConnection(clientConnection);
        return clientConnection;
    }

    private ServerAckWindow initServerAckWindow(ClientConnection clientConnection)
    {
        return new ServerAckWindow(clientConnection.getClientId(), 1024, Duration.ofSeconds(5));
    }

    private void addUserInfoToChannelAttr(ChannelHandlerContext ctx, String userId, String userName, String clientId)
    {
        ctx.channel().attr(NettyAttrUtil.USER_NAME).set(userName);
        ctx.channel().attr(NettyAttrUtil.USER_ID).set(userId);
        ctx.channel().attr(NettyAttrUtil.CLIENT_ID).set(clientId);
    }


    private Protocol.Msg getGreetResponse(JSONObject greetBody)
    {
        Protocol.Msg greetResp = Protocol.Msg.newBuilder()
                .setId(String.valueOf(IdUtil.snowGenId()))
                .setFromModule(Protocol.Module.SERVER)
                .setDestModule(Protocol.Module.CLIENT)
                .setMsgBody(greetBody.toString())
                .setTimeStamp(String.valueOf(System.currentTimeMillis()))
                .setMsgType(Protocol.MsgType.GREET)
                .build();
        return greetResp;
    }

}