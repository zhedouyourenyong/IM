package com.zzh.service;

import com.zzh.protobuf.Msg;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.*;


public class ServerAckWindow
{
    private Map<Long, Msg.Protocol> ackWindow = new ConcurrentHashMap<>();
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(8);

    /**
     * 将推送的消息加入待ack列表
     */
    public void addMsgToAckWindow(Channel destChannel, Msg.Protocol msg)
    {
        ackWindow.put(msg.getMsgHead().getMsgId(), msg);
        executorService.schedule(() -> {
            if (destChannel.isActive())
            {
                checkAndResend(destChannel, msg);
            }
        }, 200, TimeUnit.MILLISECONDS);
    }

    /**
     * 将已ack的消息从ack列表删除
     */
    public void removeMsgFromAckWindow(long msgId)
    {
        ackWindow.remove(msgId);
    }


    /**
     * 检查并重推
     */
    private void checkAndResend(Channel destChannel, Msg.Protocol msg)
    {
        long msgId = msg.getMsgHead().getMsgId();
        if (ackWindow.containsKey(msgId) && destChannel.isWritable())
        {
            destChannel.writeAndFlush(msg);
        }
    }


}
