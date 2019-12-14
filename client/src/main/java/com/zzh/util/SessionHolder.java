package com.zzh.util;

import lombok.Data;

import java.util.Date;

@Data
public class SessionHolder
{
    public static final SessionHolder INSTANCE = new SessionHolder();

    private String userName;
    private long userId;
    private String serviceInfo;
    private Date startDate;

    public void saveUserInfo (long userId, String userName)
    {
        this.userId=userId;
        this.userName=userName;
    }
}
