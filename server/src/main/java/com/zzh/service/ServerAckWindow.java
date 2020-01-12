package com.zzh.service;

import com.zzh.domain.ResponseCollector;
import com.zzh.protobuf.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * server与client之间的每条连接都有一个ackWin。
 * ackWin负责消息的第一次下发和之后的超时重推。
 * ackWin可以保证 未消费 的消息的幂等性。已经消费的消息不应该到达这里。
 *
 * @author Administrator
 */
public class ServerAckWindow
{
    private static final Logger logger = LoggerFactory.getLogger(ServerAckWindow.class);

    /**
     * clientId=>ServerAckWindow
     */
    private static Map<String, ServerAckWindow> windowMap;
    private static ExecutorService executorService;

    static
    {
        windowMap = new ConcurrentHashMap<>();
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(ServerAckWindow::checkTimeoutAndRetry);
    }


    public ServerAckWindow(String clientId, int maxSize, Duration timeout)
    {
        this.responseCollectorMap = new ConcurrentHashMap<>();
        this.timeout = timeout;
        this.maxSize = maxSize;

        windowMap.put(clientId, this);
    }


    private Duration timeout;
    private int maxSize;

    /**
     * msgId->ResponseCollector
     */
    private ConcurrentHashMap<String, ResponseCollector> responseCollectorMap;

    public static CompletableFuture<Protocol.Msg> offer(String clientId, String id, Protocol.Msg sendMessage, Consumer<Protocol.Msg> sendFunction)
    {
        return windowMap.get(clientId).offer(id, sendMessage, sendFunction);
    }

    public CompletableFuture<Protocol.Msg> offer(String id, Protocol.Msg sendMessage, Consumer<Protocol.Msg> sendFunction)
    {
        if (responseCollectorMap.containsKey(id))
        {
            CompletableFuture<Protocol.Msg> future = new CompletableFuture<>();
            future.completeExceptionally(new Exception("send repeat msg id: " + id));
            return future;
        }
        if (responseCollectorMap.size() >= maxSize)
        {
            CompletableFuture<Protocol.Msg> future = new CompletableFuture<>();
            future.completeExceptionally(new Exception("server window is full"));
            return future;
        }


        ResponseCollector responseCollector = new ResponseCollector(sendMessage, sendFunction);
        responseCollector.send();
        responseCollectorMap.put(id, responseCollector);
        return responseCollector.getFuture();
    }

    public static void ack(String clientId, Protocol.Msg msg)
    {
        windowMap.get(clientId).ack(msg);
    }

    public void ack(Protocol.Msg message)
    {
        String msgId = message.getMsgBody();
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

    private void retry(String id, ResponseCollector collector)
    {
        logger.debug("retry msg: {}", id);
        //todo: if offline
        collector.send();
    }

    private boolean timeout(ResponseCollector collector)
    {
        return collector.getSendTime().get() != 0 && collector.timeElapse() > timeout.toNanos();
    }
}