package com.zzh.service;

import com.alibaba.fastjson.JSONObject;
import com.zzh.protobuf.Msg;
import com.zzh.util.ClientChannelContext;
import com.zzh.util.NettyAttrUtil;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MsgTransferHandler
{
    private static final Logger logger = LoggerFactory.getLogger(MsgTransferHandler.class);

    @Autowired
    OkHttpClient httpClient;


    /**
     * 负责消息的实际发送工作
     * 目标连接在本机和不在本机写成两个方法
     *分布式通信，看极客时间里和分布式相关的课程，使用消息队列或RPC进行通信。
     * @param msg
     * @param channel
     */
    public void sendSingleMsg(Msg.Protocol msg, Channel channel)
    {
        Long fromId = Long.parseLong(msg.getMsgHead().getFromId());
        Long destId = Long.parseLong(msg.getMsgHead().getDestId());
        Channel destChannel = ClientChannelContext.get(destId);
        ServerAckWindow serverAckWindow = NettyAttrUtil.getServerAckWindow(channel);

        /**
         * 目标连接在本机器
         */
        if (destChannel != null)
        {
            if (destChannel.isActive() && destChannel.isWritable())
            {
                destChannel.writeAndFlush(msg).addListener(future -> {
                    if (future.isCancelled())
                    {
                        logger.warn("future has been cancelled. {}, channel: {}", msg.getMsgBody().toString(), destChannel);
                    } else if (future.isSuccess())
                    {
                        serverAckWindow.addMsgToAckWindow(destChannel, msg);
                        logger.warn("future has been successfully pushed. {}, channel: {}", msg.getMsgBody().toString(), destChannel);
                    } else
                    {
                        logger.error("message write fail, {}, channel: {}", msg.getMsgBody().toString(), destChannel, future.cause());
                    }
                });
            }
        } else
        {
            //todo 通过路由层获取目标channel所在机器的ip+port，然后跨机器通信
            //怎么搞ack，，，rpc方式吗

        }
    }

    private JSONObject getDestChannelAddress(Long userId)
    {
        JSONObject address=new JSONObject();
        return address;
    }

    public void sendGroupMsg(Msg.Protocol msg)
    {

    }
}
