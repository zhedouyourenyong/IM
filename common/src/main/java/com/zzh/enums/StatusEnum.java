package com.zzh.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum StatusEnum
{
    /**
     * 成功
     */
    SUCCESS("9000", "成功"),
    /**
     * 成功
     */
    FALLBACK("8000", "FALL_BACK"),
    /**
     * 参数校验失败
     **/
    VALIDATION_FAIL("3000", "参数校验失败"),
    /**
     * 失败
     */
    FAIL("4000", "失败"),

    /**
     * 重复登录
     */
    REPEAT_LOGIN("5000", "账号重复登录，请退出一个账号！"),

    REPEAT_REGISTRY("6000","用户名已被注册"),

    /**
     * 账号不在线
     */
    OFF_LINE("7000", "你选择的账号不在线，请重新选择！"),

    /**
     * 登录信息不匹配
     */
    ACCOUNT_NOT_MATCH("9100", "登录信息不匹配！"),

    /**
     * 请求限流
     */
    REQUEST_LIMIT("6000", "请求限流");

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
