package com.zzh.server.handler.service;

import com.zzh.protobuf.Msg;
import com.zzh.server.handler.ServerHandlerInterface;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(一句话描述该类的功能)
 * @Author: Administrator
 * @Date: 2019/12/14 16:20
 */
@Component
public class OfflineService implements ServerHandlerInterface
{

    @Override
    public void process(ChannelHandlerContext ctx, Msg.Protocol msg) throws Exception
    {

    }

    @Override
    public Msg.MsgType getMsgType()
    {
        return Msg.MsgType.OFFLINE;
    }
}
