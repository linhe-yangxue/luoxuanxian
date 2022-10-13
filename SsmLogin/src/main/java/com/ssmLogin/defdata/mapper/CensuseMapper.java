package com.ssmLogin.defdata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ssmLogin.defdata.entity.Censuse;
import com.ssmLogin.defdata.entity.CensuseCriteria;
import com.ssmLogin.defdata.entity.CensuseKey;

public interface CensuseMapper {
    int countByExample(CensuseCriteria example);

    int deleteByExample(CensuseCriteria example);

    int deleteByPrimaryKey(CensuseKey key);

    int insert(Censuse record);

    int insertSelective(Censuse record);

    List<Censuse> selectByExample(CensuseCriteria example);

    Censuse selectByPrimaryKey(CensuseKey key);

    int updateByExampleSelective(@Param("record") Censuse record, @Param("example") CensuseCriteria example);

    int updateByExample(@Param("record") Censuse record, @Param("example") CensuseCriteria example);

    int updateByPrimaryKeySelective(Censuse record);

    int updateByPrimaryKey(Censuse record);
}