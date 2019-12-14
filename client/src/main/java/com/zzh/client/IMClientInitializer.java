package com.zzh.client;

import com.zzh.handler.IMClientHandle;
import com.zzh.protobuf.Msg;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

public class IMClientInitializer extends ChannelInitializer<NioSocketChannel>
{
    private final IMClientHandle clientHandle = new IMClientHandle();

    @Override
    protected void initChannel(NioSocketChannel channel) throws Exception
    {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("IdleStateHandler", new IdleStateHandler(11, 0, 0));
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(Msg.Protocol.getDefaultInstance()));
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
        pipeline.addLast("IMClientHandle", clientHandle);
    }
}
