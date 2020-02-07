package com.zzh.registery;

import com.alibaba.fastjson.JSONObject;
import com.zzh.config.TransferConfig;
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
public class ZKTransferAddressRegister implements ServiceRegistry
{
    private ZkClient zkClient;
    private ZkConfig zkConfig;
    private TransferConfig transferConfig;

    @Autowired
    private ZKTransferAddressRegister(ZkClient zkClient, ZkConfig zkConfig, TransferConfig transferConfig)
    {
        this.zkClient = zkClient;
        this.zkConfig = zkConfig;
        this.transferConfig = transferConfig;
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

            String servicePath = rootPath + "/" + getRegistryContent();
            if (!zkClient.exists(servicePath))
            {
                zkClient.createEphemeral(servicePath);
                log.info("注册 transferAddress 成功，msg=[{}]", servicePath);
            }
        } catch (Exception e)
        {
            log.error("register transfer address failure", e);
        }
    }

    private String getRegistryContent() throws UnknownHostException
    {
        String localAddress = InetAddress.getLocalHost().getHostAddress();
        JSONObject route = new JSONObject();
        route.put("ip", localAddress);
        route.put("port",transferConfig.getServerPort());
        return route.toJSONString();
    }
}
