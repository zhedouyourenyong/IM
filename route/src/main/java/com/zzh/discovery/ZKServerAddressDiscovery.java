package com.zzh.discovery;


import com.zzh.config.RouteConfig;
import com.zzh.interf.DiscoveryClient;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Function: Zookeeper 工具
 * 监听服务数据，对外提供获取缓存的方法
 */
@Slf4j
@Component
public class ZKServerAddressDiscovery implements DiscoveryClient
{
    private ZkClient zkClient;
    private RouteConfig config;
    private ServerAddressCache cache;

    @Autowired
    public ZKServerAddressDiscovery(ZkClient zkClient, RouteConfig config, ServerAddressCache cache)
    {
        this.zkClient = zkClient;
        this.config = config;
        this.cache = cache;
    }


    @Override
    public void discovery()
    {
        synchronized (ZKServerAddressDiscovery.class)
        {
            /**
             * 初始化缓存
             */
            List<String> serverAddress = zkClient.getChildren(config.getRegistryRootPath());
            log.info("获取到的服务端地址信息:{}", Arrays.toString(serverAddress.toArray()));
            cache.updateCache(serverAddress);

            /**
             * 监听注册服务的变化,同时更新数据到本地缓存
             */
            zkClient.subscribeChildChanges(config.getRegistryRootPath(), (parentPath, currentChilds) -> {
                log.info("清除/更新本地缓存 parentPath={},currentChilds={}", parentPath, currentChilds.toString());

                //更新所有缓存/先删除 再新增
                cache.updateCache(currentChilds);
            });
        }
    }

}
