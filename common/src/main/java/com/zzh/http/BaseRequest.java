package com.zzh.http;


import lombok.Data;

@Data
public class BaseRequest
{

    //唯一请求号
    private String reqNo;

    //当前请求的时间戳
    private int timeStamp;


    public BaseRequest ()
    {
        this.setTimeStamp((int) (System.currentTimeMillis() / 1000));
    }

}
