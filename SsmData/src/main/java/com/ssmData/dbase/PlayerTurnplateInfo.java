package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerTurnplateInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String _id;

    @Indexed
    public String uid;                      // 玩家唯一ID
    
    public Long last_t = 0L;		//上次抽奖时间
    public List<Integer> cnts = new ArrayList<Integer>();  //各个格子的启动次数，格子ID从0开始
    public Integer total_cnt = 0;			//已经抽取的总次数，会被配置中需要清空的格子给清空
}
