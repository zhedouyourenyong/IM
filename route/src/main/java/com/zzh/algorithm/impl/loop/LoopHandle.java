package com.zzh.algorithm.impl.loop;


import com.zzh.algorithm.RouteHandle;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Function:路由策略， 循环
 */
@Component("LoopHandle")
public class LoopHandle implements RouteHandle
{
    private AtomicLong index = new AtomicLong();

    @Override
    public String routeServer(List<String> values, String key)
    {
        if (values.size() == 0)
        {
            throw new RuntimeException("IM服务器可用服务列表为空");
        }
        Long position = index.incrementAndGet() % values.size();
        if (position < 0)
        {
            position = 0L;
        }

        return values.get(position.intValue());
    }
}
