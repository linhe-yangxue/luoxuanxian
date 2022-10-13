package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public class PlayerSendInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String id;						// 玩家唯一ID
	
    public List<SendQuest> qs = new ArrayList<SendQuest>();			//任务列表
    
    public long refresh_rob_t = 0L;					//上一次刷新掠夺列表时间
    public List<Team> rob_teams = new ArrayList<Team>();  //掠夺列表
    public List<String> enemys = new ArrayList<String>();	//仇人列表    
    
    public class Team implements Serializable {
    	private static final long serialVersionUID = 1L;
    	
    	public String uid = "";
    	public Integer zid = 0;
    	public String user_name = "";
    	public Integer vip = 0;
    	public Integer lv = 0;
    	public Integer q_idx = 0;
    	public SendQuest q = new SendQuest();
    }
    
    /*public class Enemy implements Serializable {
    	private static final long serialVersionUID = 1L;
    	
    	public String uid = "";
    	public Integer zid = 0;
    	public String user_name = "";
    	public Integer vip = 0;
    	public Integer lv = 0;
    
    	public List<Quest> qs = new ArrayList<Quest>();
    }*/
}
