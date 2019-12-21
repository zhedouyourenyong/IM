package com.zzh.server.handler.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzh.constant.Constant;
import com.zzh.domain.ClientConnection;
import com.zzh.domain.ClientConnectionContext;
import com.zzh.protobuf.Msg;
import com.zzh.server.handler.ServerHandlerInterface;
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
 * 客户端与服务器建立连接后首先会发送greet消息，服务器收到后需要将userId保存在channel中
 * 将channel封装为ClientConnection放入容器，并设置ackWin,并将netId保存在channel中
 * 回复greet的应答，应答中包含tid
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
    public void process(ChannelHandlerContext ctx, Msg.Protocol msg) throws Exception
    {
        logger.info("begin process GreetMsg");
        JSONObject userInfo = JSON.parseObject(msg.getMsgBody());
        String userId = userInfo.getString(Constant.USER_ID);
        String userName = userInfo.getString(Constant.USER_NAME);

        if (userId == null || userName == null)
        {
            throw new Exception("userId or userName not in the greet msg");
        }

        initUserIdAndName(ctx, userId, userName);
        ClientConnection clientConnection = initClientConnection(ctx, userId);
        initServerAckWindow(clientConnection);
        initNetId(ctx, clientConnection);
        JSONObject greetResp = initTid(ctx);

        ctx.channel().writeAndFlush(getGreetResponse(greetResp));
        logger.info("客户端[{}]上线成功", userInfo.getString(Constant.USER_ID));
    }

    @Override
    public Msg.MsgType getMsgType()
    {
        return Msg.MsgType.GREET;
    }


    private JSONObject initTid(ChannelHandlerContext ctx)
    {
        AtomicLong tid = new AtomicLong(Long.parseLong(IdUtil.uuid()));
        ctx.channel().attr(NettyAttrUtil.TID_GENERATOR).set(tid);
        JSONObject greetResp = new JSONObject();
        greetResp.put(Constant.ITd, tid);
        return greetResp;
    }

    private ClientConnection initClientConnection(ChannelHandlerContext ctx, String userId)
    {
        ClientConnection clientConnection = new ClientConnection(userId, ctx);
        clientConnectionContext.addClientConnection(clientConnection);
        return clientConnection;
    }

    private ServerAckWindow initServerAckWindow(ClientConnection clientConnection)
    {
        ServerAckWindow ackWindow = new ServerAckWindow(clientConnection.getNetId(), 24, Duration.ofSeconds(5));
        return ackWindow;
    }

    private void initNetId(ChannelHandlerContext ctx, ClientConnection clientConnection)
    {
        ctx.channel().attr(NettyAttrUtil.NET_ID).set(clientConnection.getNetId());
    }

    private void initUserIdAndName(ChannelHandlerContext ctx, String userId, String userName)
    {
        ctx.channel().attr(NettyAttrUtil.USER_NAME).set(userName);
        ctx.channel().attr(NettyAttrUtil.USER_ID).set(userId);
    }


    private Msg.Protocol getGreetResponse(JSONObject greetBody)
    {
        Msg.Head head = Msg.Head.newBuilder()
                .setMsgType(Msg.MsgType.GREET)
                .build();
        Msg.Protocol greetResp = Msg.Protocol.newBuilder()
                .setMsgHead(head)
                .setMsgBody(greetBody.toString())
                .build();
        return greetResp;
    }

}