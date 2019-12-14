package com.zzh.service;


import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.zzh.enums.StatusEnum;
import com.zzh.request.LoginRequestVO;
import com.zzh.request.RegisterInfoReqVO;

import java.util.Map;


public interface AccountService
{

    /**
     * 注册用户
     *
     * @param
     * @return
     * @throws Exception
     */
    JSONObject register (String userName, String password) throws Exception;

    /**
     * 登录服务
     *
     * @param
     * @throws Exception
     */
    JSONObject login (String userName, String password) throws Exception;



    /**
     * 用户下线
     *
     * @param userId 下线用户ID
     * @throws Exception
     */
    void offLine (String userId) throws Exception;
}
