package com.zzh.registery;

import com.alibaba.fastjson.JSONObject;
import com.zzh.IMServerApplication;
import com.zzh.config.ServerConfig;
import com.zzh.config.ZkConfig;
import com.zzh.interf.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Slf4j
@Component
public class ZKServerAddressRegister implements ServiceRegistry
{
    private ZkClient zkClient;
    private ZkConfig zkConfig;
    private ServerConfig serverConfig;

    @Autowired
    private ZKServerAddressRegister(ZkClient zkClient, ZkConfig zkConfig, ServerConfig serverConfig)
    {
        this.zkClient = zkClient;
        this.zkConfig = zkConfig;
        this.serverConfig = serverConfig;
    }

    @Override
    public void registry()
    {
        try
        {
            String rootPath = zkConfig.getRegistryRootPath();
            if (!zkClient.exists(rootPath))
            {
                zkClient.createPersistent(rootPath);
                log.info("create registry node: {}", rootPath);
            }

            /**
             *  /route/ip-IP地址:Netty端口:Server唯一识别码
             */
//            String localAddress = InetAddress.getLocalHost().getHostAddress();
//            String servicePath = rootPath + "/ip-" + localAddress + ":" + serverConfig.getServerPort() + ":" + IMServerApplication.SERVER_ID;
            String servicePath = rootPath + "/" + getRegistryContent();
            if (!zkClient.exists(servicePath))
            {
                zkClient.createEphemeral(servicePath);
                log.info("注册 ServerAddress 成功，msg=[{}]", servicePath);
            }
        } catch (Exception e)
        {
            log.error("register server address failure", e);
        }
    }

    private String getRegistryContent() throws UnknownHostException
    {
        String localAddress = InetAddress.getLocalHost().getHostAddress();
        JSONObject route = new JSONObject();
        route.put("ip", localAddress);
        route.put("serverPort", serverConfig.getServerPort());
        route.put("serverId", IMServerApplication.SERVER_ID);
        return route.toJSONString();
    }
}
