package com.zzh.handler;

import com.alibaba.fastjson.JSONObject;
import com.zzh.ClientApplication;
import com.zzh.constant.Constant;
import com.zzh.protobuf.Protocol;
import com.zzh.util.IdUtil;
import com.zzh.util.SessionHolder;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ChannelHandler.Sharable
public class IMClientHandle extends SimpleChannelInboundHandler<Protocol.Msg>
{
    private static final Logger logger = LoggerFactory.getLogger(IMClientHandle.class);

    /**
     * 发送问候消息
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        logger.info("begin greet");

        JSONObject msgBody = new JSONObject();
        msgBody.put(Constant.USER_ID, SessionHolder.INSTANCE.getUserId());
        msgBody.put(Constant.CLIENT_ID, ClientApplication.CLIENT_ID);
        msgBody.put(Constant.USER_NAME,SessionHolder.INSTANCE.getUserName());

        Protocol.Msg greet = Protocol.Msg.newBuilder()
                .setId(String.valueOf(IdUtil.snowGenId()))
                .setTimeStamp(String.valueOf(System.currentTimeMillis()))
                .setFromModule(Protocol.Module.CLIENT)
                .setDestModule(Protocol.Module.SERVER)
                .setMsgType(Protocol.MsgType.GREET)
                .setMsgBody(msgBody.toString())
                .build();

        ChannelFuture channelFuture = ctx.channel().writeAndFlush(greet);
        channelFuture.addListener((ChannelFutureListener) future -> {
                    logger.info("registry im server success!");
                }
        );

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocol.Msg msg) throws Exception
    {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {

    }
}
