package com.zzh.registery.impl;

import com.zzh.config.ServerConfig;
import com.zzh.registery.ServerAddressRegistry;
import com.zzh.util.SpringBeanFactory;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class ZKServerAddressRegister implements ServerAddressRegistry
{
    private static final Logger logger = LoggerFactory.getLogger(ZKServerAddressRegister.class);

    private ZkClient zkClient;

    private ServerConfig serverConfig;


    private void init()
    {
        serverConfig = SpringBeanFactory.getBean(ServerConfig.class);
        zkClient = SpringBeanFactory.getBean(ZkClient.class);
    }

    @Override
    public void register()
    {
        try
        {
            init();
            Thread thread = new Thread(() -> {
                try
                {
                    String rootPath = serverConfig.getRegistryRootPath();
                    if (!zkClient.exists(rootPath))
                    {
                        zkClient.createPersistent(rootPath);
                        logger.info("create registry node: {}", rootPath);
                    }

                    /**
                     *  /route/ip-192.168.64.81:8899:8081
                     */
                    String localAddress = InetAddress.getLocalHost().getHostAddress();
                    String servicePath = rootPath + "/ip-" + localAddress + ":" + serverConfig.getServerPort() + ":" + serverConfig.getWebPort();
                    if (!zkClient.exists(servicePath))
                    {
                        zkClient.createEphemeral(servicePath);
                        logger.info("注册 ServerAddress 成功，msg=[{}]", servicePath);
                    }
                } catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            });
            thread.setName("zk-registry");
            thread.start();
        } catch (Exception e)
        {
            logger.error("register server address failure", e);
        }
    }
}
