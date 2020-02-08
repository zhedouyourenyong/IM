package com.zzh.api;

import com.zzh.ClientApplication;
import com.zzh.console.ConsoleCommandManager;
import com.zzh.domain.ClientAckWindow;
import com.zzh.domain.ServerAckWindow;
import com.zzh.protocol.Single;
import com.zzh.session.SessionHolder;
import com.zzh.util.IdUtil;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2020/1/31 20:42
 */
@Slf4j
public class ChatApi
{
    private Channel channel;

    public ChatApi(Channel channel)
    {
        this.channel = channel;
    }

    public void text(String destId, String content)
    {
        Single.SingleMsg msg = getSingleMsg(destId, content);

        ServerAckWindow.offer(ClientApplication.CLIENT_ID, msg.getId(), msg,
                channel::writeAndFlush);
    }


    private Single.SingleMsg getSingleMsg(String destId, String content)
    {
        return Single.SingleMsg.newBuilder()
                .setId(IdUtil.snowGenId())
                .setTimeStamp(System.currentTimeMillis())
                .setSessionId(SessionHolder.INSTANCE.getNextSessionId())
                .setFromId(SessionHolder.INSTANCE.getUserId())
                .setDestId(destId)
                .setFromModule(Single.SingleMsg.Module.CLIENT)
                .setDestModule(Single.SingleMsg.Module.CLIENT)
                .setBody(content)
                .build();
    }

}
