package com.zzh.constant;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(一句话描述该类的功能)
 * @Author: Administrator
 * @Date: 2019/12/10 15:42
 */
public interface RedisKey
{
    /**
     * 账号前缀
     */
    String USER_INFO_PREFIX = "im-userInfo:";

    /**
     * 路由信息前缀
     */
    String ROUTE_PREFIX = "im-route:";

    /**
     * 登录状态前缀
     */
    String LOGIN_STATUS_PREFIX = "login-status";

    /**
     * 检查幂等性
     */
    String CONSUMED_MESSAGE="consumed-message";
}
