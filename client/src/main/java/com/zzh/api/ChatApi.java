package com.zzh.api;

import com.zzh.console.ConsoleCommandManager;
import com.zzh.domain.ClientAckWindow;
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
    private ConsoleCommandManager manager;

    public ChatApi(Channel channel)
    {
        this.channel = channel;
        this.manager = new ConsoleCommandManager();
    }


    public void startConsoleThread()
    {
        Scanner scanner = new Scanner(System.in);
        Thread thread = new Thread(() -> {
            while (!Thread.interrupted())
            {
                try
                {
                    manager.exec(scanner, channel);
                } catch (Exception e)
                {
                    log.error("consoleThread has error!", e);
                }
            }
        });
        thread.setName("consoleThread");
        thread.start();
    }
}
