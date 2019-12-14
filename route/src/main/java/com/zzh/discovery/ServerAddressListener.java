package com.zzh.discovery;

import com.zzh.discovery.impl.ZKServerAddressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerAddressListener
{
    private static final Logger logger= LoggerFactory.getLogger(ServerAddressListener.class);

    @Autowired
    private ZKServerAddressListener listener;


    public void start ()
    {
        Thread thread=new Thread(()->{
            try
            {
                logger.info("开启初始化服务端地址列表");
                listener.initServerAddressList();
                logger.info("初始化服务端地址列表成功");
            }catch (Exception e)
            {
                logger.error(e.getMessage(),e);
            }
        });
        thread.setName("ServerAddressListener-thread");
        thread.start();
    }
}
