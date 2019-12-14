package com.zzh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RouteApplication implements CommandLineRunner
{
    private final static Logger logger = LoggerFactory.getLogger(RouteApplication.class);

    @Autowired
    RouteStarter routeStarter;

    public static void main (String[] args)
    {
        SpringApplication.run(RouteApplication.class, args);
    }

    /**
     * 开启监听工作
     * @param args
     * @throws Exception
     */
    @Override
    public void run (String... args) throws Exception
    {
        routeStarter.start();
    }
}