package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public class HubPlayerGuildInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String id;		//玩家ID
    
    public Byte touched = 0;		//是否已经被攻击过
    
    public Integer daily_s = 0;		//当日积分
    
    public Integer weekly_s = 0;	//当周积分
    
    public List<String> daily_rcd = new ArrayList<String>();	//每日战报ID列表
    
    
}
