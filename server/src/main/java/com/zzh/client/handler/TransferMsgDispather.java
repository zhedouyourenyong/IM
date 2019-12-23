package com.zzh.client.handler;

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
 * @Description:
 * @Author: Administrator
 * @Date: 2019/12/21 21:48
 */
@Service
public class TransferMsgDispather
{
    private static final Logger logger = LoggerFactory.getLogger(TransferMsgDispather.class);


    private Map<Msg.MsgType, TransferHandlerInterface> handlerMap;
    private ExecutorService executorService = Executors.newFixedThreadPool(8);

    private void initHandlerMap()
    {
        try
        {
            Map<String, TransferHandlerInterface> handlers = SpringBeanFactory.getBeansOfType(TransferHandlerInterface.class);
            handlerMap = new HashMap<>(handlers.size());
            if (handlers.size() != 0)
            {
                for (Map.Entry<String, TransferHandlerInterface> entry : handlers.entrySet())
                {
                    TransferHandlerInterface handler = entry.getValue();
                    handlerMap.put(handler.getMsgType(), handler);
                }
            } else
            {
                logger.debug("transferHandlersMap size is 0");
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
                    TransferHandlerInterface msgHandler = handlerMap.get(msgType);
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
