package com.zzh.kit;

import io.netty.channel.ChannelHandlerContext;

public interface HeartBeatHandler
{
    void process(ChannelHandlerContext ctx) throws Exception ;
}
