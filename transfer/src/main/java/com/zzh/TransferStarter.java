package com.zzh;

import com.zzh.registery.ZKTransferAddressRegister;
import com.zzh.server.TransferServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 负责transfer的启动工作
 * @Author: Administrator
 * @Date: 2020/1/29 19:04
 */
@Slf4j
@Component
public class TransferStarter implements CommandLineRunner
{
    private TransferServer transferServer;
    private ZKTransferAddressRegister addressRegister;

    @Autowired
    public TransferStarter(TransferServer transferServer, ZKTransferAddressRegister addressRegister)
    {
        this.transferServer = transferServer;
        this.addressRegister = addressRegister;
    }

    @Override
    public void run(String... args) throws Exception
    {
        log.info("begin start transfer");
        try
        {
            transferServer.start();

            addressRegister.registry();
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        log.info("start transfer success");
    }
}
