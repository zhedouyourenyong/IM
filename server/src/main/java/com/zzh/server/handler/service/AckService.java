package com.zzh.server.handler.service;

import com.zzh.protobuf.Msg;
import com.zzh.server.handler.ServerHandlerInterface;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(一句话描述该类的功能)
 * @Author: Administrator
 * @Date: 2019/12/10 11:05
 */
@Service
public class AckService implements ServerHandlerInterface
{
    private static final Logger logger = LoggerFactory.getLogger(AckService.class);

    @Override
    public void process(ChannelHandlerContext ctx, Msg.Protocol msg) throws Exception
    {

    }

    @Override
    public Msg.MsgType getMsgType()
    {
        return Msg.MsgType.ACK;
    }
}
