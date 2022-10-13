package com.ssmData.dbase;

import java.io.Serializable;

public class DailyTaskInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public int id;		//任务id
	public int cnt;			//当天已经完成次数
}
