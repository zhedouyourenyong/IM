package com.zzh.loadbalance.impl;


import com.zzh.loadbalance.LoadBalance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Function:路由策略， 循环
 */
@Component("LoopHandle")
public class LoopHandle implements LoadBalance
{
    private AtomicLong index = new AtomicLong();

    @Override
    public String routeServer(List<String> values)
    {
        if (values == null || values.size() == 0)
        {
            throw new RuntimeException("IM服务器可用服务列表为空");
        }
        long position = index.incrementAndGet() % values.size();
        if (position < 0)
        {
            position = 0L;
        }

        return values.get((int) position);
    }
}
