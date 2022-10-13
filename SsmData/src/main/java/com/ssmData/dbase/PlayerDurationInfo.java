package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerDurationInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String _id;

    @Indexed
    public String uid;                      // 玩家唯一ID
    public long last_t = 0L;				//上次查询时间
    public long duration = 0L;				//累计时长
    public List<Integer> award = new ArrayList<Integer>();  //在线时长已经领取的奖励
}
