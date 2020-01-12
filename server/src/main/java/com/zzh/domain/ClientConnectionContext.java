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
 * @Description: 保存客户端连接容器，建立userId->clientId,clientId-> ClientConnection 的映射
 * @Author: Administrator
 * @Date: 2019/12/14 15:04
 */
@Component
public class ClientConnectionContext
{
    private static final Logger logger = LoggerFactory.getLogger(ClientConnectionContext.class);

    private final ConcurrentHashMap<String, String> userIdToNetId = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ClientConnection> netIdToClientConnection = new ConcurrentHashMap<>();


    public void addClientConnection(ClientConnection connection)
    {
        String userId = connection.getUserId();
        String clientId = connection.getClientId();

        logger.debug("[add conn on this machine] userId:{} , clientId:{}", userId, clientId);

        userIdToNetId.put(userId, clientId);
        netIdToClientConnection.put(clientId, connection);
    }

    public ClientConnection getClientConnectionByUserId(String userId)
    {
        String clientId = userIdToNetId.get(userId);
        if (clientId == null)
        {
            logger.debug("[get conn this machine] netId not found");
            return null;
        }

        ClientConnection clientConnection = netIdToClientConnection.get(clientId);
        if (clientConnection == null)
        {
            logger.debug("[get conn this machine] conn not found");
            userIdToNetId.remove(userId);
            return null;
        } else
        {
            logger.debug("[get conn this machine] found conn, userId:{}, clientId: {}", userId, clientId);
        }
        return clientConnection;
    }

    public ClientConnection getClientConnectionByNetId(Long netId)
    {
        ClientConnection clientConnection = netIdToClientConnection.get(netId);
        if (clientConnection == null)
        {
            logger.debug("[get conn this machine] conn not found");
            return null;
        }
        return clientConnection;
    }

    public void removeClientConnection(ChannelHandlerContext ctx)
    {
        Long netId = ctx.channel().attr(NettyAttrUtil.NET_ID).get();
        String userId = ctx.channel().attr(NettyAttrUtil.USER_ID).get();

        userIdToNetId.remove(userId);
        netIdToClientConnection.remove(netId);
    }

    /**
     * 根据userId检查目标channel是否在本机
     * @param userId
     * @return
     */
    public boolean onThisMachine(String userId)
    {
        return userIdToNetId.containsKey(userId);
    }

}