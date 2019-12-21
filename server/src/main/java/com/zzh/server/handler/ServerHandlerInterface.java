package com.zzh.server.handler;

import com.zzh.protobuf.Msg;
import io.netty.channel.ChannelHandlerContext;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2019/12/10 10:50
 */
public interface ServerHandlerInterface
{

    /**
     * 消息处理
     *
     * @param ctx
     * @param msg
     */
    void process(ChannelHandlerContext ctx, Msg.Protocol msg) throws Exception;

    /**
     * 返回要处理的消息的类型
     *
     * @return
     */
    Msg.MsgType getMsgType();
}
