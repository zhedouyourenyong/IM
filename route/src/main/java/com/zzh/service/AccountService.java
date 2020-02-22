package com.zzh.service;


import com.zzh.enums.StatusEnum;


public interface AccountService
{

    /**
     * 注册用户
     *
     * @param
     * @return
     * @throws Exception
     */
    StatusEnum register(String userName, String password) throws Exception;

    /**
     * 登录服务
     *
     * @param
     * @throws Exception
     */
    StatusEnum login(String userName, String password) throws Exception;


    /**
     * 用户下线
     *
     * @param userId 下线用户ID
     * @throws Exception
     */
    void offLine(String userId) throws Exception;

    /**
     * 根据用户名获取用户ID
     * @param name
     * @return
     */
    Long getUserIdByName(String name);

    /**
     * 在redis中建立用户名和用户ID的映射。
     * @param name
     * @param id
     */
    void setUserIdByName(String name,Long id);
}
