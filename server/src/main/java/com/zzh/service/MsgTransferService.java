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

    public void sendSingleMsgToClient(Single.SingleMsg msg)
    {
        if (connContext.onThisMachine(msg.getDestId()))
        {
            ClientConnection destConn = connContext.getClientConnectionByUserId(msg.getDestId());
            ServerAckWindow.offer(destConn.getClientId(), msg.getId(), msg, message -> {
                Channel channel = destConn.getCtx().channel();
                if (channel.isActive() && channel.isWritable())
                {
                    channel.writeAndFlush(message).addListener(future -> {
                        if (future.isSuccess())
                        {
                            log.info("send msg to client success,msg:{}", msg.toString());
                        }
                    });
                }
            });
        }
    }

    public void sendAckToClient(Ack.AckMsg msg)
    {
        if (connContext.onThisMachine(msg.getDestId()))
        {
            ClientConnection destConn = connContext.getClientConnectionByUserId(msg.getDestId());
            destConn.getCtx().writeAndFlush(msg).addListener(future -> {
                if (future.isSuccess())
                {
                    log.info("send ack to client,msg:{}", msg.toString());
                }
            });
        }
    }

    public void sendAckMsgToClientOrTransfer(Ack.AckMsg msg)
    {
        if (connContext.onThisMachine(msg.getDestId()))
        {
            sendAckToClient(msg);
        } else
        {
            sendMsgToTransfer(msg);
        }
    }

    public void sendMsgToTransfer(Message msg)
    {
        ChannelHandlerContext ctx = ServerTransferHandler.getOneOfTransferCtx(System.currentTimeMillis());
        Channel channel = ctx.channel();
        if (channel.isActive() && channel.isWritable())
        {
            channel.writeAndFlush(msg).addListener(future -> {
                if (future.isSuccess())
                {
                    log.info("send msg to transfer success,msg:{}", msg.toString());
                }
            });
        }
    }


//    public boolean sendMsg(Long msgId, String destId, Message msg)
//    {
//        ClientConnection destConn = connContext.getClientConnectionByUserId(destId);
//        if (destConn == null)
//        {
//            ChannelHandlerContext ctx = ServerTransferHandler.getOneOfTransferCtx(System.currentTimeMillis());
//            Channel channel = ctx.channel();
//            if (channel.isActive() && channel.isWritable())
//            {
//                channel.writeAndFlush(msg).addListener(future -> {
//                    if (future.isSuccess())
//                    {
//                        log.info("send msg to transfer success,msg:{}", msg.toString());
//                    }
//                });
//            }
//            return false;
//        } else
//        {
//            ServerAckWindow.offer(destConn.getClientId(), msgId, msg, message -> {
//                Channel channel = destConn.getCtx().channel();
//                if (channel.isActive() && channel.isWritable())
//                {
//                    channel.writeAndFlush(message).addListener(future -> {
//                        if (future.isSuccess())
//                        {
//                            log.info("send msg to client success,msg:{}", msg.toString());
//                        }
//                    });
//                }
//            });
//            return true;
//        }
//    }


}