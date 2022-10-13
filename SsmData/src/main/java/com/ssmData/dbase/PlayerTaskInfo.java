package com.ssmData.dbase;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerTaskInfo implements Serializable   {
	private static final long serialVersionUID = 1L;
	
	@Id
	public String _id;
	
    @Indexed
    public String uid;                      // 玩家唯一ID
    
    public int cur_id;		//当前任务状态
    
    public boolean is_finish; //当前任务是否完成
    
    public int arg;			//当前进度参数
}
