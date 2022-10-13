package com.ssmLogin.defdata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ssmLogin.defdata.entity.Gameplat;
import com.ssmLogin.defdata.entity.GameplatCriteria;
import com.ssmLogin.defdata.entity.GameplatKey;

public interface GameplatMapper {
    int countByExample(GameplatCriteria example);

    int deleteByExample(GameplatCriteria example);

    int deleteByPrimaryKey(GameplatKey key);

    int insert(Gameplat record);

    int insertSelective(Gameplat record);

    List<Gameplat> selectByExample(GameplatCriteria example);

    Gameplat selectByPrimaryKey(GameplatKey key);

    int updateByExampleSelective(@Param("record") Gameplat record, @Param("example") GameplatCriteria example);

    int updateByExample(@Param("record") Gameplat record, @Param("example") GameplatCriteria example);

    int updateByPrimaryKeySelective(Gameplat record);

    int updateByPrimaryKey(Gameplat record);
}