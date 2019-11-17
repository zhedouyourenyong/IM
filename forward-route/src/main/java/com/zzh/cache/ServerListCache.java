package com.zzh.cache;

import com.google.common.cache.LoadingCache;
import com.zzh.config.RouteConfig;
import com.zzh.discover.ZKServiceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ServerListCache
{
    private static final Logger logger = LoggerFactory.getLogger(ServerListCache.class);

    @Autowired
    private LoadingCache<String, String> cache;
    @Autowired
    private ZKServiceListener zkUtil;
    @Autowired
    private RouteConfig config;


    public void addCache (String key)
    {
        cache.put(key, key);
    }


    /**
     * 更新所有缓存/先删除 再新增
     **/
    public void updateCache (List<String> currentChilds)
    {
        cache.invalidateAll();
        for (String currentChild : currentChilds)
        {
            String key = currentChild.split("-")[1];
            addCache(key);
        }
    }

    public List<String> getAll ()
    {
        if(cache.size() == 0)
        {
            List<String> curServerLists = zkUtil.getAll(config.getRegistryRootPath());
            for (String node : curServerLists)
            {
                String key = node.split("-")[1];
                addCache(key);
            }
        }

        List<String> list = new ArrayList<>((int) cache.size());
        for (Map.Entry<String, String> entry : cache.asMap().entrySet())
        {
            list.add(entry.getKey());
        }
        return list;
    }

}
