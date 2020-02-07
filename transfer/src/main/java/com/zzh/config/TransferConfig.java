package com.zzh.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Data
@Component
public class TransferConfig
{
    @Value("${im.server.port}")
    private int ServerPort;

    @Value("${im.heartbeat.time}")
    private int heartTime;
}
