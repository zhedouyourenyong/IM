package com.zzh.service;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.Message;
import com.zzh.constant.RedisKey;
import com.zzh.domain.ServerTransferConn;
import com.zzh.domain.ServerTransferConnContext;
import com.zzh.pojo.RouteInfo;
import com.zzh.protobuf.Protocol;
import com.zzh.util.IdUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Service
public class TransferService
{
    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    @Autowired
    private ServerTransferConnContext context;
    @Autowired
    private StringRedisTemplate redisTemplate;


    public void doChat(Protocol.Msg msg) throws IOException
    {
        ServerTransferConn conn = getConnection(msg);

        if (conn != null)
        {
            conn.getCtx().writeAndFlush(msg);
        } else
        {
            doOffline(msg);
        }
    }

    public void doSendAck(Protocol.Msg msg) throws IOException
    {
        ServerTransferConn conn = getConnection(msg);

        if (conn != null)
        {
            conn.getCtx().writeAndFlush(msg);
        } else
        {
            doOffline(msg);
        }
    }

    public void doGreet(Protocol.Msg msg, ChannelHandlerContext ctx)
    {
        String serverId = msg.getMsgBody();
        ServerTransferConn conn = new ServerTransferConn(serverId, ctx);
        context.addConnection(conn);

        ctx.writeAndFlush(getInternalAck(msg.getId()));
    }

    private ServerTransferConn getConnection(Protocol.Msg msg)
    {
        String routeInfo = redisTemplate.opsForValue().get(RedisKey.ROUTE_PREFIX + msg.getDestId());
        if (routeInfo == null)
        {
            logger.error("not found routeInfo,userID:{}", msg.getDestId());
            return null;
        } else
        {
            RouteInfo info = (RouteInfo) JSONObject.parse(routeInfo);
            String serverId = info.getServerId();
            ServerTransferConn conn = context.getConnectionByServerId(serverId);
            return conn;
        }
    }


    private Protocol.Msg getInternalAck(String msgId)
    {
        return Protocol.Msg.newBuilder()
                .setId(String.valueOf(IdUtil.snowGenId()))
                .setFromModule(Protocol.Module.TRANSFER)
                .setDestModule(Protocol.Module.SERVER)
                .setMsgType(Protocol.MsgType.ACK)
                .setTimeStamp(String.valueOf(System.currentTimeMillis()))
                .setMsgBody(msgId.toString())
                .build();
    }

    private void doOffline(Message msg) throws IOException
    {
        logger.info("doOffline,msg:{}", msg.toString());
//        producer.basicPublish(ImConstant.MQ_EXCHANGE, ImConstant.MQ_ROUTING_KEY,
//                MessageProperties.PERSISTENT_TEXT_PLAIN, msg);
    }

}
