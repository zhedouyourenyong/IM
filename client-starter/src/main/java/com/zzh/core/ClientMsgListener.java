package com.zzh.core;

import com.zzh.protocol.Single;
import io.netty.channel.ChannelHandlerContext;

/**
 * Date: 2019-05-18
 * Time: 23:46
 *
 * @author yrw
 */
public interface ClientMsgListener
{

    /**
     * do when the client connect to connector successfully
     */
    void online();

    /**
     * read a msg
     *
     * @param chatMsg
     */
    void read(Single.SingleMsg chatMsg);

    /**
     * do when the client disconnect to connector
     */
    void offline();

    /**
     * a exception is occurred
     *
     * @param ctx
     * @param cause
     */
    void hasException(ChannelHandlerContext ctx, Throwable cause);
}