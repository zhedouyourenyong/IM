package com.zzh.handler;

import com.zzh.domain.ServerTransferConnContext;
import com.zzh.protobuf.Msg;
import com.zzh.service.TransferService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2019/12/21 22:51
 */
@Component
@ChannelHandler.Sharable
public class TransferMsgHandler extends SimpleChannelInboundHandler<Msg.Protocol>
{
    private static final Logger logger = LoggerFactory.getLogger(TransferMsgHandler.class);
    @Autowired
    private ServerTransferConnContext connContext;
    @Autowired
    private TransferService transferService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Msg.Protocol msg) throws Exception
    {
        logger.info("receive msg:{}",msg.toString());

        Msg.MsgType msgType = msg.getMsgHead().getMsgType();
        if (msgType == Msg.MsgType.GREET)
        {
            transferService.doGreet(msg, ctx);
        } else if (msgType == Msg.MsgType.ACK)
        {
            transferService.doSendAck(msg);
        } else if (msgType == Msg.MsgType.SINGLE)
        {
            transferService.doChat(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        connContext.removeConnection(ctx);
    }

}
