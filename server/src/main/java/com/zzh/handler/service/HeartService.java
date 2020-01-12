package com.zzh.handler.service;

import com.zzh.protobuf.Protocol;
import com.zzh.handler.ServerHandlerInterface;
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
 * @Description:
 * @Author: Administrator
 * @Date: 2019/12/10 11:13
 */
@Service
public class HeartService implements ServerHandlerInterface
{
    private static final Logger logger = LoggerFactory.getLogger(HeartService.class);


    @Override
    public void process(ChannelHandlerContext ctx, Protocol.Msg msg) throws Exception
    {
        Channel channel = ctx.channel();
        NettyAttrUtil.updateReaderTime(channel, System.currentTimeMillis());
        Protocol.Msg heart = getPongHeart();
        channel.writeAndFlush(heart).addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess())
            {
                logger.error("send pongHeart fail,close Channel");
                future.channel().close();
            }
        });
    }

    @Override
    public Protocol.MsgType getMsgType()
    {
        return Protocol.MsgType.HEART;
    }

    /**
     * todo 如何按照客户端区分心跳
     * @return
     */
    private Protocol.Msg getPongHeart()
    {
        Protocol.Msg pong= Protocol.Msg.newBuilder()
                .setId(String.valueOf(IdUtil.snowGenId()))
                .setMsgType(Protocol.MsgType.HEART)
                .setTimeStamp(String.valueOf(System.currentTimeMillis()))
                .build();
        return pong;
    }

}
