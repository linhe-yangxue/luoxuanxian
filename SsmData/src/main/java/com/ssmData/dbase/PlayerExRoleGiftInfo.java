package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public class PlayerExRoleGiftInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String id;
    
    public long last_t = 0L;			//上次刷新数据的时间戳
    public List<Integer> ids = new ArrayList<Integer>();  //已经购买的id
    public List<Integer> cnts = new ArrayList<Integer>();  //已经购买的次数，跟id一一对应
}
