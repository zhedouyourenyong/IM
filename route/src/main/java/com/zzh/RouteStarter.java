package com.zzh;

import com.zzh.discovery.ZKServerAddressDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: route启动工作
 * @Author: Administrator
 * @Date: 2020/1/29 19:08
 */
@Slf4j
@Component
public class RouteStarter implements CommandLineRunner
{

    private ZKServerAddressDiscovery zkServerAddressDiscovery;

    @Autowired
    public RouteStarter(ZKServerAddressDiscovery zkServerAddressRegistry)
    {
        this.zkServerAddressDiscovery = zkServerAddressRegistry;
    }

    @Override
    public void run(String... args) throws Exception
    {
        try
        {
            zkServerAddressDiscovery.discovery();
        } catch (Exception e)
        {
            log.error(e.getMessage());
        }
    }
}
