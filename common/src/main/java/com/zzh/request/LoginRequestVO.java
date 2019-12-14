package com.zzh.request;

import lombok.Data;

@Data
public class LoginRequestVO extends BaseRequest
{
    private String userId;
    private String userName;
    private String password;

    public LoginRequestVO()
    {
    }

    public LoginRequestVO(String userName, String password)
    {
        this.password = password;
        this.userName = userName;
    }
}
