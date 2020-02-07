package com.zzh.protocol.codec;

import com.google.protobuf.Message;
import com.zzh.protocol.parse.ParseService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 私有协议编码
 * @Author: Administrator
 * @Date: 2020/1/29 13:41
 */
@Slf4j
public class MsgDecoder extends ByteToMessageDecoder
{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception
    {
        in.markReaderIndex();

        if (in.readableBytes() < 4)
        {
            in.resetReaderIndex();
            return;
        }

        int length = in.readInt();
        if (length < 0)
        {
            ctx.close();
            log.error("[IM msg decoder]message length less than 0, channel closed");
            return;
        }

        if (length > in.readableBytes() - 4)
        {
            in.resetReaderIndex();
            return;
        }

        int code = in.readInt();
        ByteBuf byteBuf = Unpooled.buffer(length);
        in.readBytes(byteBuf);
        byte[] body = byteBuf.array();

        try
        {
            Message message = ParseService.INSTANCE.getMsgByCode(code, body);
            list.add(message);
        } catch (Exception e)
        {
            log.error("parse service has error!", e);
        }
    }
}
