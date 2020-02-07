package com.zzh.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2020/2/3 22:54
 */
@Data
@Component
@ConfigurationProperties(prefix = "im")
public class UserConfig
{
    private String name;
    private String password;
}
