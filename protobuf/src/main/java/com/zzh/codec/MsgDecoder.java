package com.zzh.codec;

import com.google.protobuf.Message;
import com.zzh.parse.ParseService;
import com.zzh.util.SpringBeanFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class MsgDecoder extends ByteToMessageDecoder
{
    private static final Logger logger = LoggerFactory.getLogger(MsgDecoder.class);

    private ParseService parseService;

    public MsgDecoder ()
    {
        this.parseService = SpringBeanFactory.getBean(ParseService.class);
    }

    @Override
    protected void decode (ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        in.markReaderIndex();

        if(in.readableBytes() < 4)
        {
            in.resetReaderIndex();
            return;
        }

        int length = in.readInt();

        if(length < 0)
        {
            ctx.close();
            logger.error("[IM msg decoder]message length less than 0, channel closed");
            return;
        }

        if(length > in.readableBytes() - 4)
        {
            in.resetReaderIndex();
            return;
        }

        int code = in.readInt();
        ByteBuf byteBuf = Unpooled.buffer(length);

        in.readBytes(byteBuf);

        byte[] body = byteBuf.array();

        Message msg = parseService.getMsgByCode(code, body);
        out.add(msg);
    }
}
