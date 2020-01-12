package com.zzh.repository;

import com.zzh.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2019/12/11 17:33
 */
@Mapper
@Repository
public interface UserDao
{
    String TABLE_NAME = " user ";
    String INSERT_FIELD = " user_id,user_name,password ";
    String SELECT_FIELD = " user_id,user_name,password ";

    @Insert({"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELD, ")", "VALUES (#{userId},#{userName},#{password})"})
    int addUser(String userId, String userName, String password);

    @Select({"SELECT ", SELECT_FIELD, " FROM ", TABLE_NAME, " WHERE user_name=#{name}"})
    User selectByName(String name);
}
