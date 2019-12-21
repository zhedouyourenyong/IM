package com.zzh.service;


import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.Message;
import com.zzh.client.handler.ServerTransferHandler;
import com.zzh.domain.ClientConnection;
import com.zzh.domain.ClientConnectionContext;
import com.zzh.protobuf.Msg;
import com.zzh.util.IdUtil;
import com.zzh.util.NettyAttrUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * 负责server与transfer之间的联系
 */
@Service
public class MsgTransferHandler
{
    private static final Logger logger = LoggerFactory.getLogger(MsgTransferHandler.class);

    @Autowired
    private ClientConnectionContext clientConnectionContext;


    /**
     * 使用这个方法需要确认目标channel一定在本服务器，一般由server受到transfer的消息时使用。
     *
     * @param msg
     */
    public void sendMsgToClient(Msg.Protocol msg)
    {
        ClientConnection conn = clientConnectionContext.getClientConnectionByUserId(msg.getMsgHead().getDestId());
        if (conn == null)
        {
            logger.error("[send chat to client] not one the machine, userId: {}, connectorId: {}",
                    msg.getMsgHead().getDestId(), ServerTransferHandler.CONNECTOR_ID);
            return;
        }
        //change msg id
        Msg.Head head = Msg.Head.newBuilder().mergeFrom(msg.getMsgHead()).setMsgId(IdUtil.nextId(conn.getNetId())).build();
        Msg.Protocol copy = Msg.Protocol.newBuilder().mergeFrom(msg)
                .setMsgHead(head).build();

        conn.getCtx().writeAndFlush(copy);

        //send delivered
        sendMsg(msg.getMsgHead().getFromId(), msg.getMsgHead().getMsgId(), cid -> getDelivered(cid, msg));
    }


    public void sendMsgToClientOrTransfer(Msg.Protocol msg)
    {
        boolean onTheMachine = sendMsg(msg.getMsgHead().getDestId(), msg.getMsgHead().getMsgId(), cid -> {
            Msg.Head head = Msg.Head.newBuilder().mergeFrom(msg.getMsgHead()).setMsgId(IdUtil.nextId(cid)).build();
            return Msg.Protocol.newBuilder().mergeFrom(msg).setMsgHead(head).build();
        });

        /**
         * send ack to from id
         */
        if (onTheMachine)
        {
            ClientConnection conn = clientConnectionContext.getClientConnectionByUserId(msg.getMsgHead().getFromId());
            if (conn == null)
            {
                ChannelHandlerContext ctx = ServerTransferHandler.getOneOfTransferCtx(System.currentTimeMillis());
                ctx.writeAndFlush(getDelivered(ctx.channel().attr(NettyAttrUtil.NET_ID).get(), msg));
            } else
            {
                Msg.Protocol delivered = getDelivered(conn.getNetId(), msg);
                ServerAckWindow.offer(Long.valueOf(conn.getUserId()), delivered.getMsgHead().getMsgId(), delivered, m -> conn.getCtx().writeAndFlush(m));
            }
        }
    }

    public void doSendAckToClient(Msg.Protocol ackMsg)
    {
        ClientConnection conn = clientConnectionContext.getClientConnectionByUserId(ackMsg.getMsgHead().getDestId());
        if (conn == null)
        {
            logger.error("[send msg to client] not one the machine, userId: {}, connectorId: {}",
                    ackMsg.getMsgHead().getDestId(), ServerTransferHandler.CONNECTOR_ID);
            return;
        }
        Msg.Head copyHead = Msg.Head.newBuilder().mergeFrom(ackMsg.getMsgHead()).setMsgId(IdUtil.nextId(conn.getNetId())).build();

        Msg.Protocol copy = Msg.Protocol.newBuilder().mergeFrom(ackMsg)
                .setMsgHead(copyHead)
                .build();
        conn.getCtx().writeAndFlush(copy);
    }


    public void doSendAckToClientOrTransfer(Msg.Protocol ackMsg)
    {
        sendMsg(ackMsg.getMsgHead().getDestId(), ackMsg.getMsgHead().getMsgId(),
                netId -> {
                    Msg.Head copyHead = Msg.Head.newBuilder().mergeFrom(ackMsg.getMsgHead())
                            .setMsgId(IdUtil.nextId(netId))
                            .build();
                    return Msg.Protocol.newBuilder().mergeFrom(ackMsg).setMsgHead(copyHead).build();
                });
    }

    private Msg.Protocol getDelivered(Long connectionId, Msg.Protocol msg)
    {
        Msg.Head head = Msg.Head.newBuilder()
                .setVersion(1)
                .setMsgId(IdUtil.nextId(connectionId))
                .setMsgType(Msg.MsgType.ACK)
                .setFromId(msg.getMsgHead().getFromId())
                .setDestId(msg.getMsgHead().getDestId())
                .setTimeStamp(System.currentTimeMillis())
                .build();

        JSONObject ackId = new JSONObject();
        ackId.put("ackId", msg.getMsgHead().getMsgId());

        return Msg.Protocol.newBuilder()
                .setMsgHead(head)
                .setMsgBody(ackId.toString())
                .build();
    }


    private boolean sendMsg(String destUserId, Long msgId, Function<Long, Message> generateMsg)
    {
        ClientConnection destClientConn = clientConnectionContext.getClientConnectionByUserId(destUserId);
        if (destClientConn == null)
        {
            ChannelHandlerContext ctx = ServerTransferHandler.getOneOfTransferCtx(System.currentTimeMillis());
            ctx.writeAndFlush(generateMsg.apply(ctx.channel().attr(NettyAttrUtil.NET_ID).get()));
            return false;
        } else
        {
            Message message = generateMsg.apply(destClientConn.getNetId());
            ServerAckWindow.offer(destClientConn.getNetId(), msgId, message, msg -> {
                destClientConn.getCtx().channel().writeAndFlush(msg);
            });
            return true;
        }
    }
}
