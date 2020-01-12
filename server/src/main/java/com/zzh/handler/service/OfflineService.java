package com.zzh.handler.service;

import com.zzh.protobuf.Protocol;
import com.zzh.handler.ServerHandlerInterface;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 处理用户下线请求
 * @Author: Administrator
 * @Date: 2019/12/14 16:20
 */
@Component
public class OfflineService implements ServerHandlerInterface
{

    @Override
    public void process(ChannelHandlerContext ctx, Protocol.Msg msg) throws Exception
    {

    }

    @Override
    public Protocol.MsgType getMsgType()
    {
        return Protocol.MsgType.OFFLINE;
    }
}
