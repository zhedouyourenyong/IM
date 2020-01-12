package com.zzh.service;

import com.zzh.IMServerApplication;
import com.zzh.client.ServerTransferHandler;
import com.zzh.domain.ClientConnection;
import com.zzh.domain.ClientConnectionContext;
import com.zzh.protobuf.Protocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 负责msg和ack的发送,目的地是client或transfer。
 */
@Service
public class MsgTransferService
{
    private static final Logger logger = LoggerFactory.getLogger(MsgTransferService.class);

    @Autowired
    private ClientConnectionContext connContext;

    /**
     * 检查目标连接是否在本服务器，如果是交给ackWin，如果不是交给transfer转发
     *
     * @param destUserId
     * @param msg
     */
    public void sendMsgToClientOrTransfer(String destUserId, Protocol.Msg msg)
    {
        if (connContext.onThisMachine(destUserId))
        {
            sendMsgToClient(msg);
        } else
        {
            sendMsgToTransfer(msg);
        }
    }

    public void sendMsgToClient(Protocol.Msg msg)
    {
        ClientConnection conn = connContext.getClientConnectionByUserId(msg.getDestId());
        if (conn == null)
        {
            logger.error("destChannel not one the machine, msgId:{},serverId:{}",
                    msg.getId(), IMServerApplication.SERVER_ID);
            return;
        }
        ServerAckWindow.offer(conn.getClientId(), msg.getId(), msg, message -> {
            Channel channel = conn.getCtx().channel();
            if (channel.isActive() && channel.isWritable())
            {
                channel.writeAndFlush(message).addListener(future -> {
                    if (future.isSuccess())
                    {
                        logger.info("send msg to client is success!  msgId:{},clientId:{}", msg.getId(), conn.getClientId());
                    } else
                    {
                        logger.error("send msg to client is fail!  msgId:{},clientId:{}", msg.getId(), conn.getClientId());
                    }
                });
            } else
            {
                logger.error("clientId:{},This channel is not available. Push down message failed!", conn.getClientId());
            }
        });
    }

    public void sendMsgToTransfer(Protocol.Msg msg)
    {
        ChannelHandlerContext ctx = ServerTransferHandler.getOneOfTransferCtx(System.currentTimeMillis());
        Channel channel = ctx.channel();
        if (channel.isActive() && channel.isWritable())
        {
            channel.writeAndFlush(msg).addListener(future -> {
                if (future.isSuccess())
                {
                    logger.info("send msg to transfer is success!  msgId:{}", msg.getId());
                } else
                {
                    logger.error("send msg to transfer is fail!  msgId:{}", msg.getId());
                }
            });
        }
    }
}