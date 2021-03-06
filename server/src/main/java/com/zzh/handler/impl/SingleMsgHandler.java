package com.zzh.handler.impl;

import com.google.protobuf.Message;
import com.zzh.IMServerApplication;
import com.zzh.constant.RedisKey;
import com.zzh.domain.ClientConnectionContext;
import com.zzh.handler.MsgHandler;
import com.zzh.protocol.Ack;
import com.zzh.protocol.Single;
import com.zzh.service.MsgTransferService;
import com.zzh.util.IdUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 私聊消息的发送
 * @Author: Administrator
 * @Date: 2019/12/10 11:15
 */
@Slf4j
@Service
public class SingleMsgHandler implements MsgHandler
{
    private MsgTransferService msgTransferService;
    private StringRedisTemplate redisTemplate;
    private ClientConnectionContext connContext;

    @Autowired
    public SingleMsgHandler(ClientConnectionContext connContext, StringRedisTemplate redisTemplate, MsgTransferService msgTransferService)
    {
        this.msgTransferService = msgTransferService;
        this.redisTemplate = redisTemplate;
        this.connContext = connContext;
    }


    /**
     * msg来自client：
     * 首先检查目标channel是否在本机
     * 不在:发送给transfer
     * 在: 处理消息，下发给客户端
     * <p>
     * msg来自transfer:
     * 目标channel一定在本机，处理消息，下发给客户端
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void process(ChannelHandlerContext ctx, Message msg)
    {
        try
        {
            log.info("received single msg:{}", msg.toString());
            Single.SingleMsg message = (Single.SingleMsg) msg;
            if (message.getFromModule() == Single.SingleMsg.Module.CLIENT)
            {
                if (connContext.onThisMachine(message.getDestId()))
                {
                    sendMsgToClient(ctx.channel(), message);
                } else
                {
                    sendMsgToTransfer(message);
                }
            } else if (message.getFromModule() == Single.SingleMsg.Module.TRANSFER)
            {
                sendMsgToClient(ctx.channel(), message);
            }

        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    private void sendMsgToTransfer(Single.SingleMsg msg)
    {
        msgTransferService.sendMsgToTransfer(msg);
    }

    /**
     * todo 通过AOP做幂等性检查
     * 首先要检查该消息是否已经发送过ack，如果已经发送过但又再次收到同一个消息，说明ack丢失，重发ack。
     * 之后由ServerAckWin会判断该消息是否正在处理，并作出解决。
     */
    private void sendMsgToClient(Channel channel, Single.SingleMsg msg)
    {
        try
        {
            if (checkMsgConsumeStatus(String.valueOf(msg.getId())))
            {
                log.info("received repeat msg,msgId:{}", msg.getId());
                doSendAck(channel, msg);
            } else
            {
                msgTransferService.sendSingleMsgToClient(msg);
            }
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 根据消息ID检查消息是否被消费
     *
     * @return true:已被消费   false：未被消费
     */
    private Boolean checkMsgConsumeStatus(String msgId)
    {
        String key = RedisKey.CONSUMED_MESSAGE + IMServerApplication.SERVER_ID;
        Boolean result = redisTemplate.opsForSet().isMember(key, msgId);
        if (result == null)
        {
            log.error("checkMsgConsumeStatus fail,msgId:{},redisKey:{}", msgId, key);
        }
        return result;
    }

    private void doSendAck(Channel channel, Single.SingleMsg msg)
    {
        if (channel.isOpen() && channel.isActive() && channel.isWritable())
        {
            channel.writeAndFlush(getAck(msg.getDestId(), msg.getFromId(), msg.getId(), msg.getSessionId())).addListener(future -> {
                if (future.isSuccess())
                {
                    log.info("repeat msg resend ack success,msgId:{},sessionId:{}", msg.getId(), msg.getSessionId());
                }
            });
        }
    }

    private Ack.AckMsg getAck(String fromId, String destId, Long ackMsgId, Long ackMsgSessionId)
    {
        return Ack.AckMsg.newBuilder()
                .setId(IdUtil.snowGenId())
                .setTimeStamp(System.currentTimeMillis())
                .setFromModule(Ack.AckMsg.Module.SERVER)
                .setFromId(fromId)
                .setDestId(destId)
                .setAckMsgId(ackMsgId)
                .setAckMsgSessionId(ackMsgSessionId)
                .build();
    }


    @Override
    public Class<?> getProcessedType()
    {
        return Single.SingleMsg.class;
    }
}
