package com.zzh.service;


import com.zzh.pojo.User;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(用户登录状态相关服务)
 * @Author: Administrator
 * @Date: 2019/12/14 12:58
 */
public interface UserStatusService
{
    /**
     * 保存用户登录情况
     *
     * @param userId userId 用户唯一 ID
     * @return true 为可以登录 false 为已经登录
     * @throws Exception
     */
    boolean saveUserLoginStatus(String userId) throws Exception;

    /**
     * 检查用户登录状态
     * @param userId
     * @return
     * @throws Exception
     */
    boolean checkUserLoginStatus(String userId) throws Exception;


    /**
     * 清除用户的登录状态
     *
     * @param userId
     * @throws Exception
     */
    void removeUserLoginStatus(String userId) throws Exception;

}
