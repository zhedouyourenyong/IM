package com.zzh.discovery;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.LoadingCache;
import com.zzh.config.RouteConfig;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2020/2/5 18:48
 */
@Slf4j
@Component
public class ServerAddressCache
{
    private LoadingCache<String, String> cache;
    private RouteConfig config;
    private ZkClient zkClient;

    @Autowired
    public ServerAddressCache(LoadingCache<String, String> cache, RouteConfig config, ZkClient zkClient)
    {
        this.cache = cache;
        this.config = config;
        this.zkClient = zkClient;
    }

    public List<String> getAll()
    {
        if (cache.size() == 0)
        {
            synchronized (ZKServerAddressDiscovery.class)
            {
                if (cache.size() == 0)
                {
                    List<String> curServerLists = zkClient.getChildren(config.getRegistryRootPath());
                    updateCache(curServerLists);
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


    /**
     * 更新所有缓存/先删除 再新增
     * /server/{"ip":"192.168.201.1","serverPort":8899,"serverId":"4eaafb51"}
     **/
    public void updateCache(List<String> currentChilds)
    {
        try
        {
            cache.invalidateAll();
            for (String currentChild : currentChilds)
            {
                addCache(currentChild);
            }
        } catch (Exception e)
        {
            log.error("更新ServerAddress缓存出现异常", e);
        }
    }

}
