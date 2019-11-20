package com.zzh.pojo;


public class UserInfo
{
    private Long userId;
    private String userName;

    public UserInfo (Long userId, String userName)
    {
        this.userId = userId;
        this.userName = userName;
    }

    public Long getUserId ()
    {
        return userId;
    }

    public void setUserId (Long userId)
    {
        this.userId = userId;
    }

    public String getUserName ()
    {
        return userName;
    }

    public void setUserName (String userName)
    {
        this.userName = userName;
    }

    @Override
    public String toString ()
    {
        return "UserInfo{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }
}
