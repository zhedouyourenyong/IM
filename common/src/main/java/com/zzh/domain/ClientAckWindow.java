package com.zzh.domain;

import com.google.protobuf.Message;
import com.zzh.exception.ImException;
import com.zzh.protocol.Ack;
import com.zzh.util.IdUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 客户端使用，主要用于消息去重，保证有序
 * @Author: Administrator
 * @Date: 2020/1/30 22:19
 */
@Slf4j
public class ClientAckWindow
{
    private int maxSize;
    private AtomicLong lastId;
    private AtomicBoolean first;

    /**
     * sessionId->msg
     */
    private ConcurrentHashMap<Long, ProcessMsgNode> notContinuousMap;

    public ClientAckWindow()
    {
        this(20);
    }

    public ClientAckWindow(int maxSize)
    {
        this.maxSize = maxSize;
        lastId = new AtomicLong(-1);
        first = new AtomicBoolean(true);
        notContinuousMap = new ConcurrentHashMap<>();
    }

    public CompletableFuture<Void> offer(Long msgId, Long sessionId, String fromId, String destId, Message receivedMsg, Channel channel, Consumer<Message> processFunction)
    {
        if (isRepeat(sessionId))
        {
            channel.writeAndFlush(getAck(fromId, msgId, sessionId));
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.complete(null);
            return future;
        }

        ProcessMsgNode msgNode = new ProcessMsgNode(msgId, sessionId, fromId, destId, receivedMsg, channel, processFunction);

        if (!isContinuous(sessionId))
        {
            if (notContinuousMap.size() > maxSize)
            {
                CompletableFuture<Void> future = new CompletableFuture<>();
                future.completeExceptionally(new ImException("client window is full"));
                return future;
            }
            notContinuousMap.put(sessionId, msgNode);
            return msgNode.getFuture();
        }
        return processAsync(msgNode);
    }

    public void removeMsgFromNotContinueMap(Long sessionId)
    {
        if (notContinuousMap.containsKey(sessionId))
        {
            notContinuousMap.remove(sessionId);
            log.info("removeMsgFromNotContinueMap,sessionId:{}", sessionId);
        }
    }

    private CompletableFuture<Void> processAsync(ProcessMsgNode node)
    {
        return CompletableFuture.runAsync(node::process)
                .thenAccept(v -> {
                    lastId.set(node.getSessionId());
                    log.info("sessionId update to:{}", lastId.get());
                    removeMsgFromNotContinueMap(node.getSessionId());
                })
                .thenComposeAsync(v -> {
                    Long nextId = nextId(node.getSessionId());
                    if (notContinuousMap.containsKey(nextId))
                    {
                        ProcessMsgNode nextNode = notContinuousMap.get(nextId);
                        return processAsync(nextNode);
                    } else
                    {
                        return node.getFuture();
                    }
                })
                .exceptionally(e -> {
                    log.error("[process received msg] has error", e);
                    return null;
                });
    }


    private boolean isRepeat(Long sessionId)
    {
        return sessionId <= lastId.get();
    }

    private boolean isContinuous(Long msgId)
    {
        if (first.compareAndSet(true, false))
        {
            return true;
        } else
        {
            return msgId - lastId.get() == 1;
        }
    }

    private Long nextId(Long msgId)
    {
        return msgId + 1;
    }

    public Ack.AckMsg getAck(String fromId, Long msgId, Long sessionId)
    {
        return Ack.AckMsg.newBuilder()
                .setId(IdUtil.snowGenId())
                .setTimeStamp(System.currentTimeMillis())
                .setFromModule(Ack.AckMsg.Module.CLIENT)
                .setAckMsgId(msgId)
                .setAckMsgSessionId(sessionId)
                .setDestId(fromId)
                .build();
    }
}
