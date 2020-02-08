package com.zzh.protocol.codec;

import com.google.protobuf.Message;
import com.zzh.protocol.parse.MsgTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 私有协议解码
 * @Author: Administrator
 * @Date: 2020/1/29 13:41
 */

@Slf4j
public class MsgEncoder extends MessageToByteEncoder<Message>
{
    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) throws Exception
    {
        try
        {
            byte[] bytes=message.toByteArray();
            int code= MsgTypeEnum.getByClass(message.getClass()).getCode();
            int length=bytes.length;

            ByteBuf buf= Unpooled.directBuffer(8+length);
            buf.writeInt(length);
            buf.writeInt(code);
            buf.writeBytes(bytes);

            out.writeBytes(buf);
        }catch (Exception e)
        {
            log.error("msg encoder has error!",e);
        }
    }
}
