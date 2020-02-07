package com.zzh;


import com.zzh.util.IdUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IMServerApplication
{
    /**
     * 唯一识别ID
     */
    public static final String SERVER_ID = IdUtil.uuid();


    public static void main (String[] args)
    {
        SpringApplication.run(IMServerApplication.class, args);
    }
}