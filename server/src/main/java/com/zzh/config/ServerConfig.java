package com.zzh.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class ServerConfig
{
    @Value("${im.server.port}")
    private int ServerPort;

    @Value("${im.web.port}")
    private int WebPort;

    @Value("${im.netty.bossThreads}")
    private int bossThreads;

    @Value("${im.netty.workerThreads}")
    private int workerThreads;

    @Value("${im.netty.epoll}")
    private boolean useEpoll;

    @Value("${zk.address}")
    private String zkAddress;

    @Value("${zk.session.timeout}")
    private int sessionTimeout;

    @Value("${zk.connection.timeout}")
    private int connectionTimeout;


    @Value("${zk.registry.root}")
    private String registryRootPath;

    @Value("${im.heartbeat.time}")
    private int heartTime;

    @Value("${im.clear.route.request.url}")
    private String offLineURL;

}
