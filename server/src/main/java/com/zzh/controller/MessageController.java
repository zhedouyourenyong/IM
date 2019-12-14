package com.zzh.controller;

import com.zzh.service.MsgTransferHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(负责服务器之间的通信)
 * @Author: Administrator
 * @Date: 2019/12/9 21:39
 */
@Controller("/")
public class MessageController
{
    private static final Logger logger= LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MsgTransferHandler msgTransferHandler;

    @RequestMapping(value = "sendMsg",method = RequestMethod.POST)
    public void sendMsg()
    {

    }
}
