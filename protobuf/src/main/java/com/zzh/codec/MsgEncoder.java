package com.zzh.codec;

import com.google.protobuf.Message;
import com.zzh.constant.MsgTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MsgEncoder extends MessageToByteEncoder<Message>
{
    private static final Logger logger = LoggerFactory.getLogger(MsgEncoder.class);

    @Override
    protected void encode (ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception
    {
        try
        {

            byte[] bytes = msg.toByteArray();
            int code = MsgTypeEnum.getByClass(msg.getClass()).getCode();
            int length = bytes.length;

            ByteBuf buf = ctx.alloc().ioBuffer(8 + length);
            buf.writeInt(length);
            buf.writeInt(code);
            buf.writeBytes(bytes);

            out.writeBytes(buf);
        } catch (Exception e)
        {
            logger.error("[client] msg encode has error", e);
        }
    }
}
