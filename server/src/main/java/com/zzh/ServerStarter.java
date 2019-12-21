package com.zzh;

import com.zzh.client.TransferClient;
import com.zzh.config.ServerConfig;
import com.zzh.registery.impl.ZKServerAddressRegister;
import com.zzh.server.IMServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(SpringBoot容器启动后 ， 负责启动Netty然后启动一个线程注册地址到Zookeeper)
 * @Author: Administrator
 * @Date: 2019/12/10 21:15
 */
@Component
public class ServerStarter
{
    private static final Logger logger = LoggerFactory.getLogger(ServerStarter.class);

    @Autowired
    private IMServer imServer;
    @Autowired
    private ZKServerAddressRegister addressRegister;
    @Autowired
    private ServerConfig serverConfig;

    public void start()
    {
        try
        {
            TransferClient.start(null);
            imServer.start();
            addressRegister.register();
        } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }
}
