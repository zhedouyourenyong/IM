package com.zzh.http.response;

import com.zzh.pojo.RouteInfo;
import lombok.Data;
import lombok.NonNull;

/**
 * code : 9000
 * message : 成功
 * reqNo : null
 * dataBody : {"ip":"127.0.0.1","port":8081}
 */
@Data
public class LoginResponse
{
    private boolean success;
    private String reason;
    private String userId;
    private RouteInfo routeInfo;

}
