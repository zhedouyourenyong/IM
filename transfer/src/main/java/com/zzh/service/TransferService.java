package com.zzh.service;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.Message;
import com.zzh.constant.RedisKey;
import com.zzh.domain.ServerTransferConn;
import com.zzh.domain.ServerTransferConnContext;
import com.zzh.pojo.RouteInfo;
import com.zzh.protocol.Ack;
import com.zzh.protocol.Internal;
import com.zzh.protocol.Single;
import com.zzh.util.IdUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2019/12/21 23:02
 */
@Slf4j
@Service
public class TransferService
{
    @Autowired
    private ServerTransferConnContext context;
    @Autowired
    private StringRedisTemplate redisTemplate;


    public void doChat(Single.SingleMsg msg) throws IOException
    {
        ServerTransferConn conn = getConnection(msg.getDestId());

        if (conn != null)
        {
            conn.getCtx().writeAndFlush(msg);
        } else
        {
            doOffline(msg);
        }
    }

    public void doSendAck(Ack.AckMsg msg) throws IOException
    {
        ServerTransferConn conn = getConnection(msg.getDestId());

        if (conn != null)
        {
            conn.getCtx().writeAndFlush(msg);
        } else
        {
            doOffline(msg);
        }
    }

    public void doGreet(Internal.InternalMsg msg, ChannelHandlerContext ctx)
    {
        String serverId = msg.getBody();
        ServerTransferConn conn = new ServerTransferConn(serverId, ctx);
        context.addConnection(conn);

        log.info("serverId:{} 上线!",serverId);

        //todo greet 回应
//        ctx.writeAndFlush(getInternalAck(msg.getId()));
    }

    private ServerTransferConn getConnection(String destId)
    {
        String routeInfo = redisTemplate.opsForValue().get(RedisKey.ROUTE_PREFIX + destId);
        if (routeInfo == null)
        {
            log.error("not found routeInfo,userID:{}", destId);
            return null;
        } else
        {
            RouteInfo info = (RouteInfo) JSONObject.parse(routeInfo);
            String serverId = info.getServerId();
            ServerTransferConn conn = context.getConnectionByServerId(serverId);
            return conn;
        }
    }


    private void doOffline(Message msg) throws IOException
    {
        log.info("doOffline,msg:{}", msg.toString());
//        producer.basicPublish(ImConstant.MQ_EXCHANGE, ImConstant.MQ_ROUTING_KEY,
//                MessageProperties.PERSISTENT_TEXT_PLAIN, msg);
    }

}
