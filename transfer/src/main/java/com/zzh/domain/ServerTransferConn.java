package com.zzh.domain;

import com.zzh.util.NettyAttrUtil;
import io.netty.channel.ChannelHandlerContext;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: serverId标识这条channel属于哪个server
 * @Author: Administrator
 * @Date: 2019/12/21 23:15
 */
public class ServerTransferConn
{

    private ChannelHandlerContext ctx;
    private String serverId;

    public ServerTransferConn(String serverId, ChannelHandlerContext ctx)
    {
        this.ctx = ctx;
        this.serverId = serverId;
        ctx.channel().attr(NettyAttrUtil.SERVER_ID).set(this.serverId);
    }


    public ChannelHandlerContext getCtx()
    {
        return ctx;
    }

    public String getServerId()
    {
        return serverId;
    }
}
