package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerLimitSevenActivityInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String _id;

    @Indexed
    public String uid;                      // 玩家唯一ID
    
    public Long limit_seven_last_t = 0L;		//限时7日活动上次登陆的时间戳
    public Integer limit_seven_acc = 0;		//限时7日活动上次登陆的时间戳
    public List<Integer> limit_seven_award = new ArrayList<Integer>();  //限时7日登陆活动已经领奖的列表
}
