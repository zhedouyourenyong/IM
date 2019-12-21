package com.zzh.server.handler.service;

import com.zzh.protobuf.Msg;
import com.zzh.server.handler.ServerHandlerInterface;
import io.netty.channel.ChannelHandlerContext;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(一句话描述该类的功能)
 * @Author: Administrator
 * @Date: 2019/12/10 11:15
 */
public class GroupMsgService implements ServerHandlerInterface
{


    @Override
    public void process(ChannelHandlerContext ctx, Msg.Protocol msg) throws Exception
    {

    }

    @Override
    public Msg.MsgType getMsgType()
    {
        return Msg.MsgType.GROUP;
    }
}
