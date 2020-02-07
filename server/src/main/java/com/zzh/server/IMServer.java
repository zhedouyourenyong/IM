package com.zzh.server;

import com.zzh.config.ServerConfig;
import com.zzh.exception.ImException;
import com.zzh.handler.MessageHandler;
import com.zzh.protocol.codec.MsgDecoder;
import com.zzh.protocol.codec.MsgEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 基于Netty实现的IM服务端
 */
@Slf4j
@Component
public class IMServer
{
    private ServerConfig serverConfig;
    private MessageHandler msgHandler;

    @Autowired
    public IMServer(ServerConfig serverConfig, MessageHandler msgHandler)
    {
        this.serverConfig = serverConfig;
        this.msgHandler = msgHandler;
    }


    /**
     * 开启Netty服务
     *
     * @throws Exception
     */
    public void start() throws Exception
    {
        if (serverConfig.getServerPort() == 0)
        {
            throw new ImException("Netty服务端的端口没有配置.");
        }

        ServerBootstrap serverBootstrap = newServerBootStrap();
        serverBootstrap
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>()
                {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception
                    {
                        ChannelPipeline pipeline = channel.pipeline();
//                      pipeline.addLast("IdleStateHandler", new IdleStateHandler(11, 0, 0));
                        pipeline.addLast("MsgDecoder", new MsgDecoder());
                        pipeline.addLast("MsgEncoder", new MsgEncoder());
                        pipeline.addLast("MsgHandler", msgHandler);
                    }
                });
        bind(serverBootstrap, serverConfig.getServerPort());
    }

    private void bind(ServerBootstrap bootstrap, int port)
    {
        bootstrap.bind(port).addListener(future -> {
            if (future.isSuccess())
            {
                log.info(new Date() + ": 端口[" + port + "]绑定成功!");

                /**
                 * 关闭时的清理工作
                 */
                Runtime.getRuntime().addShutdownHook(new ShutdownThread(bootstrap, (ChannelFuture) future));
            } else
            {
                log.info("端口[" + port + "]绑定失败!");
            }
        });
    }


    /**
     * 如果系统本身支持epoll会优先使用EpollEventLoopGroup
     */
    private ServerBootstrap newServerBootStrap()
    {
        if (Epoll.isAvailable())
        {
            EventLoopGroup bossGroup = new EpollEventLoopGroup();
            EventLoopGroup workerGroup = new EpollEventLoopGroup();

            return new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(EpollServerSocketChannel.class);
        }
        return newNioServerBootstrap();
    }

    private ServerBootstrap newNioServerBootstrap()
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        return new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class);
    }


    static class ShutdownThread extends Thread
    {
        private ServerBootstrap serverBootstrap;
        private ChannelFuture bossChannelFuture;

        public ShutdownThread(ServerBootstrap serverBootstrap, ChannelFuture bossChannelFuture)
        {
            this.serverBootstrap = serverBootstrap;
            this.bossChannelFuture = bossChannelFuture;
        }

        @Override
        public void run()
        {
            try
            {
                if (bossChannelFuture != null)
                {
                    bossChannelFuture.channel().close().awaitUninterruptibly(10, TimeUnit.SECONDS);
                    bossChannelFuture = null;
                }
                if (serverBootstrap != null && serverBootstrap.config().group() != null)
                {
                    serverBootstrap.config().group().shutdownGracefully();
                }
                if (serverBootstrap != null && serverBootstrap.config().childGroup() != null)
                {
                    serverBootstrap.config().childGroup().shutdownGracefully();
                }
                serverBootstrap = null;

                log.info("Netty server stopped");
            } catch (Exception e)
            {
                log.error("ShutdownThread has error", e);
            }

        }
    }
}