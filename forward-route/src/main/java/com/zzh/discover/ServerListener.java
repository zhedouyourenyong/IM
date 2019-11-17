package com.zzh.discover;

import com.zzh.config.RouteConfig;
import com.zzh.util.SpringBeanFactory;

public class ServerListener implements Runnable
{
    private ZKServiceListener listener;
    private RouteConfig config;

    public ServerListener ()
    {
        listener = SpringBeanFactory.getBean(ZKServiceListener.class);
        config = SpringBeanFactory.getBean(RouteConfig.class);
    }

    @Override
    public void run ()
    {
        listener.subscribeEvent(config.getRegistryRootPath());
    }
}
