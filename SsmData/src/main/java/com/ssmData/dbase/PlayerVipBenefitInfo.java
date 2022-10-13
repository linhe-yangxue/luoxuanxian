package com.ssmData.dbase;

import java.io.Serializable;
import org.springframework.data.annotation.Id;

public class PlayerVipBenefitInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String id;

    public Long last_t;    //上一次领取时间，当天没领取过，可以领取1次
}
