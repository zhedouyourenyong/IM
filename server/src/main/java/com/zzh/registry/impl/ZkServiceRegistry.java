package com.zzh.registry.impl;

import com.zzh.config.ServerConfig;
import com.zzh.registry.ServiceRegistry;
import com.zzh.util.SpringBeanFactory;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetAddress;


public class ZkServiceRegistry implements ServiceRegistry, Runnable
{
    private static final Logger logger = LoggerFactory.getLogger(ZkServiceRegistry.class);

    private ZkClient zkClient;

    private ServerConfig serverConfig;


    public ZkServiceRegistry ()
    {
        serverConfig = SpringBeanFactory.getBean(ServerConfig.class);
        zkClient = new ZkClient(serverConfig.getZkAddress(), serverConfig.getSessionTimeout(), serverConfig.getConnectionTimeout());
        logger.info("connect to zookeeper");
    }

    @Override
    public void register ()
    {
        try
        {
            String rootPath = serverConfig.getRegistryRootPath();
            if(!zkClient.exists(rootPath))
            {
                zkClient.createPersistent(rootPath);
                logger.debug("create registry node: {}", rootPath);
            }

            String ip = InetAddress.getLocalHost().getHostAddress();
            String servicePath = rootPath + "/ip-" + ip + ":" + serverConfig.getServerPort() + ":" + serverConfig.getWebPort();
            if(!zkClient.exists(servicePath))
            {
                zkClient.createPersistent(servicePath);
                logger.info("注册 zookeeper 成功，msg=[{}]", servicePath);
            }
        } catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void run ()
    {
        register();
    }
}
