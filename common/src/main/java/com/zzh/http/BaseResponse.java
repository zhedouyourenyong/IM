package com.zzh.http;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable
{
    private String code;

    private String message;

    /**
     * 请求号
     */
    private String respNo;

    private T dataBody;
}
