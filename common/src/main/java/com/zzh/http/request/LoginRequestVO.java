package com.zzh.http.request;

import lombok.Data;

@Data
public class LoginRequestVO
{
    private String name;
    private String password;

    public LoginRequestVO(String name, String password)
    {
        this.name = name;
        this.password = password;
    }
}
