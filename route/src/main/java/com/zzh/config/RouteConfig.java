package com.zzh.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class RouteConfig
{

    @Value("${zk.address}")
    private String zkAddress;

    @Value("${zk.session.timeout}")
    private int sessionTimeout;

    @Value("${zk.connection.timeout}")
    private int connectionTimeout;

    @Value("${zk.registry.root}")
    private String registryRootPath;

}
