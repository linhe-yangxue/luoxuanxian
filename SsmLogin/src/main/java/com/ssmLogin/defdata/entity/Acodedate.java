package com.ssmLogin.defdata.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("Acodedate")
public class Acodedate extends AcodedateKey implements Serializable {
    private Date productDate;

    private Long generateDate;

    private Integer generateNum;

    private static final long serialVersionUID = 1L;

    public Date getProductDate() {
        return productDate;
    }

    public void setProductDate(Date productDate) {
        this.productDate = productDate;
    }

    public Long getGenerateDate() {
        return generateDate;
    }

    public void setGenerateDate(Long generateDate) {
        this.generateDate = generateDate;
    }

    public Integer getGenerateNum() {
        return generateNum;
    }

    public void setGenerateNum(Integer generateNum) {
        this.generateNum = generateNum;
    }
}