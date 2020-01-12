package com.zzh.domain;

import com.google.protobuf.Message;
import com.zzh.protobuf.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ResponseCollector
{
    private static final Logger logger = LoggerFactory.getLogger(ResponseCollector.class);

    private Protocol.Msg sendMessage;
    private Consumer<Protocol.Msg> sendFunction;
    private CompletableFuture<Protocol.Msg> future;

    private volatile AtomicLong sendTime;
    private volatile AtomicBoolean sending;

    public ResponseCollector(Protocol.Msg sendMessage, Consumer<Protocol.Msg> sendFunction)
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
                logger.error("send msg send has error", e);
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

    public CompletableFuture<Protocol.Msg> getFuture()
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
