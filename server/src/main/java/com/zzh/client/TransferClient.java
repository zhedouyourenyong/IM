package com.zzh.client;

import com.zzh.client.handler.ServerTransferHandler;
import com.zzh.protobuf.Msg;
import com.zzh.util.SpringBeanFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(一句话描述该类的功能)
 * @Author: Administrator
 * @Date: 2019/12/21 15:29
 */
public class TransferClient
{
    public static void start(String[] transferUrls) throws Exception
    {
        for (String transferUrl : transferUrls)
        {
            String[] url = transferUrl.split(":");

            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            ChannelFuture f = b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>()
                    {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception
                        {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("IdleStateHandler", new IdleStateHandler(11, 0, 0));
                            pipeline.addLast(new ProtobufVarint32FrameDecoder());
                            pipeline.addLast(new ProtobufDecoder(Msg.Protocol.getDefaultInstance()));
                            pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                            pipeline.addLast(new ProtobufEncoder());
                            pipeline.addLast("MsgHandler", SpringBeanFactory.getBean(ServerTransferHandler.class));
                        }
                    }).connect(url[0], Integer.parseInt(url[1]))
                    .addListener((ChannelFutureListener) future -> {
                        if (!future.isSuccess())
                        {
                            throw new Exception("[connector] connect to transfer failed! transfer url: " + transferUrl);
                        }
                    });

            try
            {
                f.get(10, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e)
            {
                throw new Exception("[connector] connect to transfer failed! transfer url: " + transferUrl, e);
            }
        }
    }
}
