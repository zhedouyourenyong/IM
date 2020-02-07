package com.zzh.domain;

import com.zzh.util.NettyAttrUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2019/12/14 15:04
 */
@Slf4j
@Component
public class ServerTransferConnContext
{
    private final ConcurrentHashMap<String, ServerTransferConn> serverIdToServer = new ConcurrentHashMap<>();


    public void addConnection(ServerTransferConn connection)
    {
        String serverId = connection.getServerId();

        log.debug("[add conn on this machine] serverId:{}", serverId);

        serverIdToServer.put(serverId, connection);
    }


    public ServerTransferConn getConnectionByServerId(String netId)
    {
        ServerTransferConn serverTransferConn = serverIdToServer.get(netId);
        if (serverTransferConn == null)
        {
            log.debug("[get conn this machine] conn not found");
            return null;
        }
        return serverTransferConn;
    }

    public void removeConnection(ChannelHandlerContext ctx)
    {
        String serverId = ctx.channel().attr(NettyAttrUtil.SERVER_ID).get();
        serverIdToServer.remove(serverId);
    }

}
