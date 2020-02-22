package com.zzh.core;

import com.zzh.handler.IMClientHandle;
import com.zzh.protocol.codec.MsgDecoder;
import com.zzh.protocol.codec.MsgEncoder;
import com.zzh.util.IdUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 连接服务器，创建api
 */

@Slf4j
public class IMClient
{
    public static final String CLIENT_ID = IdUtil.uuid();

    private static final int MAX_RETRY = 5;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private IMClientHandle clientHandle;

    private Channel channel = null;

    public IMClient(ClientMsgListener msgListener)
    {
        clientHandle = new IMClientHandle(msgListener);
    }

    public Channel start(String ip, int port)
    {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception
                    {
                        ChannelPipeline pipeline = channel.pipeline();
//                      pipeline.addLast("IdleStateHandler", new IdleStateHandler(11, 0, 0));
                        pipeline.addLast("MsgDecoder", new MsgDecoder());
                        pipeline.addLast("MsgEncoder", new MsgEncoder());
                        pipeline.addLast("IMClientHandle", clientHandle);
                    }
                });
        connect(bootstrap, ip, port, MAX_RETRY);
        try
        {
            countDownLatch.await();
        } catch (InterruptedException e)
        {
            log.error("连接服务器超时", e);
            return null;
        }
        return channel;
    }

    private void connect(Bootstrap bootstrap, String host, int port, int retry)
    {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess())
            {
                log.info(new Date() + "连接服务器成功,启动控制台线程...");
                channel = ((ChannelFuture) future).channel();
                countDownLatch.countDown();
            } else if (retry == 0)
            {
                log.error("重连次数用完,连接服务器失败!");
            } else
            {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                log.info(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(() -> {
                    connect(bootstrap, host, port, retry - 1);
                }, delay, TimeUnit.SECONDS);
            }
        });
    }
}
