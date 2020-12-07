package com.example.demo.dao.userTest;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserInfoMapper {

    @Select("select * from user_info")
    List<Map<String,Object>> getUserInfo();

    @Select(value = "select * from user_info limit #{limit} ")
    Cursor<Map<String,Object>> getUserInfoV2(int limit);

}
