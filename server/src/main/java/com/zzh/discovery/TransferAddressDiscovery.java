package com.zzh.discovery;

import com.zzh.config.ZkConfig;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 提供transfer木块的地址
 * @Author: Administrator
 * @Date: 2020/2/6 19:21
 */
@Slf4j
@Component
public class TransferAddressDiscovery
{
    private ZkClient zkClient;
    private ZkConfig zkConfig;

    @Autowired
    public TransferAddressDiscovery(ZkClient zkClient, ZkConfig zkConfig)
    {
        this.zkClient = zkClient;
        this.zkConfig = zkConfig;
    }

    public List<String> getTransferAddress()
    {
        List<String> address = zkClient.getChildren(zkConfig.getTransferRoot());
        return address;
    }
}
