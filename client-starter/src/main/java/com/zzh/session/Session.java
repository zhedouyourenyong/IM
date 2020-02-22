package com.zzh.session;

import io.netty.channel.Channel;
import lombok.Data;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class Session
{
    public static final Session INSTANCE = new Session();

    private String userId;
    private String userName;
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
