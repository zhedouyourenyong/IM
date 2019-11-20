package com.zzh.handler;

import com.google.protobuf.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
public class IMServerHandler extends SimpleChannelInboundHandler<Message>
{
    private static final Logger logger = LoggerFactory.getLogger(IMServerHandler.class);

    @Override
    protected void channelRead0 (ChannelHandlerContext ctx, Message msg) throws Exception
    {
        logger.info(msg.toString());
    }
}