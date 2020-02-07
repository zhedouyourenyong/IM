package com.zzh.loadbalance.impl;


import com.zzh.loadbalance.LoadBalance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Function: 路由策略， 随机
 */
@Component("RandomHandle")
public class RandomHandle implements LoadBalance
{

    @Override
    public String routeServer (List<String> values)
    {
        int size = values.size();
        if(size == 0)
        {
            throw new RuntimeException("IM服务器可用服务列表为空");
        }
        int offset = ThreadLocalRandom.current().nextInt(size);

        return values.get(offset);
    }
}
