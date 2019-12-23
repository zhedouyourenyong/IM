package com.zzh;


import com.zzh.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class IMServerApplication implements CommandLineRunner
{
    private final static Logger logger = LoggerFactory.getLogger(IMServerApplication.class);

    /**
     * 唯一识别ID
     */
    public static final String SERVER_ID = IdUtil.uuid();

    @Autowired
    private ServerStarter serverStarter;


    public static void main (String[] args)
    {
        SpringApplication.run(IMServerApplication.class, args);
    }


    @Override
    public void run (String... args)
    {
        serverStarter.start();
    }
}