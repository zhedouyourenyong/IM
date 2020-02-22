package com.zzh;

import com.zzh.core.ClientMsgListener;
import com.zzh.config.ClientConfig;
import com.zzh.protocol.Single;
import com.zzh.session.Session;
import com.zzh.util.HttpUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2020/2/21 15:48
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ClientConfig.class)
@ConditionalOnClass(MyClient.class)
@ConditionalOnProperty(prefix = "im.client", value = "enable", matchIfMissing = true)
public class ImClientStarterAutoConfiguration
{
    private ClientConfig clientConfig;

    @Autowired
    public ImClientStarterAutoConfiguration(ClientConfig clientConfig)
    {
        this.clientConfig = clientConfig;
    }

    @Bean
    @ConditionalOnMissingBean(HttpUtil.class)
    public HttpUtil httpUtil ()
    {
        return new HttpUtil();
    }

    @Bean
    @ConditionalOnMissingBean(MyClient.class)
    public MyClient myClient()
    {
        return new MyClient(clientConfig, new ClientMsgListener()
        {
            @Override
            public void online()
            {
                String userId = Session.INSTANCE.getUserId();
                log.info("用户:" + userId + " 上线!");
            }

            @Override
            public void read(Single.SingleMsg chatMsg)
            {
                String formId = chatMsg.getFromId();
                System.out.println(String.format("收到来自用户{%s}的消息:\n{%s}", formId, chatMsg.getBody()));
            }

            @Override
            public void offline()
            {
                String userId = Session.INSTANCE.getUserId();
                log.info("用户:" + userId + " 下线!");
            }

            @Override
            public void hasException(ChannelHandlerContext ctx, Throwable cause)
            {
                log.error(cause.getMessage());
                ctx.close();
            }
        });
    }
}
