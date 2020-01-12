package com.zzh.handler.service;

import com.zzh.IMServerApplication;
import com.zzh.constant.RedisKey;
import com.zzh.domain.ClientConnection;
import com.zzh.domain.ClientConnectionContext;
import com.zzh.protobuf.Protocol;
import com.zzh.handler.ServerHandlerInterface;
import com.zzh.service.MsgTransferService;
import com.zzh.service.ServerAckWindow;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Service
public class AckService implements ServerHandlerInterface
{
    private static final Logger logger = LoggerFactory.getLogger(AckService.class);

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MsgTransferService msgTransferService;
    @Autowired
    private ClientConnectionContext connContext;

    /**
     * 如果ack来自transfer，说明当前服务器只做中转作用，直接下发给client。
     * 如果ack来自client2，则当前服务器2的ackWin有负责该消息重推，需要将消息设置为已消费，并取消重推。
     * 然后需要将ack发送给client1，取消client1的消息重推。
     * <p>
     * 如果client2发送的ack没有到达服务器2，则client2会重推ack直到服务器2收到ack。
     * 如果服务器2发给client1的ack中途丢失，则client1会重推消息到服务器2，服务器2检查消息ID为已消费则直接再次发送ack。
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void process(ChannelHandlerContext ctx, Protocol.Msg msg) throws Exception
    {
        logger.info("receive ackMsg:{}", msg.toString());

        if (msg.getFromModule() == Protocol.Module.TRANSFER)
        {
            doSendAck(msg);
        } else if (msg.getFromModule() == Protocol.Module.CLIENT)
        {
            setMsgConsumedStatus(msg);
            msgTransferService.sendMsgToClientOrTransfer(msg.getDestId(), msg);
        }
    }

    private Long setMsgConsumedStatus(Protocol.Msg msg)
    {
        ClientConnection conn = connContext.getClientConnectionByUserId(msg.getDestId());
        ServerAckWindow.ack(conn.getClientId(),msg);

        String key = RedisKey.CONSUMED_MESSAGE + IMServerApplication.SERVER_ID;
        return redisTemplate.opsForSet().add(key, msg.getId());
    }

    private void doSendAck(Protocol.Msg msg)
    {
        msgTransferService.sendMsgToClient(msg);
    }

    @Override
    public Protocol.MsgType getMsgType()
    {
        return Protocol.MsgType.ACK;
    }
}
