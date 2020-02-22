package com.zzh.core;

import com.zzh.domain.ServerAckWindow;
import com.zzh.protocol.Single;
import com.zzh.session.Session;
import com.zzh.util.IdUtil;
import io.netty.channel.Channel;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2020/1/31 20:42
 */

public class ChatApi
{
    private Channel channel;

    public ChatApi(Channel channel)
    {
        this.channel = channel;
    }

    public void sendText(String destId, String content)
    {
        Single.SingleMsg msg = getSingleMsg(destId, content);

        ServerAckWindow.offer(IMClient.CLIENT_ID, msg.getId(), msg,
                channel::writeAndFlush);
    }


    private Single.SingleMsg getSingleMsg(String destId, String content)
    {
        return Single.SingleMsg.newBuilder()
                .setId(IdUtil.snowGenId())
                .setTimeStamp(System.currentTimeMillis())
                .setSessionId(Session.INSTANCE.getNextSessionId())
                .setFromId(Session.INSTANCE.getUserId())
                .setDestId(destId)
                .setFromModule(Single.SingleMsg.Module.CLIENT)
                .setDestModule(Single.SingleMsg.Module.CLIENT)
                .setBody(content)
                .build();
    }

}
