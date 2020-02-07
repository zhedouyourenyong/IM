package com.zzh;

import com.zzh.api.ChatApi;
import com.zzh.api.LoginApi;
import com.zzh.client.IMClient;
import com.zzh.config.UserConfig;
import com.zzh.exception.ImException;
import com.zzh.pojo.RouteInfo;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2020/2/1 21:57
 */
@Component
public class ImClientStarter implements CommandLineRunner
{
    private IMClient imClient;
    private ChatApi chatApi;
    private LoginApi loginApi;
    private UserConfig userConfig;

    @Autowired
    public ImClientStarter(IMClient imClient, LoginApi loginApi, UserConfig userConfig)
    {
        this.imClient = imClient;
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
        chatApi.startConsoleThread();
    }
}
