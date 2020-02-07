package com.zzh.transfer;

import com.alibaba.fastjson.JSONObject;
import com.zzh.IMServerApplication;
import com.zzh.protocol.codec.MsgDecoder;
import com.zzh.protocol.codec.MsgEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 负责server与transfer之间连接
 * @Author: Administrator
 * @Date: 2019/12/21 15:29
 */
@Slf4j
@Component
public class TransferClient
{
    private ServerTransferHandler handler;

    @Autowired
    public TransferClient(ServerTransferHandler handler)
    {
        this.handler = handler;
    }


    public void start(List<String> transferUrls) throws Exception
    {
        if (transferUrls == null || transferUrls.size() == 0)
        {
            log.error("serverId:{},transferUrls is empty!", IMServerApplication.SERVER_ID);
            return;
        }

        for (String transferUrl : transferUrls)
        {
            JSONObject url = JSONObject.parseObject(transferUrl);
            String ip = url.getString("ip");
            int port = url.getInteger("port");

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
//                            pipeline.addLast("IdleStateHandler", new IdleStateHandler(11, 0, 0));
                            pipeline.addLast("MsgDecoder", new MsgDecoder());
                            pipeline.addLast("MsgEncoder", new MsgEncoder());
                            pipeline.addLast("MsgHandler", handler);
                        }
                    }).connect(ip, port)
                    .addListener((ChannelFutureListener) future -> {
                        if (!future.isSuccess())
                        {
                            throw new Exception("[connector] connect to transfer failed! transfer url: " + transferUrl);
                        } else
                        {
                            log.info("connect transfer success,address:{}", transferUrl);
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
