package com.zzh.handler;

import com.google.protobuf.Message;
import com.zzh.domain.ServerTransferConnContext;
import com.zzh.protocol.Ack;
import com.zzh.protocol.Internal;
import com.zzh.protocol.Single;
import com.zzh.service.TransferService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2019/12/21 22:51
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class TransferMsgHandler extends SimpleChannelInboundHandler<Message>
{
    @Autowired
    private ServerTransferConnContext connContext;
    @Autowired
    private TransferService transferService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception
    {
        log.info("receive msg:{}", msg.toString());

        if (msg instanceof Single.SingleMsg)
        {
            transferService.doChat((Single.SingleMsg) msg);
        } else if (msg instanceof Ack.AckMsg)
        {
            transferService.doSendAck((Ack.AckMsg) msg);
        } else if (msg instanceof Internal.InternalMsg)
        {
            transferService.doGreet((Internal.InternalMsg) msg, ctx);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        connContext.removeConnection(ctx);
    }
}
