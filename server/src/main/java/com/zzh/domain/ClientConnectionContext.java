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
 * @Description: 保存客户端连接容器，建立userId->clientId,clientId-> ClientConnection 的映射
 * @Author: Administrator
 * @Date: 2019/12/14 15:04
 */
@Slf4j
@Component
public class ClientConnectionContext
{
    private final ConcurrentHashMap<String, String> userIdToNetId = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ClientConnection> netIdToClientConnection = new ConcurrentHashMap<>();


    public void addClientConnection(ClientConnection connection)
    {
        String userId = connection.getUserId();
        String clientId = connection.getClientId();

        log.debug("[add conn on this machine] userId:{} , clientId:{}", userId, clientId);

        userIdToNetId.put(userId, clientId);
        netIdToClientConnection.put(clientId, connection);
    }

    public ClientConnection getClientConnectionByUserId(String userId)
    {
        String clientId = userIdToNetId.get(userId);
        if (clientId == null)
        {
            log.debug("[get conn this machine] netId not found");
            return null;
        }

        ClientConnection clientConnection = netIdToClientConnection.get(clientId);
        if (clientConnection == null)
        {
            log.debug("[get conn this machine] conn not found");
            userIdToNetId.remove(userId);
            return null;
        } else
        {
            log.debug("[get conn this machine] found conn, userId:{}, clientId: {}", userId, clientId);
        }
        return clientConnection;
    }

    public ClientConnection getClientConnectionByClientId(String clientId)
    {
        ClientConnection clientConnection = netIdToClientConnection.get(clientId);
        if (clientConnection == null)
        {
            log.debug("[get conn this machine] conn not found");
            return null;
        }
        return clientConnection;
    }

    public void removeClientConnection(ChannelHandlerContext ctx)
    {
        String clientId = ctx.channel().attr(NettyAttrUtil.CLIENT_ID).get();
        String userId = ctx.channel().attr(NettyAttrUtil.USER_ID).get();

        userIdToNetId.remove(userId);
        netIdToClientConnection.remove(clientId);
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
