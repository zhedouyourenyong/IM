package com.zzh.discovery.impl;


import com.google.common.cache.LoadingCache;
import com.zzh.config.RouteConfig;
import com.zzh.discovery.ServerAddressInvoker;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Function: Zookeeper 工具
 * 监听服务数据，对外提供获取缓存的方法
 */
@Component
public class ZKServerAddressListener implements ServerAddressInvoker
{
    private static final Logger logger = LoggerFactory.getLogger(ZKServerAddressListener.class);

    @Autowired
    private ZkClient zkClient;
    @Autowired
    private LoadingCache<String, String> cache;
    @Autowired
    private RouteConfig config;


    @Override
    public void initServerAddressList()
    {
        synchronized (ZKServerAddressListener.class)
        {
            List<String> serverAddress = getAll(config.getRegistryRootPath());
            logger.info("获取到的服务端地址:{}",serverAddress.toArray().toString());
            updateCache(serverAddress);

            /**
             * 监听注册服务的变化,同时更新数据到本地缓存
             */
            zkClient.subscribeChildChanges(config.getRegistryRootPath(), (parentPath, currentChilds) -> {
                logger.debug("清除/更新本地缓存 parentPath={},currentChilds={}", parentPath, currentChilds.toString());

                //更新所有缓存/先删除 再新增
                updateCache(currentChilds);
            });
        }
    }

    public List<String> getAll()
    {
        if (cache.size() == 0)
        {
            synchronized (ZKServerAddressListener.class)
            {
                if (cache.size() == 0)
                {
                    List<String> curServerLists = getAll(config.getRegistryRootPath());
                    for (String node : curServerLists)
                    {
                        String key = node.split("-")[1];
                        addCache(key);
                    }
                }
            }
        }

        List<String> list = new ArrayList<>((int) cache.size());
        for (Map.Entry<String, String> entry : cache.asMap().entrySet())
        {
            list.add(entry.getKey());
        }
        return list;
    }


    private void addCache(String key)
    {
        cache.put(key, key);
    }

    private List<String> getAll(String path)
    {
        return zkClient.getChildren(path);
    }

    /**
     * 更新所有缓存/先删除 再新增
     *    /route/ip-192.168.64.81:8899:8081
     **/
    private void updateCache(List<String> currentChilds)
    {
        logger.debug("begin update ServerAddress Cache");
        cache.invalidateAll();
        for (String currentChild : currentChilds)
        {
            String key = currentChild.split("-")[1];
            addCache(key);
        }
        logger.debug("update ServerAddress success");
    }

}
