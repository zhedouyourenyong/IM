package com.zzh.http.response;

import com.zzh.enums.StatusEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse implements Serializable
{
    private StatusEnum statusEnum;

    private String dataBody;
}
