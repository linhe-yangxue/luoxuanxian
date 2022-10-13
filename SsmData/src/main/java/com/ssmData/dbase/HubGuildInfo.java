package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

public class HubGuildInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String id;						//公会 id
    	
	public String enemy_id;					//对手工会id，匹配时清空，匹配后产生
	
	public Long m_t = 0L;				//最后匹配时间，如果不是当天的，则不进行后续操作
	
	public Byte local = 0;				//是否本服匹配， 0 不是，1是
	
	public Integer daily_s = 0;		//团体每日积分，每日匹配后清0
	
	public Integer weekly_s = 0;		//团体每周积分,周一清0
	
	public Long b_t = 0L;	//周分数改变的时间点，用于同分排行
	
	public Byte war_stat = 0;			//是否参加今日战斗了。注意和GuildInfo里的同步
	
	public Double fight = 0.0;			//匹配时的战力
	
	public String name	=	"";			//工会名
	
	public Integer zid = 0;					//公会所属服务器id
	
	public Integer lv = 0;		//等级
	
	public Integer cnt = 0;		//人数
	
    public String ceo_id; 		//会长id
    public String ceo_name;		//会长名称
	
	public Map<String, Player> members = new HashMap<String, Player>();  
	
	public class Player implements Serializable {
		private static final long serialVersionUID = 1L;
		public Byte touched = 0;
	    public Integer daily_s = 0;		//当日被扣除的积分
	    public List<String> rcd = new ArrayList<String>();	//每日战报ID列表
	}
}
