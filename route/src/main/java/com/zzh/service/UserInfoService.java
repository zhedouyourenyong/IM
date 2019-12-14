package com.zzh.service;

import com.zzh.pojo.User;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(用户)
 * @Author: Administrator
 * @Date: 2019/12/14 12:58
 */
public interface UserInfoService
{
    /**
     * 存储用户信息
     * @param user
     */
    void saveUserInfo(User user);

    /**
     * 删除用户信息
     * @param userId
     */
    Boolean removeUserInfoByUserId(String userId);

    /**
     * 通过userId获取UserInfo
     * @param userId
     * @return
     */
    User getUserInfoByUserId(String userId);
}
