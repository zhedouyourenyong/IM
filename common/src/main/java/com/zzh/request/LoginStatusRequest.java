package com.zzh.request;

import lombok.Data;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: TODO(一句话描述该类的功能)
 * @Author: Administrator
 * @Date: 2019/12/11 15:16
 */
@Data
public class LoginStatusRequest extends BaseRequest
{
    private String userId;
}
