package com.zzh.handler.service;

import com.zzh.protobuf.Protocol;
import com.zzh.handler.ServerHandlerInterface;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2019/12/10 11:15
 */
@Service
public class GroupMsgService implements ServerHandlerInterface
{


    @Override
    public void process(ChannelHandlerContext ctx, Protocol.Msg msg) throws Exception
    {

    }

    @Override
    public Protocol.MsgType getMsgType()
    {
        return Protocol.MsgType.GROUP;
    }
}
