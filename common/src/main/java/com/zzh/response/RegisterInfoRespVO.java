package com.zzh.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterInfoRespVO implements Serializable
{
    private Long userId;
    private String userName;

    public RegisterInfoRespVO (Long userId, String userName)
    {
        this.userId = userId;
        this.userName = userName;
    }
}
