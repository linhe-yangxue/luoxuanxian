package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerCheckinActivityInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String _id;

    @Indexed
    public String uid;                      // 玩家唯一ID
    public int re = 0;	//	本月补签次数
    public List<Integer> date = new ArrayList<Integer>();  //本月已经签到的id
    public List<Integer> award = new ArrayList<Integer>();  //本月已经领取的累计登陆奖励的id
}
