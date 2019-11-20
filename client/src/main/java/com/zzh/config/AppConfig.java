package com.zzh.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AppConfig
{
    @Value("${im.server.route.request.url}")
    private String serverRouteLoginUrl;

}
