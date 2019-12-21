package com.zzh.server.handler.service;

import com.zzh.protobuf.Msg;
import com.zzh.server.handler.ServerHandlerInterface;
import com.zzh.service.MsgTransferHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(处理私聊数据)
 * @Author: Administrator
 * @Date: 2019/12/10 11:15
 */
@Service
public class SingleMsgService implements ServerHandlerInterface
{
    @Autowired
    private MsgTransferHandler msgTransferHandler;


    @Override
    public void process(ChannelHandlerContext ctx, Msg.Protocol msg) throws Exception
    {
        msgTransferHandler.sendMsgToClientOrTransfer(msg);
    }

    @Override
    public Msg.MsgType getMsgType()
    {
        return Msg.MsgType.SINGLE;
    }
}
