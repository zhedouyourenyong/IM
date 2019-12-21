package com.zzh.server.handler.service;

import com.zzh.protobuf.Msg;
import com.zzh.server.handler.ServerHandlerInterface;
import com.zzh.util.IdUtil;
import com.zzh.util.NettyAttrUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(一句话描述该类的功能)
 * @Author: Administrator
 * @Date: 2019/12/10 11:13
 */
@Service
public class HeartService implements ServerHandlerInterface
{
    private static final Logger logger = LoggerFactory.getLogger(HeartService.class);


    @Override
    public void process(ChannelHandlerContext ctx, Msg.Protocol msg) throws Exception
    {
        Channel channel = ctx.channel();
        NettyAttrUtil.updateReaderTime(channel, System.currentTimeMillis());
        Msg.Protocol heart = getPongHeart();
        channel.writeAndFlush(heart).addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess())
            {
                logger.error("send pongHeart fail,close Channel");
                future.channel().close();
            }
        });
    }

    @Override
    public Msg.MsgType getMsgType()
    {
        return Msg.MsgType.HEART;
    }

    private Msg.Protocol getPongHeart()
    {
        Msg.Head head = Msg.Head.newBuilder()
                .setFromModule(Msg.Module.SERVER)
                .setDestModule(Msg.Module.CLIENT)
                .setMsgId(IdUtil.snowGenId())
                .setTimeStamp(System.currentTimeMillis())
                .setMsgType(Msg.MsgType.HEART)
                .setVersion(1)
                .build();
        Msg.Protocol heart = Msg.Protocol.newBuilder()
                .setMsgHead(head)
                .setMsgBody("pong")
                .build();
        return heart;
    }

}
