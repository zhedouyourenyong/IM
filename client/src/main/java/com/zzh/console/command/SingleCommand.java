package com.zzh.console.command;

import com.zzh.console.ConsoleCommand;
import com.zzh.protocol.Single;
import com.zzh.util.IdUtil;
import com.zzh.util.SessionHolder;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 私聊命令
 * @Author: Administrator
 * @Date: 2020/2/5 18:22
 */
@Slf4j
public class SingleCommand implements ConsoleCommand
{
    @Override
    public void exec(Scanner scanner, Channel channel)
    {
        System.out.print("目标用户ID:");
        String destId = scanner.next();
        System.out.print("内容:");
        String content = scanner.next();

        channel.writeAndFlush(getSingleMsg(destId, content));
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
