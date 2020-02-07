package com.zzh.http.request;


import com.zzh.http.BaseRequest;
import lombok.Data;

@Data
public class RegisterRequest extends BaseRequest
{
    private String userName;
    private String password;
}
