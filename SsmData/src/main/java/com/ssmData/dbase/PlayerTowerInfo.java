package com.ssmData.dbase;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerTowerInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	public String _id;
	
    @Indexed
    public String uid;                      // 玩家唯一ID
    
    public int cur_lv;				        // 当前停留层；
    public boolean win;			            // 当前层是否已胜利；
    public int history_open;	            // 历史达到最远层
    public int history_box;		//历史领取最远层
    public long box_time;		//胜利时间（领取）
    public List<Integer> first_award;		//首次通关奖励领取情况
}
