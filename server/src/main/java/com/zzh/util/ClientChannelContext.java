package com.zzh.util;

import com.zzh.pojo.User;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientChannelContext
{
    private static final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();
    private static final Map<String, String> SESSION_MAP = new ConcurrentHashMap<>();

    public static void saveSession(String userId, String userName)
    {
        SESSION_MAP.put(userId, userName);
    }

    public static void removeSession(String userId)
    {
        SESSION_MAP.remove(userId);
    }

    public static void put(String id, Channel channel)
    {
        CHANNEL_MAP.put(id, channel);
    }

    public static Channel get(Long id)
    {
        return CHANNEL_MAP.get(id);
    }

    public static Map<String, Channel> getMAP()
    {
        return CHANNEL_MAP;
    }

    public static void remove(Channel channel)
    {
        CHANNEL_MAP.entrySet().stream().filter(entry -> entry.getValue() == channel).forEach(entry -> CHANNEL_MAP.remove(entry.getKey()));
    }


    public static User getUserInfo(Channel channel)
    {
        String userId = NettyAttrUtil.getUserId(channel);
        if (userId != null)
        {
            String userName = SESSION_MAP.get(userId);
            User info = new User();
            info.setUserId(userId);
            info.setUserName(userName);
            return info;
        }
        return null;
    }
}
