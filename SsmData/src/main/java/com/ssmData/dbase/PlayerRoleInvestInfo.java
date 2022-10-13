package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public class PlayerRoleInvestInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String id;
    
    public boolean has_invest = false;  	//是否已经投资过
    public List<Integer> invest_award = new ArrayList<Integer>();  //投资活动已经领奖的列表
}
