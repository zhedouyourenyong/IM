package com.zzh.http.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterResponse implements Serializable
{
    private Long userId;
    private String userName;

    public RegisterResponse(Long userId, String userName)
    {
        this.userId = userId;
        this.userName = userName;
    }
}
