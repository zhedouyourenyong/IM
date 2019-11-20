package com.zzh.response;

import lombok.Data;

@Data
public class ServerInfo
{
    private String ip;
    private String serverPort;
    private String httpPort;
}
