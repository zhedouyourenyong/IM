package com.zzh.client.handler.service;

import com.zzh.client.handler.TransferHandlerInterface;
import com.zzh.protobuf.Msg;
import io.netty.channel.ChannelHandlerContext;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(一句话描述该类的功能)
 * @Author: Administrator
 * @Date: 2019/12/21 21:45
 */
public class SingleMsgTransferService implements TransferHandlerInterface
{
    @Override
    public void process(ChannelHandlerContext channel, Msg.Protocol msg) throws Exception
    {

    }

    @Override
    public Msg.MsgType getMsgType()
    {
        return Msg.MsgType.SINGLE;
    }
}
