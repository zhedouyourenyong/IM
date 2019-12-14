package com.zzh.handler.service;

import com.zzh.protobuf.Msg;
import io.netty.channel.Channel;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(一句话描述该类的功能)
 * @Author: Administrator
 * @Date: 2019/12/10 10:50
 */
public interface MsgHandlerInterface
{

    /**
     * 消息处理
     * @param channel
     * @param msg
     */
    void process(Channel channel, Msg.Protocol msg);
}
