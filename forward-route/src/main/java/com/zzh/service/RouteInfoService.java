package com.zzh.service;

import com.zzh.request.LoginRequestVO;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(用户路由信息的相关服务)
 * @Author: Administrator
 * @Date: 2019/12/11 14:43
 */
public interface RouteInfoService
{
    /**
     * 通过userId获取用户路由信息
     * @param userId
     * @return
     * @throws Exception
     */
    String getRouteInfoByUserId(String userId) throws Exception;

    /**
     * 保存路由信息
     *
     * @param routeInfo  服务器信息
     * @param userId     用户信息
     * @throws Exception
     */
    void saveRouteInfo (String userId, String routeInfo) throws Exception;

    /**
     * 删除用户路由信息
     * @param userId
     * @throws Exception
     */
    void removeRouteInfoByUserId(String userId) throws Exception;
}
