package com.zzh.util;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.netty.channel.Channel;
import lombok.Data;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class SessionHolder
{
    public static final SessionHolder INSTANCE = new SessionHolder();

    private String userName;
    private String userId;
    private String serviceInfo;
    private Date startDate;
    private Channel channel;
    private AtomicLong sessionId = new AtomicLong(-1);
    private Boolean login = false;

    public long getNextSessionId()
    {
        return sessionId.incrementAndGet();
    }
}
