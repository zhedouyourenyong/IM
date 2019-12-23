package com.zzh.domain;

import com.zzh.util.NettyAttrUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 对客户端channel的封装, 并将netId设置在channel的map中.
 * netId在一台服务器上是唯一的
 * @Author: Administrator
 * @Date: 2019/12/14 14:53
 */
public class ClientConnection
{
    private static final AtomicLong NETID_GENERATOR = new AtomicLong(0);

    private String userId;
    private ChannelHandlerContext ctx;
    private Long netId;

    public ClientConnection(String userId, ChannelHandlerContext ctx)
    {
        this.userId = userId;
        this.ctx = ctx;
        this.netId = generateNetId();
        ctx.channel().attr(NettyAttrUtil.NET_ID).set(this.netId);
    }


    public String getUserId()
    {
        return userId;
    }

    public ChannelHandlerContext getCtx()
    {
        return ctx;
    }

    public Long getNetId()
    {
        return netId;
    }


    /**
     * 生成netId
     *
     * @param
     * @return
     */
    private Long generateNetId()
    {
        return NETID_GENERATOR.getAndIncrement();
    }
}
