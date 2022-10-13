package com.ssmLogin.defdata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ssmLogin.defdata.entity.Paymement;
import com.ssmLogin.defdata.entity.PaymementCriteria;

public interface PaymementMapper {
    int countByExample(PaymementCriteria example);

    int deleteByExample(PaymementCriteria example);

    int deleteByPrimaryKey(String ownOrder);

    int insert(Paymement record);

    int insertSelective(Paymement record);

    List<Paymement> selectByExample(PaymementCriteria example);

    Paymement selectByPrimaryKey(String ownOrder);

    int updateByExampleSelective(@Param("record") Paymement record, @Param("example") PaymementCriteria example);

    int updateByExample(@Param("record") Paymement record, @Param("example") PaymementCriteria example);

    int updateByPrimaryKeySelective(Paymement record);

    int updateByPrimaryKey(Paymement record);
}