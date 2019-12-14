package com.zzh.controller;

import com.alibaba.fastjson.JSONObject;
import com.zzh.constant.Constant;
import com.zzh.service.UserStatusService;
import org.apache.tomcat.util.bcel.Const;
import org.apache.tomcat.util.descriptor.web.WebXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(一句话描述该类的功能)
 * @Author: Administrator
 * @Date: 2019/12/14 13:40
 */
@Controller("/")
public class UserStatusController
{
    private static final Logger logger = LoggerFactory.getLogger(UserStatusController.class);

    @Autowired
    private UserStatusService userStatusService;

    @RequestMapping(value = "checkUserLoginStatus", method = RequestMethod.POST)
    public String checkUserLoginStatus(@RequestBody JSONObject req)
    {
        JSONObject resp = new JSONObject();
        try
        {
            Boolean login = userStatusService.checkUserLoginStatus(req.getString(Constant.USER_ID));
            resp.put(Constant.STATUS, 0);
            resp.put(Constant.MESSAGE, login.toString());
        } catch (Exception e)
        {
            resp.put(Constant.STATUS, -1);
            logger.error(e.getMessage(), e);
        }
        return resp.toJSONString();
    }

}
