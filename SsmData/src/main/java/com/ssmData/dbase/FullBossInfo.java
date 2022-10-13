package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public class FullBossInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String id;						//ID
	public List<BossInfo> all = new ArrayList<BossInfo>();
    
    public class BossInfo implements Serializable {
    	private static final long serialVersionUID = 1L;
    	
    	public Integer id = 0;		//Boss表格id
    	public Long dead_t = 0L;			//最近一次死亡时间戳，如果是0就是活着的
    	public Long maxhp = 0L; //总血量
    	public List<Integer> mon_ids = new ArrayList<Integer>();	//怪物ID列表
    	public List<Integer> hps = new ArrayList<Integer>();		//怪物当前血量列表
    	public List<Hitman> hit_list = new ArrayList<Hitman>();		//参与的人
    	public List<Hitman> tops = new ArrayList<Hitman>();   //名词靠前
    	public List<Killer> killers = new ArrayList<Killer>();  //历史击杀记录
    	
    	public class Hitman implements Serializable {
        	private static final long serialVersionUID = 1L;
        	
        	public String uid = "";
        	public Long acc_dmg = 0L;		//累计伤害
        	public Long a_t = 0L;		//攻击时间戳，用于同伤害的排序		
        }
        
        public class Killer implements Serializable {
        	private static final long serialVersionUID = 1L;
        	
        	public String uid = "";
        	public Integer f = 0;		//当时战力
        	public Long k_t = 0L;		//击杀时间戳，用于同伤害的排序		
        }
    }
}
