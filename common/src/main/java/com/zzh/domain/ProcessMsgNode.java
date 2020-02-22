package com.zzh.domain;

import com.google.protobuf.Message;
import com.zzh.protocol.Ack;
import com.zzh.util.IdUtil;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 消息处理
 * @Author: Administrator
 * @Date: 2020/1/31 14:08
 */
@Getter
@Slf4j
public class ProcessMsgNode
{
    private Long id; //收到的消息的id
    private Long sessionId; //会话Id
    private String fromId;  //消息发送者Id
    private String destId;  //消息接收者Id
    private Message message;

    private Channel channel;
    private CompletableFuture<Void> future;
    private Consumer<Message> consumer;

    public ProcessMsgNode(Long id, Long sessionId, String fromId, String destId, Message message, Channel channel, Consumer<Message> consumer)
    {
        this.id = id;
        this.sessionId = sessionId;
        this.fromId = fromId;
        this.destId = destId;
        this.message = message;
        this.channel = channel;
        this.future = new CompletableFuture<>();
        this.consumer = consumer;
    }

    public Void process()
    {
        try
        {
            consumer.accept(message);
            sendAck();
        } catch (Exception e)
        {
            log.error("process msg fail", e);
        } finally
        {
            complete();
        }
        return null;
    }

    public void sendAck()
    {
        if (channel.isOpen() && channel.isActive() && channel.isWritable())
        {
            Ack.AckMsg ackMsg = Ack.AckMsg.newBuilder()
                    .setId(IdUtil.snowGenId())
                    .setTimeStamp(System.currentTimeMillis())
                    .setFromModule(Ack.AckMsg.Module.CLIENT)
                    .setAckMsgId(id)
                    .setAckMsgSessionId(sessionId)
                    .setFromId(destId)
                    .setDestId(fromId)
                    .build();
            channel.writeAndFlush(ackMsg).addListener(future -> {
                if (future.isSuccess())
                {
                    log.info("客户端发送ACK,ackMsg:{}", ackMsg.toString());
                }
            });
        }
    }

    /**
     * 处理消息后，无论是否发生异常均调用此方法
     */
    public void complete()
    {
        this.future.complete(null);
    }
}
