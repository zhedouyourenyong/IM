package com.zzh.handler.service;

import com.zzh.protobuf.Msg;
import io.netty.channel.Channel;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(消息检查和消息处理)
 * @Author: Administrator
 * @Date: 2019/12/10 10:57
 */
public abstract class AbstractMsgHandler implements MsgHandlerInterface
{

    @Override
    public void process(Channel channel, Msg.Protocol msg)
    {
        checkMsg(msg);
        processMsg(channel, msg);
    }

    abstract public void checkMsg(Msg.Protocol msg);

    protected abstract void processMsg(Channel channel, Msg.Protocol msg);
}
