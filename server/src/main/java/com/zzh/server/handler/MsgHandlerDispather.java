package com.zzh.server.handler;

import com.zzh.protobuf.Msg;
import com.zzh.util.SpringBeanFactory;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 具体的消息是在这里处理，使用线程池
 * @Author: Administrator
 * @Date: 2019/12/14 15:52
 */
@Service
public class MsgHandlerDispather
{
    private static final Logger logger = LoggerFactory.getLogger(MsgHandlerDispather.class);


    private final Map<Msg.MsgType, ServerHandlerInterface> handlerMap = new HashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(8);

    private void initHandlerMap()
    {
        try
        {
            Map<String, ServerHandlerInterface> handlers = SpringBeanFactory.getBeansOfType(ServerHandlerInterface.class);
            if (handlers != null && handlers.size() != 0)
            {
                for (Map.Entry<String, ServerHandlerInterface> entry : handlers.entrySet())
                {
                    ServerHandlerInterface handler = entry.getValue();
                    handlerMap.put(handler.getMsgType(), handler);
                }
            } else
            {
                logger.debug("handlersMap is null or size is 0");
            }

        } catch (Exception e)
        {
            logger.error("MagHandlerDispather init fail");
        }
    }

    /**
     * 具体的消息处理
     *
     * @param ctx
     * @param msg
     */
    public void processMsg(ChannelHandlerContext ctx, Msg.Protocol msg)
    {
        if (handlerMap == null)
        {
            initHandlerMap();
        }
        executorService.submit(() -> {
            try
            {
                Msg.MsgType msgType = msg.getMsgHead().getMsgType();
                if (msgType != null)
                {
                    ServerHandlerInterface msgHandler = handlerMap.get(msgType);
                    if (null != msgHandler)
                    {
                        msgHandler.process(ctx, msg);
                    } else
                    {
                        logger.error("{} handler is null", msg.getMsgHead().getMsgType());
                    }
                } else
                {
                    logger.error("message not have msgType,msg:{}", msg.toString());
                }
            } catch (Exception e)
            {
                logger.error(e.getMessage(), e);
            }
        });
    }
}
