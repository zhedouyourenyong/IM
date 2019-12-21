package com.zzh.client.handler;

import com.zzh.protobuf.Msg;
import io.netty.channel.ChannelHandlerContext;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(一句话描述该类的功能)
 * @Author: Administrator
 * @Date: 2019/12/21 21:28
 */
public interface TransferHandlerInterface
{
    /**
     * 消息处理
     *
     * @param channel
     * @param msg
     */
    void process(ChannelHandlerContext channel, Msg.Protocol msg) throws Exception;

    /**
     * 返回要处理的消息的类型
     *
     * @return
     */
    Msg.MsgType getMsgType();
}
