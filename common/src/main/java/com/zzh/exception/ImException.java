package com.zzh.exception;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2020/1/31 13:51
 */
public class ImException extends RuntimeException
{
    public ImException(String message, Throwable e)
    {
        super(message, e);
    }

    public ImException(Throwable e)
    {
        super(e);
    }

    public ImException(String message)
    {
        super(message);
    }

    @Override
    public String toString()
    {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        return (message != null) ? (s + ": " + message) : s;
    }
}
