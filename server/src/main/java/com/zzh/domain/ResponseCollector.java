package com.zzh.domain;

import com.google.protobuf.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;


/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 线程安全, 封装消息，发送消息的逻辑，以及消息状态
 * @Author: Administrator
 * @Date: 2019/12/19 13:58
 */
@Slf4j
public class ResponseCollector
{
    private Message sendMessage;
    private Consumer<Message> sendFunction;
    private CompletableFuture<Message> future;

    private volatile AtomicLong sendTime;
    private volatile AtomicBoolean sending;

    public ResponseCollector(Message sendMessage, Consumer<Message> sendFunction)
    {
        this.sendMessage = sendMessage;
        this.sendFunction = sendFunction;
        this.future = new CompletableFuture<>();
        this.sendTime = new AtomicLong(0);
        this.sending = new AtomicBoolean(false);
    }

    public void send()
    {
        if (sending.compareAndSet(false, true))
        {
            this.sendTime.set(System.nanoTime());
            try
            {
                sendFunction.accept(sendMessage);
            } catch (Exception e)
            {
                log.error("send msg send has error", e);
            } finally
            {
                this.sending.set(false);
            }
        }
    }

    public long timeElapse()
    {
        return System.nanoTime() - sendTime.get();
    }

    public CompletableFuture<Message> getFuture()
    {
        return future;
    }

    public AtomicLong getSendTime()
    {
        return sendTime;
    }

    public AtomicBoolean getSending()
    {
        return sending;
    }
}
