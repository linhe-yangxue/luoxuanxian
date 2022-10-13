package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerSevenActivityInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String _id;

    @Indexed
    public String uid;                      // 玩家唯一ID
    
    public Long seven_last_t = 0L;		//7日活动上次登陆的时间戳
    public Integer seven_acc = 0;		//7日登陆活动的累计登陆日期
    public List<Integer> seven_award = new ArrayList<Integer>();  //7日登陆活动已经领奖的列表
}
