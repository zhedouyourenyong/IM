package com.zzh.constant;

import com.zzh.protobuf.Ack;
import com.zzh.protobuf.Chat;
import com.zzh.protobuf.Internal;

import java.util.stream.Stream;

public enum MsgTypeEnum
{

    /**
     * 聊天消息
     */
    CHAT(0, Chat.ChatMsg.class),

    /**
     * 内部消息
     */
    INTERNAL(1, Internal.InternalMsg.class),

    /**
     * ack消息
     */
    ACK(2, Ack.AckMsg.class);

    int code;
    Class<?> clazz;

    MsgTypeEnum (int code, Class<?> clazz)
    {
        this.code = code;
        this.clazz = clazz;
    }

    public static MsgTypeEnum getByCode (int code)
    {
        for (MsgTypeEnum typeEnum : values())
        {
            if(typeEnum.getCode() == code)
            {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("ResultInfo StatusEnum not legal:" + code);
    }

    public static MsgTypeEnum getByClass (Class<?> clazz)
    {
        for (MsgTypeEnum typeEnum : values())
        {
            if(typeEnum.getClass() == clazz)
            {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("ResultInfo StatusEnum not legal:" + clazz.getName());
    }

    public int getCode ()
    {
        return code;
    }
}
