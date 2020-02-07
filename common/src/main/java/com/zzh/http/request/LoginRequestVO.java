package com.zzh.http.request;

import com.zzh.http.BaseRequest;
import lombok.Data;

@Data
public class LoginRequestVO extends BaseRequest
{
    private String name;
    private String password;

    public LoginRequestVO()
    {

    }

    public LoginRequestVO(String name, String password)
    {
        this.name = name;
        this.password = password;
    }
}
