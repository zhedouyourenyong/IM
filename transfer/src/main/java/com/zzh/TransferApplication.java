package com.zzh;


import com.zzh.server.TransferServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class TransferApplication implements CommandLineRunner
{
    private final static Logger logger = LoggerFactory.getLogger(TransferApplication.class);

    @Autowired
    private TransferServer transferServer;

    public static void main(String[] args)
    {
        SpringApplication.run(TransferApplication.class, args);
    }


    @Override
    public void run(String... args)
    {
        try
        {
            transferServer.start();
        } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }
}