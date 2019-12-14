package com.zzh.handler.service;

import com.zzh.protobuf.Msg;
import com.zzh.service.MsgTransferHandler;
import io.netty.channel.Channel;
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
public class SingleMsgService extends AbstractMsgHandler
{
    @Autowired
    MsgTransferHandler msgTransferHandler;

    @Override
    public void checkMsg(Msg.Protocol msg)
    {
        if (msg.getMsgHead().getMsgType() != Msg.MsgType.SINGLE)
        {
            throw new RuntimeException("Message type validation failed");
        }
    }

    /**
     * 先做幂等性检查，再判断目标channel是否在本机器上
     * @param channel
     * @param msg
     */
    @Override
    protected void processMsg(Channel channel, Msg.Protocol msg)
    {
        msgTransferHandler.sendSingleMsg(msg,channel);
    }
}
