package com.zzh.parse;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.zzh.constant.MsgTypeEnum;
import com.zzh.protobuf.Ack;
import com.zzh.protobuf.Chat;
import com.zzh.protobuf.Internal;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ParseService
{
    private Map<MsgTypeEnum, Parse> parseFunctionMap;

    public ParseService ()
    {
        parseFunctionMap = new HashMap<>();

        parseFunctionMap.put(MsgTypeEnum.CHAT, Chat.ChatMsg::parseFrom);
        parseFunctionMap.put(MsgTypeEnum.INTERNAL, Internal.InternalMsg::parseFrom);
        parseFunctionMap.put(MsgTypeEnum.ACK, Ack.AckMsg::parseFrom);
    }


    public Message getMsgByCode (int code, byte[] bytes) throws InvalidProtocolBufferException, Exception
    {
        MsgTypeEnum msgType = MsgTypeEnum.getByCode(code);
        Parse parseFunction = parseFunctionMap.get(msgType);
        if(parseFunction == null)
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
        Message process (byte[] bytes) throws InvalidProtocolBufferException;
    }
}
