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
        return Stream.of(values()).filter(t -> t.code == code)
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public static MsgTypeEnum getByClass (Class<?> clazz)
    {
        return Stream.of(values()).filter(t -> t.clazz == clazz)
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public int getCode ()
    {
        return code;
    }
}
