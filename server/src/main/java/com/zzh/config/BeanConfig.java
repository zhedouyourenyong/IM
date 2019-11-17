package com.zzh.config;


import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig
{
    @Autowired
    ServerConfig serverConfig;

    @Bean
    public ZkClient buildZKClient()
    {
        return new ZkClient(serverConfig.getZkAddress(), serverConfig.getSessionTimeout(), serverConfig.getConnectionTimeout());
    }
}
