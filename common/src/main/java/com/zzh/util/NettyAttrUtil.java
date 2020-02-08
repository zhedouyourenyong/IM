package com.zzh.util;

import com.zzh.domain.ClientAckWindow;
import com.zzh.domain.ServerAckWindow;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicLong;

public class NettyAttrUtil
{
    public static final AttributeKey<String> SERVER_ID = AttributeKey.valueOf("serverId");
    public static final AttributeKey<Long> NET_ID = AttributeKey.valueOf("netId");
    public static final AttributeKey<String> USER_ID = AttributeKey.valueOf("userId");
    public static final AttributeKey<String> READ_TIME = AttributeKey.valueOf("readTime");
    public static final AttributeKey<AtomicLong> TID_GENERATOR = AttributeKey.valueOf("tidGenerator");
    public static final AttributeKey<String> USER_NAME = AttributeKey.valueOf("userName");
    public static final AttributeKey<ClientAckWindow> CLIENT_ACK_WIN = AttributeKey.valueOf("clientAckWin");
    public static final AttributeKey<ServerAckWindow> SERVER_ACK_WIN = AttributeKey.valueOf("serverAckWin");
    public static final AttributeKey<String> CLIENT_ID = AttributeKey.valueOf("clientId");
}
