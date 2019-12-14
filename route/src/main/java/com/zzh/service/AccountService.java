package com.zzh.service;


import com.alibaba.fastjson.JSONObject;


public interface AccountService
{

    /**
     * 注册用户
     *
     * @param
     * @return
     * @throws Exception
     */
    JSONObject register(String userName, String password) throws Exception;

    /**
     * 登录服务
     *
     * @param
     * @throws Exception
     */
    JSONObject login(String userName, String password) throws Exception;



    /**
     * 用户下线
     *
     * @param userId 下线用户ID
     * @throws Exception
     */
    void offLine(String userId) throws Exception;
}
