package com.zzh;

import com.zzh.api.ChatApi;
import com.zzh.api.UserApi;
import com.zzh.client.IMClient;
import com.zzh.config.UserConfig;
import com.zzh.console.ConsoleCommandManager;
import com.zzh.exception.ImException;
import com.zzh.pojo.RouteInfo;
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
    private IMClient imClient;
    private ChatApi chatApi;
    private UserApi loginApi;
    private UserConfig userConfig;

    @Autowired
    public ImClientStarter( UserApi loginApi, UserConfig userConfig)
    {
        this.imClient = new IMClient();
        this.loginApi = loginApi;
        this.userConfig = userConfig;
    }

    @Override
    public void run(String... args) throws Exception
    {
        RouteInfo routeInfo = loginApi.login(userConfig.getName(), userConfig.getPassword());
        Channel channel = imClient.start(routeInfo.getIp(), routeInfo.getPort());

        if (channel == null)
        {
            throw new ImException("与服务端连接失败");
        }

        chatApi = new ChatApi(channel);

        startConsoleThread(channel);
    }

    public void startConsoleThread(Channel channel)
    {
        Scanner scanner = new Scanner(System.in);
        Thread thread = new Thread(() -> {
            ConsoleCommandManager manager = new ConsoleCommandManager();
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
