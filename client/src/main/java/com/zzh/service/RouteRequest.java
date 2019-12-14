package com.zzh.service;

import com.zzh.request.LoginRequestVO;
import com.zzh.pojo.RouteInfo;

public interface RouteRequest
{
    RouteInfo getIMServer (LoginRequestVO loginRequestVO) throws Exception;
}
