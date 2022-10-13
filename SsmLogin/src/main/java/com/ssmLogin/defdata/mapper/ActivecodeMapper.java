package com.ssmLogin.defdata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ssmLogin.defdata.entity.Activecode;
import com.ssmLogin.defdata.entity.ActivecodeCriteria;

public interface ActivecodeMapper {
    int countByExample(ActivecodeCriteria example);

    int deleteByExample(ActivecodeCriteria example);

    int deleteByPrimaryKey(String activeCode);

    int insert(Activecode record);

    int insertSelective(Activecode record);

    List<Activecode> selectByExample(ActivecodeCriteria example);

    Activecode selectByPrimaryKey(String activeCode);

    int updateByExampleSelective(@Param("record") Activecode record, @Param("example") ActivecodeCriteria example);

    int updateByExample(@Param("record") Activecode record, @Param("example") ActivecodeCriteria example);

    int updateByPrimaryKeySelective(Activecode record);

    int updateByPrimaryKey(Activecode record);
}