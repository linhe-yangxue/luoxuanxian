package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerAccSpendInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String _id;

    @Indexed
    public String uid;                      // 玩家唯一ID
    
    public Long last_t = 0L;		//上次更新时间
    public Double acc_spend = 0.0;		//累计花费数量
    public List<Integer> award = new ArrayList<Integer>();  //已经领取奖品id
}
