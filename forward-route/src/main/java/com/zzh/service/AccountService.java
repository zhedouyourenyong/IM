package com.zzh.service;


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
    Map<String,Object> register (RegisterInfoReqVO userInfo) throws Exception;

    /**
     * 登录服务
     *
     * @param loginReqVO 登录信息
     * @return true 成功 false 失败
     * @throws Exception
     */
    StatusEnum login (LoginRequestVO loginReqVO) throws Exception;

    /**
     * 保存路由信息
     *
     * @param msg        服务器信息
     * @param loginReqVO 用户信息
     * @throws Exception
     */
    void saveRouteInfo (LoginRequestVO loginReqVO, String msg) throws Exception;

//    /**
//     * 加载所有用户的路有关系
//     *
//     * @return 所有的路由关系
//     */
//    Map<Long, CIMServerResVO> loadRouteRelated ();
//
//    /**
//     * 获取某个用户的路有关系
//     *
//     * @param userId
//     * @return 获取某个用户的路有关系
//     */
//    CIMServerResVO loadRouteRelatedByUserId (Long userId);
//

    /**
     * 用户下线
     *
     * @param userId 下线用户ID
     * @throws Exception
     */
    void offLine (Long userId) throws Exception;
}
