package com.zzh.handler.service;

import com.zzh.protobuf.Msg;
import com.zzh.util.IdUtil;
import com.zzh.util.NettyAttrUtil;
import com.zzh.util.SpringBeanFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.RuntimeUtil;

import javax.xml.crypto.Data;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(一句话描述该类的功能)
 * @Author: Administrator
 * @Date: 2019/12/10 11:13
 */
@Service
public class HeartService extends AbstractMsgHandler
{
    private static final Logger logger = LoggerFactory.getLogger(HeartService.class);

    @Override
    public void checkMsg(Msg.Protocol msg)
    {
        if (msg.getMsgHead().getMsgType() != Msg.MsgType.HEART)
        {
            throw new RuntimeException("Message type validation failed");
        }
    }

    @Override
    protected void processMsg(Channel channel, Msg.Protocol msg)
    {
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

    private Msg.Protocol getPongHeart()
    {
        Msg.Head head= Msg.Head.newBuilder()
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
