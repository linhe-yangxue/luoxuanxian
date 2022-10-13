package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public class HubSendInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String id;						//玩家 id
    public Integer zid = 0;	
	public Integer lv = 0;			//最后等级
	public String name = "";
	public Integer vip = 0;
	
	public List<SendQuest> qs = new ArrayList<SendQuest>();
}
