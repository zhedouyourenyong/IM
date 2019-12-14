package com.zzh.config;

import com.zzh.util.IdUtil;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class BeanConfig
{
    private final static Logger LOGGER = LoggerFactory.getLogger(BeanConfig.class);


    @Bean
    public OkHttpClient okHttpClient ()
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        return builder.build();
    }


//    @Bean(value = "pingHeart")
//    public Internal.InternalMsg heartBeat ()
//    {
//        Internal.InternalMsg heart = Internal.InternalMsg.newBuilder()
//                .setId(IdUtil.snowGenId())
//                .setFrom(Internal.InternalMsg.Module.CLIENT)
//                .setDest(Internal.InternalMsg.Module.SERVER)
//                .setCreateTime(System.currentTimeMillis())
//                .setVersion(MsgVersion.V1.getVersion())
//                .setMsgType(Internal.InternalMsg.MsgType.GREET)
//                .setMsgBody("ping")
//                .build();
//        return heart;
//    }
}
