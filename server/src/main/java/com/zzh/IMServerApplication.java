package com.zzh;

import com.zzh.registry.impl.ZkServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class IMServerApplication implements CommandLineRunner
{
    private final static Logger logger = LoggerFactory.getLogger(IMServerApplication.class);


    public static void main (String[] args)
    {
        SpringApplication.run(IMServerApplication.class, args);
    }

    /*
      将本机 ip+port注册到Zookeeper
     */
    @Override
    public void run (String... args) throws Exception
    {
        Thread thread = new Thread(new ZkServiceRegistry());
        thread.setName("zk-registry");
        thread.start();
    }
}