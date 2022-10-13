package com.ssmLogin.defdata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ssmLogin.defdata.entity.Acodedate;
import com.ssmLogin.defdata.entity.AcodedateCriteria;
import com.ssmLogin.defdata.entity.AcodedateKey;

public interface AcodedateMapper {
    int countByExample(AcodedateCriteria example);

    int deleteByExample(AcodedateCriteria example);

    int deleteByPrimaryKey(AcodedateKey key);

    int insert(Acodedate record);

    int insertSelective(Acodedate record);

    List<Acodedate> selectByExample(AcodedateCriteria example);

    Acodedate selectByPrimaryKey(AcodedateKey key);

    int updateByExampleSelective(@Param("record") Acodedate record, @Param("example") AcodedateCriteria example);

    int updateByExample(@Param("record") Acodedate record, @Param("example") AcodedateCriteria example);

    int updateByPrimaryKeySelective(Acodedate record);

    int updateByPrimaryKey(Acodedate record);
}