package com.zzh;

import com.zzh.console.ConsoleCommandManager;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;


/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2020/2/1 21:57
 */
@Slf4j
@Component
public class ImClientStarter implements CommandLineRunner
{
    public MyClient myClient;

    @Autowired
    public ImClientStarter(MyClient myClient)
    {
        this.myClient = myClient;
    }

    @Override
    public void run(String... args) throws Exception
    {
        startConsoleThread();
    }

    public void startConsoleThread()
    {
        Scanner scanner = new Scanner(System.in);
        Thread thread = new Thread(() -> {
            ConsoleCommandManager manager = new ConsoleCommandManager();
            while (!Thread.interrupted())
            {
                try
                {
                    manager.exec(scanner, myClient.getChatApi());
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
