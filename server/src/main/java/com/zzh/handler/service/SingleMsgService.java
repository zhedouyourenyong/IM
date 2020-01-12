package com.zzh.handler.service;

import com.zzh.IMServerApplication;
import com.zzh.constant.RedisKey;
import com.zzh.domain.ClientConnectionContext;
import com.zzh.protobuf.Protocol;
import com.zzh.handler.ServerHandlerInterface;
import com.zzh.service.MsgTransferService;
import com.zzh.util.IdUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Service
public class SingleMsgService implements ServerHandlerInterface
{
    private static final Logger logger = LoggerFactory.getLogger(SingleMsgService.class);

    @Autowired
    private MsgTransferService msgTransferService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ClientConnectionContext connContext;


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
    public void process(ChannelHandlerContext ctx, Protocol.Msg msg) throws Exception
    {
        if (msg.getFromModule() == Protocol.Module.CLIENT)
        {
            if (connContext.onThisMachine(msg.getDestId()))
            {
                sendMsgToClient(ctx, msg, Protocol.Module.CLIENT);
            } else
            {
                msgTransferService.sendMsgToTransfer(msg);
            }
        } else if (msg.getFromModule() == Protocol.Module.TRANSFER)
        {
            sendMsgToClient(ctx, msg, Protocol.Module.TRANSFER);
        }
    }

    /**
     * @param ctx
     * @param msg
     * @param destModule 消费已经被消费时使用，用于构造ack
     */
    private void sendMsgToClient(ChannelHandlerContext ctx, Protocol.Msg msg, Protocol.Module destModule)
    {
        if (checkMsgConsumeStatus(msg.getId()))
        {
            doSendAck(ctx.channel(), msg, Protocol.Module.CLIENT);
        } else
        {
            msgTransferService.sendMsgToClient(msg);
        }
    }

    /**
     * 检查消息是否被消费
     *
     * @return true:已被消费   false：未被消费
     */
    private Boolean checkMsgConsumeStatus(String msgId)
    {
        String key = RedisKey.CONSUMED_MESSAGE + IMServerApplication.SERVER_ID;
        Boolean result = redisTemplate.opsForSet().isMember(key, msgId);
        if (result == null)
        {
            logger.error("checkMsgConsumeStatus fail,msgId:{},redisKey:{}", msgId, key);
        }
        return result;
    }

    private void doSendAck(Channel channel, Protocol.Msg msg, Protocol.Module destModule)
    {
        if (channel.isActive() && channel.isWritable())
        {
            channel.writeAndFlush(getAck(msg, destModule));
        }
    }

    private Protocol.Msg getAck(Protocol.Msg msg, Protocol.Module destModule)
    {
        Protocol.Msg ack = Protocol.Msg.newBuilder()
                .setId(String.valueOf(IdUtil.snowGenId()))
                .setMsgType(Protocol.MsgType.ACK)
                .setTimeStamp(String.valueOf(System.currentTimeMillis()))
                .setFromModule(Protocol.Module.SERVER)
                .setDestModule(destModule)
                .setDestId(msg.getFromId())
                .setMsgBody(msg.getMsgBody())
                .build();
        return ack;
    }


    @Override
    public Protocol.MsgType getMsgType()
    {
        return Protocol.MsgType.SINGLE;
    }
}
