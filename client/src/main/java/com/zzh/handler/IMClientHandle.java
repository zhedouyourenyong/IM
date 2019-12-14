package com.zzh.handler;

import com.alibaba.fastjson.JSONObject;
import com.zzh.constant.Constant;
import com.zzh.protobuf.Msg;
import com.zzh.util.IdUtil;
import com.zzh.util.SessionHolder;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ChannelHandler.Sharable
public class IMClientHandle extends SimpleChannelInboundHandler<Msg.Protocol>
{
    private static final Logger logger = LoggerFactory.getLogger(IMClientHandle.class);

    /**
     * 发送问候消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        logger.info("begin greet");

        JSONObject greet = new JSONObject();
        greet.put(Constant.USER_ID, SessionHolder.INSTANCE.getUserId());
        greet.put(Constant.USER_NAME, SessionHolder.INSTANCE.getUserName());

        Msg.Head head= Msg.Head.newBuilder()
                .setVersion(1)
                .setMsgId(IdUtil.snowGenId())
                .setDestModule(Msg.Module.SERVER)
                .setFromModule(Msg.Module.CLIENT)
                .setTimeStamp(System.currentTimeMillis())
                .setMsgType(Msg.MsgType.GREET)
                .build();
        Msg.Protocol greetMsg = Msg.Protocol.newBuilder()
                .setMsgHead(head)
                .setMsgBody(greet.toString())
                .build();
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(greetMsg);
        channelFuture.addListener((ChannelFutureListener) future -> {
                    logger.info("registry im server success!");
                }
        );

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Msg.Protocol msg) throws Exception
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
