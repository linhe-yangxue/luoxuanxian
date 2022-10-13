package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class GuildInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String id;						//公会ID
    
	@Indexed
    public String name = "";						//名字
	
	public Long creat_t = Calendar.getInstance().getTimeInMillis();					//创建时间戳
    
    public Integer lv = 1;						//公会等级
    
    public Double exp = 0.0;				//公会经验
    
    public Long lv_t = Calendar.getInstance().getTimeInMillis();					//升级时间
    
    public String words = "";				//公告
    
    public Double funds = 0.0;				//公会资金
    
    public String ceo_id = "";				//会长id
    
    public Byte war_stat = 0;			//今日是否参加了战斗
    
    public String em_id = "";		//今日对手工会的工会ID
    
    public String em_name = "";		//今日对手工会的工会名
    
    public Integer em_lv = 0;		//今日对手工会的等级
    
    public Integer em_cnt = 0;		//今日对手会员人数
    
    public Integer em_zid = 0;		//今日对手的区域zid
    
    public Byte em_is_net = 0;		//是否远程，0是本服，1是远程
    
    public Integer seed = 0;		//显示用的对手随机队列种子
    
    public List<String> vp_ids = new ArrayList<String>();  //副会长id列表
    
    public List<String> waitings = new ArrayList<String>();  //申请列表
    
    public List<Member> members = new ArrayList<Member>();	//成员队列
    
    public List<Integer> tech_ids = new ArrayList<Integer>();	//科技ID列表
    public List<Integer> tech_lvs = new ArrayList<Integer>();	//科技对应等级列表,初始0级
    
    public class Member implements Serializable {
    	private static final long serialVersionUID = 1L;
    	
    	public String id = "";		//成员id
    	public Double acc_con = 0.0;	//累计贡献
    	public Double daily_con = 0.0;	//当日贡献    
    	public Long last_con_t = Calendar.getInstance().getTimeInMillis();
    }
}
