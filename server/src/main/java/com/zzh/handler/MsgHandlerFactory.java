package com.zzh.handler;

import com.google.protobuf.Message;
import com.zzh.util.SpringBeanFactory;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2019/12/14 15:52
 */
@Slf4j
@Service
public class MsgHandlerFactory
{
    private Map<Class<?>, MsgHandler> handlerMap;

    private void initHandlerMap()
    {
        try
        {
            Map<String, MsgHandler> handlers = SpringBeanFactory.getBeansOfType(MsgHandler.class);

            log.info(handlers.toString());

            handlerMap = new HashMap<>(handlers.size());
            if (handlers.size() != 0)
            {
                for (Map.Entry<String, MsgHandler> entry : handlers.entrySet())
                {
                    MsgHandler handler = entry.getValue();
                    handlerMap.put(handler.getProcessedType(), handler);
                }
            } else
            {
                log.error("handlersMap is null or size is 0");
            }

        } catch (Exception e)
        {
            log.error("MsgHandlerFactory initHandlerMap fail !", e);
        }
    }

    public MsgHandler getHandlerByMsgClass(Class<?> clazz)
    {
        if (handlerMap == null)
        {
            initHandlerMap();
        }
        return handlerMap.get(clazz);
    }
}