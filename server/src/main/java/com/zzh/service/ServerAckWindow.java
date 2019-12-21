package com.zzh.service;

import com.google.protobuf.Message;
import com.zzh.domain.ResponseCollector;
import com.zzh.protobuf.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * for server, every connection should has an ServerAckWindow
 */
public class ServerAckWindow
{
    private static final Logger logger = LoggerFactory.getLogger(ServerAckWindow.class);

    /**
     * netId=>ServerAckWindow
     */
    private static Map<Long, ServerAckWindow> windowMap;
    private static ExecutorService executorService;

    static
    {
        windowMap = new ConcurrentHashMap<>();
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(ServerAckWindow::checkTimeoutAndRetry);
    }

    public ServerAckWindow(Long connectionId, int maxSize, Duration timeout)
    {
        this.responseCollectorMap = new ConcurrentHashMap<>();
        this.timeout = timeout;
        this.maxSize = maxSize;

        windowMap.put(connectionId, this);
    }


    private Duration timeout;
    private int maxSize;

    private ConcurrentHashMap<Long, ResponseCollector<Msg.Protocol>> responseCollectorMap;

    public static CompletableFuture<Msg.Protocol> offer(Long connectionId, Long id, Message sendMessage, Consumer<Message> sendFunction)
    {
        return windowMap.get(connectionId).offer(id, sendMessage, sendFunction);
    }

    public CompletableFuture<Msg.Protocol> offer(Long id, Message sendMessage, Consumer<Message> sendFunction)
    {
        if (responseCollectorMap.containsKey(id))
        {
            CompletableFuture<Msg.Protocol> future = new CompletableFuture<>();
            future.completeExceptionally(new Exception("send repeat msg id: " + id));
            return future;
        }
        if (responseCollectorMap.size() >= maxSize)
        {
            CompletableFuture<Msg.Protocol> future = new CompletableFuture<>();
            future.completeExceptionally(new Exception("server window is full"));
            return future;
        }

        ResponseCollector<Msg.Protocol> responseCollector = new ResponseCollector<>(sendMessage, sendFunction);
        responseCollector.send();
        responseCollectorMap.put(id, responseCollector);
        return responseCollector.getFuture();
    }

    public void ack(Msg.Protocol message)
    {
        Long msgId = Long.parseLong(message.getMsgBody());
        logger.debug("get ack, msg: {}", msgId);
        if (responseCollectorMap.containsKey(msgId))
        {
            responseCollectorMap.get(msgId).getFuture().complete(message);
            responseCollectorMap.remove(msgId);
        }
    }


    /**
     * 单个线程一直遍历所有channel的ackWin，检查消息是否超时需要重发
     */
    private static void checkTimeoutAndRetry()
    {
        while (true)
        {
            for (ServerAckWindow window : windowMap.values())
            {
                window.responseCollectorMap.entrySet().stream()
                        .filter(entry -> window.timeout(entry.getValue()))
                        .forEach(entry -> window.retry(entry.getKey(), entry.getValue()));

            }
        }
    }

    private void retry(Long id, ResponseCollector<?> collector)
    {
        logger.debug("retry msg: {}", id);
        //todo: if offline
        collector.send();
    }

    private boolean timeout(ResponseCollector<?> collector)
    {
        return collector.getSendTime().get() != 0 && collector.timeElapse() > timeout.toNanos();
    }


}