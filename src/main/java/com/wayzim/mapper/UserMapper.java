package com.wayzim.mapper;

import com.wayzim.pojo.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface UserMapper {

    @Select("select * from t_user")
    List<User> selectAll();

}
