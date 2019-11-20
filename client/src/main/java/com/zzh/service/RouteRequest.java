package com.zzh.service;

import com.zzh.request.LoginRequestVO;
import com.zzh.response.ServerInfo;

public interface RouteRequest
{
    ServerInfo getIMServer (LoginRequestVO loginRequestVO) throws Exception;
}
