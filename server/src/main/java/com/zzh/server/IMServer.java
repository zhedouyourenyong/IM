package com.zzh.server;

import com.zzh.config.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 基于Netty实现的IM服务端
 */
@Component
public class IMServer
{
    public static final Logger logger = LoggerFactory.getLogger(IMServer.class);

    @Autowired
    private ServerConfig serverConfig;

    private ServerBootstrap serverBootstrap;
    private ChannelFuture bossChannelFuture;


    /**
     * 开启Netty服务
     *
     * @throws Exception
     */
    public void start() throws Exception
    {
        if (serverConfig.getServerPort() == 0)
        {
            logger.info("NettyServerPort not config.");
            return;
        }

        logger.info("NettyServer is starting");
        serverBootstrap = newServerBootStrap();
        serverBootstrap
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new IMServerInitializer());

        bossChannelFuture = serverBootstrap.bind(serverConfig.getServerPort()).sync();

        if (bossChannelFuture.isSuccess())
        {
            logger.info("NettyServer start successfully at port {}", serverConfig.getServerPort());
        } else
        {
            throw new Exception("[NettyServer] start failed");
        }

        /**
         * 关闭时的清理工作
         */
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
    }


    class ShutdownThread extends Thread
    {
        @Override
        public void run()
        {
            close();
        }
    }

    private void close()
    {
        if (serverBootstrap == null)
        {
            logger.info("Netty server is not running!");
            return;
        }

        logger.info("WebSocket server is stopping");
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

        logger.info("Netty server stopped");
    }


    /**
     * 如果系统本身支持epoll会优先使用EpollEventLoopGroup
     */
    private ServerBootstrap newServerBootStrap()
    {
        if (Epoll.isAvailable())
        {
            EventLoopGroup workerGroup, bossGroup;
            if (serverConfig.getBossThreads() > 0 && serverConfig.getWorkerThreads() > 0)
            {
                bossGroup = new EpollEventLoopGroup(serverConfig.getBossThreads(), new DefaultThreadFactory("NettyBossGroup", true));
                workerGroup = new EpollEventLoopGroup(serverConfig.getWorkerThreads(), new DefaultThreadFactory("NettyWorkerGroup", true));
            } else
            {
                bossGroup = new EpollEventLoopGroup();
                workerGroup = new EpollEventLoopGroup();
            }
            return new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(EpollServerSocketChannel.class);
        }
        return newNioServerBootstrap();
    }

    private ServerBootstrap newNioServerBootstrap()
    {
        EventLoopGroup bossGroup, workerGroup;
        if (serverConfig.getBossThreads() > 0 && serverConfig.getWorkerThreads() > 0)
        {
            bossGroup = new NioEventLoopGroup(serverConfig.getBossThreads(), new DefaultThreadFactory("NettyBossGroup", true));
            workerGroup = new NioEventLoopGroup(serverConfig.getWorkerThreads(), new DefaultThreadFactory("NettyWorkerGroup", true));
        } else
        {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
        }

        return new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class);
    }

}
