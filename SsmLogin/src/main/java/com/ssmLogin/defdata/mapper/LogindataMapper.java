package com.ssmLogin.defdata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ssmLogin.defdata.entity.Logindata;
import com.ssmLogin.defdata.entity.LogindataCriteria;
import com.ssmLogin.defdata.entity.LogindataKey;

public interface LogindataMapper {
    int countByExample(LogindataCriteria example);

    int deleteByExample(LogindataCriteria example);

    int deleteByPrimaryKey(LogindataKey key);

    int insert(Logindata record);

    int insertSelective(Logindata record);

    List<Logindata> selectByExample(LogindataCriteria example);

    Logindata selectByPrimaryKey(LogindataKey key);

    int updateByExampleSelective(@Param("record") Logindata record, @Param("example") LogindataCriteria example);

    int updateByExample(@Param("record") Logindata record, @Param("example") LogindataCriteria example);

    int updateByPrimaryKeySelective(Logindata record);

    int updateByPrimaryKey(Logindata record);
}