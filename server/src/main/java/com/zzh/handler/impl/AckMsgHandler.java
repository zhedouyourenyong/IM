package com.zzh.handler.impl;

import com.google.protobuf.Message;
import com.zzh.IMServerApplication;
import com.zzh.constant.RedisKey;
import com.zzh.domain.ClientConnection;
import com.zzh.domain.ClientConnectionContext;
import com.zzh.handler.MsgHandler;
import com.zzh.protocol.Ack;
import com.zzh.service.MsgTransferService;
import com.zzh.domain.ServerAckWindow;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 处理ack信息
 * @Author: Administrator
 * @Date: 2019/12/10 11:05
 */
@Slf4j
@Service
public class AckMsgHandler implements MsgHandler
{
    private StringRedisTemplate redisTemplate;
    private MsgTransferService msgTransferService;
    private ClientConnectionContext connContext;

    @Autowired
    public AckMsgHandler(StringRedisTemplate redisTemplate, MsgTransferService msgTransferService, ClientConnectionContext connContext)
    {
        this.msgTransferService = msgTransferService;
        this.redisTemplate = redisTemplate;
        this.connContext = connContext;
    }

    /**
     * 如果ack来自transfer，说明当前服务器只做中转作用，直接下发给client。
     * 如果ack来自client2，则当前服务器2的ackWin有负责该消息重推，需要将消息设置为已消费，并取消重推。
     * 然后需要将ack发送给client1，取消client1的消息重推。
     * <p>
     * 如果client2发送的ack没有到达服务器2，则client2会重推ack直到服务器2收到ack。
     * 如果服务器2发给client1的ack中途丢失，则client1会重推消息到服务器2，服务器2检查消息ID为已消费则直接再次发送ack。
     * <p>
     * 发送消息:client1 <-->  server   <--> client2
     * 发送ACK:client2 <-->  server   <--> client1
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void process(ChannelHandlerContext ctx, Message msg) throws Exception
    {
        Ack.AckMsg message = (Ack.AckMsg) msg;
        log.info("received ackMsg:{}", message.toString());

        if (message.getFromModule() == Ack.AckMsg.Module.TRANSFER)
        {
            sendAckToClient(message);
        } else if (message.getFromModule() == Ack.AckMsg.Module.CLIENT)
        {
            cancelMsgRePush(message);
            setMsgToConsumed(message);
            sendAckToClientOrTransfer(message);
        }
    }

    private void sendAckToClient(Ack.AckMsg msg)
    {
        msgTransferService.sendAckToClient(msg);
    }

    /**
     * msg:from->server->to
     * ack:to->server->from
     * server要取消对to的重推
     * 然后将消息发送给from
     */
    private void cancelMsgRePush(Ack.AckMsg msg)
    {
        //获取to的id
        ClientConnection conn = connContext.getClientConnectionByUserId(msg.getFromId());
        ServerAckWindow.ack(conn.getClientId(), msg);
    }

    private void setMsgToConsumed(Ack.AckMsg msg)
    {
        try
        {
            String key = RedisKey.CONSUMED_MESSAGE + IMServerApplication.SERVER_ID;
            redisTemplate.opsForSet().add(key, String.valueOf(msg.getAckMsgId()));
            log.info("add msgId：{} to redis consumed set", msg.getAckMsgId());
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    private void sendAckToClientOrTransfer(Ack.AckMsg msg)
    {
        msgTransferService.sendAckMsgToClientOrTransfer(msg);
    }

    @Override
    public Class<?> getProcessedType()
    {
        return Ack.AckMsg.class;
    }
}
