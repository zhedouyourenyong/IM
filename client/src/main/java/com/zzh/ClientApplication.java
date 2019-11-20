package com.zzh;

import com.zzh.request.LoginRequestVO;
import com.zzh.service.RouteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ClientApplication implements CommandLineRunner
{
    private final static Logger logger = LoggerFactory.getLogger(ClientApplication.class);

    @Autowired
    RouteRequest routeRequest;

    public static void main (String[] args)
    {
        SpringApplication.run(ClientApplication.class, args);
    }


    @Override
    public void run (String... args) throws Exception
    {
        LoginRequestVO loginRequestVO = new LoginRequestVO(123L, "zzh");
        String str = routeRequest.getIMServer(loginRequestVO).toString();
        System.out.println(str);
    }
}