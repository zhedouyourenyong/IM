package com.zzh.service;

import com.google.protobuf.Message;
import com.zzh.domain.ServerAckWindow;
import com.zzh.protocol.Ack;
import com.zzh.protocol.Single;
import com.zzh.transfer.ServerTransferHandler;
import com.zzh.domain.ClientConnection;
import com.zzh.domain.ClientConnectionContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 负责msg和ack的发送,目的地是client或transfer。
 */
@Slf4j
@Service
public class MsgTransferService
{
    private ClientConnectionContext connContext;

    @Autowired
    public MsgTransferService(ClientConnectionContext connContext)
    {
        this.connContext = connContext;
    }

    public void sendSingleMsgToClientOrTransfer(Single.SingleMsg msg)
    {
        sendMsg(msg.getId(), msg.getDestId(), msg);
    }

    public void sendAckMsgToClientOrTransfer(Ack.AckMsg msg)
    {
        sendMsg(msg.getId(), msg.getDestId(), msg);
    }


    public boolean sendMsg(Long msgId, String destId, Message msg)
    {
        ClientConnection destConn = connContext.getClientConnectionByUserId(destId);
        if (destConn == null)
        {
            ChannelHandlerContext ctx = ServerTransferHandler.getOneOfTransferCtx(System.currentTimeMillis());
            Channel channel = ctx.channel();
            if (channel.isActive() && channel.isWritable())
            {
                channel.writeAndFlush(msg);
            }
            return false;
        } else
        {
            ServerAckWindow.offer(destConn.getClientId(), msgId, msg, message -> {
                Channel channel = destConn.getCtx().channel();
                if (channel.isActive() && channel.isWritable())
                {
                    channel.writeAndFlush(message);
                }
            });
            return true;
        }
    }
}