package com.ssmData.dbase;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class ServerInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	public String _id;
	
    @Indexed
    public String uid; 				//通用表 只会等于固定值
    
    public long start_time;			//开服时间戳
    
    public Integer guild_idx = 1;		//工会当前序号
    
    public Integer guild_war_f_idx;	//工会战报当前ID序号，只有HUB用
    public Map<String, String> guild_war_pair; //工会匹配序列，用来核对，实际每个工会自身会冗余记录。只有HUB用
}
