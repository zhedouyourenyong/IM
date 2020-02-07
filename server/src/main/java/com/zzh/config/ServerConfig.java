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

    @Value("${im.clear.route.request.url}")
    private String offLineURL;

}
