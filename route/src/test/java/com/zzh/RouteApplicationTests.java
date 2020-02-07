package com.zzh;

import com.zzh.mapper.UserDao;
import org.I0Itec.zkclient.ZkClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2020/2/5 20:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RouteApplicationTests
{
    @Autowired
    public UserDao userDao;
    @Autowired
    private ZkClient zkClient;

    @Test
    public void contextLoads()
    {
//        zkClient.delete("/route");

        List<String> children=zkClient.getChildren("/");
        System.out.println("begin...");
        for(String s:children)
        {
            System.out.println(s);
        }
        System.out.println("end...");
    }
}
