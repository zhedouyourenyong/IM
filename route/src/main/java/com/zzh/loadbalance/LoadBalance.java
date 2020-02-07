package com.zzh.loadbalance;

import java.util.List;


public interface LoadBalance
{

    /**
     * 在一批服务器里进行路由
     */
    String routeServer(List<String> values) ;
}
