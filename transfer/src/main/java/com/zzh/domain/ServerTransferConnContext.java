package com.zzh.domain;

import com.zzh.util.NettyAttrUtil;
import io.netty.channel.ChannelHandlerContext;
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
@Component
public class ServerTransferConnContext
{
    private static final Logger logger = LoggerFactory.getLogger(ServerTransferConnContext.class);

    private final ConcurrentHashMap<String, ServerTransferConn> serverIdToServer = new ConcurrentHashMap<>();


    public void addConnection(ServerTransferConn connection)
    {
        String serverId = connection.getServerId();

        logger.debug("[add conn on this machine] serverId:{}", serverId);

        serverIdToServer.put(serverId, connection);
    }


    public ServerTransferConn getConnectionByServerId(String netId)
    {
        ServerTransferConn serverTransferConn = serverIdToServer.get(netId);
        if (serverTransferConn == null)
        {
            logger.debug("[get conn this machine] conn not found");
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
