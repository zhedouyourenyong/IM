package com.zzh.request;

import lombok.Data;

@Data
public class LoginRequestVO
{
    private Long userId;
    private String userName;

    public LoginRequestVO()
    {
    }

    public LoginRequestVO (Long userID, String userName)
    {
        this.userId = userID;
        this.userName = userName;
    }
}
