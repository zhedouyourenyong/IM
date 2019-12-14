package com.zzh.algorithm;

import java.util.List;


public interface RouteHandle
{

    /**
     * 再一批服务器里进行路由
     */
    String routeServer(List<String> values, String key) ;
}
