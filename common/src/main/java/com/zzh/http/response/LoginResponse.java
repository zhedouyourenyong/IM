package com.zzh.http.response;

import com.zzh.pojo.RouteInfo;
import lombok.Data;

/**
 * code : 9000
 * message : 成功
 * reqNo : null
 * dataBody : {"ip":"127.0.0.1","port":8081}
 */
@Data
public class LoginResponse
{
    private Long userId;
    private RouteInfo routeInfo;
}
