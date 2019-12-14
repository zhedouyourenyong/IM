package com.zzh.request;


import com.zzh.request.BaseRequest;
import lombok.Data;

@Data
public class RegisterInfoReqVO extends BaseRequest
{
    private String userName;
    private String password;
}
