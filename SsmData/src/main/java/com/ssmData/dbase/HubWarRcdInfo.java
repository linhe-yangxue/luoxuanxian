package com.ssmData.dbase;

import java.io.Serializable;
import org.springframework.data.annotation.Id;

public class HubWarRcdInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String id;						//战报ID  发起方的uid + daily_cont组成

	public String l_id;					//发起方uid
	
	public String r_id;					//接收方的uid
	
	public String l_name;				//发起方名字
	
	public String r_name;				//接收方名字
	
	public Integer l_lv;
	
	public Integer r_lv;
	
	public String l_img;
	
	public String r_img;
	
	public Integer result;					//胜平负 ，参见 BattleResult
	
	public Byte star;						//本场获得星数
	
	public String pack;						//战报数据json数列
}
