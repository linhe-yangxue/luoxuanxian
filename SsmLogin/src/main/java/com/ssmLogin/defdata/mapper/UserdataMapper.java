package com.ssmLogin.defdata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ssmLogin.defdata.entity.Userdata;
import com.ssmLogin.defdata.entity.UserdataCriteria;

public interface UserdataMapper {
    int countByExample(UserdataCriteria example);

    int deleteByExample(UserdataCriteria example);

    int deleteByPrimaryKey(String uid);

    int insert(Userdata record);

    int insertSelective(Userdata record);

    List<Userdata> selectByExample(UserdataCriteria example);

    Userdata selectByPrimaryKey(String uid);

    int updateByExampleSelective(@Param("record") Userdata record, @Param("example") UserdataCriteria example);

    int updateByExample(@Param("record") Userdata record, @Param("example") UserdataCriteria example);

    int updateByPrimaryKeySelective(Userdata record);

    int updateByPrimaryKey(Userdata record);
}