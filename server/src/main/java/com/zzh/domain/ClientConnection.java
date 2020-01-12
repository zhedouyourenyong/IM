package com.zzh.domain;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 对客户端channel的封装, 并将netId设置在channel的map中.
 * netId在一台服务器上是唯一的
 * @Author: Administrator
 * @Date: 2019/12/14 14:53
 */
@Data
public class ClientConnection
{
    private String userId;
    private ChannelHandlerContext ctx;
    private String clientId;

    public ClientConnection(String userId, String clientId, ChannelHandlerContext ctx)
    {
        this.clientId = clientId;
        this.userId = userId;
        this.ctx = ctx;
    }
}
