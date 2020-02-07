package com.zzh.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2020/2/6 18:01
 */
@Data
@Configuration
public class ZkConfig
{
    @Value("${zk.address}")
    private String zkAddress;

    @Value("${zk.session.timeout}")
    private int sessionTimeout;

    @Value("${zk.connection.timeout}")
    private int connectionTimeout;

    @Value("${zk.registry.root}")
    private String registryRootPath;

    @Value("${zk.transfer.root}")
    private String transferRoot;
}
