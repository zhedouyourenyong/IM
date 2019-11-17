package com.zzh.discover;


import com.zzh.cache.ServerListCache;
import com.zzh.config.RouteConfig;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Function: Zookeeper 工具
 * 监听路径，将数据放在cache中。
 */
@Component
public class ZKServiceListener
{
    private static final Logger logger = LoggerFactory.getLogger(ZKServiceListener.class);

    @Autowired
    private ZkClient zkClient;
    @Autowired
    private ServerListCache ServerListCache;

    //监听事件
    public void subscribeEvent (String path)
    {
        zkClient.subscribeChildChanges(path, (parentPath, currentChilds) -> {
            logger.info("清除/更新本地缓存 parentPath=【{}】,currentChilds=【{}】", parentPath, currentChilds.toString());

            //更新所有缓存/先删除 再新增
            ServerListCache.updateCache(currentChilds);
        });
    }

    public List<String> getAll(String path)
    {
        return zkClient.getChildren(path);
    }
}
