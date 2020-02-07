package com.zzh;


import com.zzh.util.IdUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ClientApplication
{
    public static final String CLIENT_ID = IdUtil.uuid();

    public static void main(String[] args)
    {
        SpringApplication.run(ClientApplication.class, args);
    }
}