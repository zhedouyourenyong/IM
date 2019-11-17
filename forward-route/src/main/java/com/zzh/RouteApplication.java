package com.zzh;

import com.zzh.discover.ServerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RouteApplication implements CommandLineRunner
{
    private final static Logger logger = LoggerFactory.getLogger(RouteApplication.class);

    public static void main (String[] args)
    {
        SpringApplication.run(RouteApplication.class, args);
    }

    @Override
    public void run (String... args) throws Exception
    {
        Thread thread=new Thread(new ServerListener());
        thread.setName("ServerListener-Thread");
        thread.start();
    }
}