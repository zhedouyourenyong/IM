package com.zzh;

import com.zzh.discovery.ServerAddressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(一句话描述该类的功能)
 * @Author: Administrator
 * @Date: 2019/12/11 13:49
 */
@Component
public class RouteStarter
{
    private static final Logger logger = LoggerFactory.getLogger(RouteStarter.class);

    @Autowired
    private ServerAddressListener addressListener;

    public void start()
    {
        addressListener.start();
    }

}
