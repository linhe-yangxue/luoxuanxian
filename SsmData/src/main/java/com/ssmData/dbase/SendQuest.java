package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SendQuest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Integer send_id = 0;
	public Long end_t = 0L; //任务结束时间戳
	public List<Integer> roles = new ArrayList<Integer>();
	public Integer cnt_rob = 0; //掠夺次数
	public Integer cnt_be_rob = 0; //被掠夺次数
	public Integer type = 0;
	public Integer fight = 0;
	public List<String> r_ids = new ArrayList<String>(); //抢夺者的id
}
