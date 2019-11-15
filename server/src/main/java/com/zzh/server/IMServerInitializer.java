package com.zzh.server;


import com.zzh.codec.MsgDecoder;
import com.zzh.codec.MsgEncoder;
import com.zzh.handler.IMServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


public class IMServerInitializer extends ChannelInitializer<SocketChannel>
{
    private final IMServerHandler serverHandler = new IMServerHandler();

    @Override
    protected void initChannel (SocketChannel socketChannel) throws Exception
    {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("MsgDecoder", new MsgDecoder());
        pipeline.addLast("MsgEncoder", new MsgEncoder());
        pipeline.addLast("MsgHandler", serverHandler);
    }
}
