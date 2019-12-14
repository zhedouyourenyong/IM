package com.zzh.handler.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzh.constant.Constant;
import com.zzh.protobuf.Msg;
import com.zzh.util.ClientChannelContext;
import com.zzh.util.NettyAttrUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(处理greet消息)
 * @Author: Administrator
 * @Date: 2019/12/10 10:36
 */
@Service
public class GreetService extends AbstractMsgHandler
{
    private static final Logger logger = LoggerFactory.getLogger(GreetService.class);

    @Override
    public void checkMsg(Msg.Protocol msg)
    {
        if (msg.getMsgHead().getMsgType() != Msg.MsgType.GREET)
        {
            throw new RuntimeException("Message type validation failed");
        }
    }

    /**
     * GREET 建立客户端与 Channel 之间的映射,每个channel一个ack窗口，并建立会话ID起始值返回给客户端
     */
    @Override
    public void processMsg(Channel channel, Msg.Protocol msg)
    {
        logger.info("begin process GreetMsg");
        JSONObject userInfo = JSON.parseObject(msg.getMsgBody());
        String userId = userInfo.getString(Constant.USER_ID);
        String userName = userInfo.getString(Constant.USER_NAME);

        ClientChannelContext.put(userId, channel);
        ClientChannelContext.saveSession(userId, userName);

        NettyAttrUtil.setUserId(channel, userId);
        NettyAttrUtil.initAckWindow(channel);
        Long tid = NettyAttrUtil.initTid(channel);

        JSONObject greetResp = new JSONObject();
        greetResp.put(Constant.ITd, tid);

        channel.writeAndFlush(getGreetResponse(greetResp));
        logger.info("客户端[{}]上线成功", userInfo.getString("userName"));
    }

    private Msg.Protocol getGreetResponse(JSONObject greetBody)
    {
        Msg.Head head = Msg.Head.newBuilder()
                .setMsgType(Msg.MsgType.GREET)
                .build();
        Msg.Protocol greetResp = Msg.Protocol.newBuilder()
                .setMsgHead(head)
                .setMsgBody(greetBody.toString())
                .build();
        return greetResp;
    }
}
