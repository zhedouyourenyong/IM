package com.zzh.util;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;


public class IdUtil
{

    private static SnowFlake snowFlake;
    private static ConcurrentMap<Serializable, AtomicLong> sessionMap;

    static
    {
        snowFlake = new SnowFlake(1, 1);
        sessionMap = new ConcurrentHashMap<>();
    }

    public static String uuid()
    {
        return UUID.randomUUID().toString().split("-")[0];
    }

    /**
     * snowFlake
     * for InternalMsg
     *
     * @return
     */
    public static Long snowGenId()
    {
        return snowFlake.nextId();
    }

    /**
     * consistent id
     * for ChatMsg, AckMsg
     *
     * @return
     */
    public static Long nextId(Serializable connectorId)
    {
        return sessionMap.computeIfAbsent(connectorId,
                key -> new AtomicLong(0)).incrementAndGet();
    }
}
