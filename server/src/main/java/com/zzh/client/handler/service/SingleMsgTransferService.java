package com.zzh.client.handler.service;

import com.zzh.client.handler.TransferHandlerInterface;
import com.zzh.protobuf.Msg;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 处理私聊消息
 * @Author: Administrator
 * @Date: 2019/12/21 21:45
 */
@Service
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
