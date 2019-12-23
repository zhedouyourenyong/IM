package com.zzh.server;

import com.zzh.config.TransferConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class TransferServer
{
    public static final Logger logger = LoggerFactory.getLogger(TransferServer.class);

    @Autowired
    private TransferConfig serverConfig;

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

        logger.info("TransferServer is starting");
        serverBootstrap = newServerBootStrap();
        serverBootstrap
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new TransferInitializer());

        bossChannelFuture = serverBootstrap.bind(serverConfig.getServerPort()).sync();

        if (bossChannelFuture.isSuccess())
        {
            logger.info("TransferServer start successfully at port {}", serverConfig.getServerPort());
        } else
        {
            throw new Exception("[TransferServer] start failed");
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
            logger.info("TransferServer is not running!");
            return;
        }

        logger.info("TransferServer is stopping");
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

            bossGroup = new EpollEventLoopGroup();
            workerGroup = new EpollEventLoopGroup();

            return new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(EpollServerSocketChannel.class);
        }
        return newNioServerBootstrap();
    }

    private ServerBootstrap newNioServerBootstrap()
    {
        EventLoopGroup bossGroup, workerGroup;
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        return new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class);
    }

}
