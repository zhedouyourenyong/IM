package com.zzh.handler.service;

import com.zzh.protobuf.Msg;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(一句话描述该类的功能)
 * @Author: Administrator
 * @Date: 2019/12/10 11:05
 */
@Service
public class AckService extends AbstractMsgHandler
{
    private static final Logger logger = LoggerFactory.getLogger(AckService.class);


    @Override
    public void checkMsg(Msg.Protocol msg)
    {

    }

    @Override
    protected void processMsg(Channel channel, Msg.Protocol msg)
    {

    }
}
