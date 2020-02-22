package com.zzh;

import com.zzh.config.ClientConfig;
import com.zzh.core.ChatApi;
import com.zzh.core.ClientMsgListener;
import com.zzh.core.IMClient;
import com.zzh.core.UserApi;
import com.zzh.pojo.RouteInfo;
import io.netty.channel.Channel;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2020/2/21 15:56
 */
public class MyClient
{
    private ClientConfig clientConfig;
    private IMClient imClient;
    private UserApi userApi;
    private ChatApi chatApi;
    private ClientMsgListener msgListener;


    public MyClient(ClientConfig clientConfig, ClientMsgListener msgListener)
    {
        this.clientConfig = clientConfig;
        this.userApi = new UserApi();
        this.msgListener = msgListener;

        this.imClient = new IMClient(this.msgListener);

        RouteInfo routeInfo = userApi.login(clientConfig.getRouteUrl(), clientConfig.getName(), clientConfig.getPassword());
        Channel channel = imClient.start(routeInfo.getIp(), routeInfo.getPort());
        chatApi = new ChatApi(channel);
    }

    /**
     * 更改默认的msgListener
     *
     * @param msgListener
     */
    public void setMsgListener(ClientMsgListener msgListener)
    {
        this.msgListener = msgListener;
    }

    public ChatApi getChatApi()
    {
        return chatApi;
    }

    public UserApi getUserApi()
    {
        return userApi;
    }
}
