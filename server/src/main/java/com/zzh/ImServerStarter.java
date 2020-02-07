package com.zzh;

import com.zzh.config.ServerConfig;
import com.zzh.discovery.TransferAddressDiscovery;
import com.zzh.registery.ZKServerAddressRegister;
import com.zzh.server.IMServer;
import com.zzh.transfer.TransferClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 启动工作
 * @Author: Administrator
 * @Date: 2020/1/29 19:16
 */
@Slf4j
@Component
public class ImServerStarter implements CommandLineRunner
{
    private IMServer imServer;
    private ZKServerAddressRegister addressRegister;
    private TransferClient transferClient;
    private TransferAddressDiscovery transferAddressDiscovery;

    @Autowired
    public ImServerStarter(IMServer imServer, ZKServerAddressRegister addressRegister, ServerConfig serverConfig, TransferClient transferClient, TransferAddressDiscovery transferAddressDiscovery)
    {
        this.imServer = imServer;
        this.addressRegister = addressRegister;
        this.transferClient = transferClient;
        this.transferAddressDiscovery = transferAddressDiscovery;
    }

    @Override
    public void run(String... args)
    {
        try
        {
            List<String> transferAddress = transferAddressDiscovery.getTransferAddress();
            transferClient.start(transferAddress);

            imServer.start();

            addressRegister.registry();
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }
}
