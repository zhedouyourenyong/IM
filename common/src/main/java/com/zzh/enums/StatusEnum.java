package com.zzh.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum StatusEnum
{
    SUCCESS("0","成功"),
    NAME_IS_NULL("-1","用户名为空"),
    NAME_REPEAT("-2","用户名已被注册"),
    PASSWORD_IS_NULL("-3","密码为空"),
    PASSWORD_ERROR("-4","密码错误"),
    NAME_NOT_EXIST("-5","用户名不存在"),
    LOGIN_REPEAT("-6","重复登录"),
    ;

    private String code;
    private String message;

    StatusEnum (String code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public String getCode ()
    {
        return code;
    }

    public String getMessage ()
    {
        return message;
    }

    /**
     * 通过枚举值码查找枚举值。
     *
     * @param code 查找枚举值的枚举值码。
     * @return 枚举值码对应的枚举值。
     * @throws IllegalArgumentException 如果 code 没有对应的 StatusEnum 。
     */
    public static StatusEnum findStatusByCode (String code)
    {
        for (StatusEnum status : values())
        {
            if(status.getCode() == code)
            {
                return status;
            }
        }
        throw new IllegalArgumentException("ResultInfo StatusEnum not legal:" + code);
    }


    /**
     * 获取全部枚举值。
     *
     * @return 全部枚举值。
     */
    public static List<StatusEnum> getAllStatus ()
    {
        List<StatusEnum> result = Arrays.asList(values());
        return result;
    }

    /**
     * 获取全部枚举值码。
     *
     * @return 全部枚举值码。
     */
    public static List<String> getAllStatusCode ()
    {
        List<String> list = new ArrayList<>(values().length);
        for (StatusEnum status : values())
        {
            list.add(status.getCode());
        }
        return list;
    }

}
