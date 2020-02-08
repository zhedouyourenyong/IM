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
    private String fromId;  //发送者Id
    private Message message;

    private Channel channel;
    private CompletableFuture<Void> future;
    private Consumer<Message> consumer;

    public ProcessMsgNode(Long id, Long sessionId, String fromId, Message message, Channel channel, Consumer<Message> consumer)
    {
        this.id = id;
        this.sessionId = sessionId;
        this.fromId = fromId;
        this.message = message;
        this.channel = channel;
        this.future = new CompletableFuture<>();
        this.consumer = consumer;
    }

    public Void process()
    {
        consumer.accept(message);
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
                    .setDestId(fromId)
                    .build();
            channel.writeAndFlush(ackMsg).addListener(future -> {
                if (future.isSuccess())
                {
                    log.info("发送ACK,ackMsgId:{}", sessionId);
                }
            });
        }
    }

    public void complete()
    {
        this.future.complete(null);
    }
}
