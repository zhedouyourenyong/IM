package com.zzh.protocol.parse;

import com.zzh.protocol.Ack;
import com.zzh.protocol.Internal;
import com.zzh.protocol.Single;

import java.util.stream.Stream;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 消息类型 Class<-->code
 * @Author: Administrator
 * @Date: 2020/1/29 14:51
 */
public enum MsgTypeEnum
{
    SINGLE(0, Single.SingleMsg.class),
    ACK(1, Ack.AckMsg.class),
    INTERNAL(2, Internal.InternalMsg.class);

    int code;
    Class<?> clazz;

    MsgTypeEnum(int code, Class<?> clazz)
    {
        this.code = code;
        this.clazz = clazz;
    }

    public static MsgTypeEnum getByClass(Class<?> clazz)
    {
        return Stream.of(values()).filter(t -> t.clazz == clazz)
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public static MsgTypeEnum getByCode(int code)
    {
        return Stream.of(values()).filter(t -> t.code == code)
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public int getCode()
    {
        return code;
    }

    public Class<?> getClazz()
    {
        return clazz;
    }
}
