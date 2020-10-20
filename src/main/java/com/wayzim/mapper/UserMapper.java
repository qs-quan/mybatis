package com.wayzim.mapper;

import com.wayzim.pojo.User;

import java.util.List;


public interface UserMapper {

    List<User> selectAll();

    User selectById(String id);

}
