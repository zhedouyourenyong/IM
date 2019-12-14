package com.zzh.util;

import com.zzh.service.ServerAckWindow;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicLong;

public class NettyAttrUtil
{
    private static final AttributeKey<String> USER_ID = AttributeKey.valueOf("userId");
    private static final AttributeKey<String> READ_TIME = AttributeKey.valueOf("readTime");
    private static final AttributeKey<AtomicLong> TID_GENERATOR = AttributeKey.valueOf("tidGenerator");
    private static final AttributeKey<ServerAckWindow> SERVER_ACK_WINDOW = AttributeKey.valueOf("serverAckWindow");

    public static void updateReaderTime(Channel channel, Long time)
    {
        channel.attr(READ_TIME).set(time.toString());
    }


    public static Long getLastReaderTime(Channel channel)
    {
        String lastReaderTime = channel.attr(READ_TIME).get();

        if (lastReaderTime != null)
        {
            return Long.valueOf(lastReaderTime);
        }
        return null;
    }

    public static void initAckWindow(Channel channel)
    {
        channel.attr(SERVER_ACK_WINDOW).set(new ServerAckWindow());
    }

    public static ServerAckWindow getServerAckWindow(Channel channel)
    {
        return channel.attr(SERVER_ACK_WINDOW).get();
    }

    public static Long initTid(Channel channel)
    {
        AtomicLong tid = new AtomicLong(Long.parseLong(IdUtil.uuid()));
        channel.attr(TID_GENERATOR).set(tid);
        return tid.get();
    }

    public static AtomicLong getTid(Channel channel)
    {
        return channel.attr(TID_GENERATOR).get();
    }

    public static void setUserId(Channel channel, String userId)
    {
        channel.attr(USER_ID).set(userId);
    }

    public static String getUserId(Channel channel)
    {
        return channel.attr(USER_ID).get();
    }
}
