package com.zzh.protocol.parse;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.zzh.protocol.Ack;
import com.zzh.protocol.Internal;
import com.zzh.protocol.Single;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: byte[]-->Message
 * @Author: Administrator
 * @Date: 2020/1/29 16:29
 */
@Slf4j
public class ParseService
{

    public static final ParseService INSTANCE = new ParseService();

    /**
     * Parse代表一个方法
     */
    private Map<MsgTypeEnum, Parse> parseFunctionMap;

    private ParseService()
    {
        parseFunctionMap = new HashMap<>(MsgTypeEnum.values().length);

        parseFunctionMap.put(MsgTypeEnum.SINGLE, Single.SingleMsg::parseFrom);
        parseFunctionMap.put(MsgTypeEnum.INTERNAL, Internal.InternalMsg::parseFrom);
        parseFunctionMap.put(MsgTypeEnum.ACK, Ack.AckMsg::parseFrom);
    }

    public Message getMsgByCode(int code, byte[] bytes) throws Exception
    {
        MsgTypeEnum msgType = MsgTypeEnum.getByCode(code);
        Parse parseFunction = parseFunctionMap.get(msgType);
        if (parseFunction == null)
        {
            throw new Exception("[msg parse], no proper parse function, msgType: " + msgType.name());
        }
        return parseFunction.process(bytes);
    }


    @FunctionalInterface
    public interface Parse
    {
        /**
         * parse msg
         *
         * @param bytes msg bytes
         * @return
         * @throws InvalidProtocolBufferException
         */
        Message process(byte[] bytes) throws InvalidProtocolBufferException;
    }
}
