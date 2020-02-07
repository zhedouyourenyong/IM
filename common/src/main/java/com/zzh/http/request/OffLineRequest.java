package com.zzh.http.request;

import com.zzh.http.BaseRequest;
import lombok.Data;

@Data
public class OffLineRequest extends BaseRequest
{
    private String userId;
}
