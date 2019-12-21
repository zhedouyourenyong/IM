package com.zzh.server;


import com.zzh.server.handler.DefaultMsgHandler;
import com.zzh.protobuf.Msg;
import com.zzh.util.SpringBeanFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;


public class IMServerInitializer extends ChannelInitializer<NioSocketChannel>
{
    private DefaultMsgHandler msgHandler;

    public IMServerInitializer()
    {
        msgHandler = SpringBeanFactory.getBean(DefaultMsgHandler.class);
    }


    @Override
    protected void initChannel(NioSocketChannel channel) throws Exception
    {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("IdleStateHandler", new IdleStateHandler(11, 0, 0));
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(Msg.Protocol.getDefaultInstance()));
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
        pipeline.addLast("MsgHandler", msgHandler);
    }
}
