package com.zzh.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2020/2/18 18:00
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "im")
public class ClientConfig
{
    private String routeUrl = "http://112.74.62.176:8091/login";
    private String name;
    private String password;
}
