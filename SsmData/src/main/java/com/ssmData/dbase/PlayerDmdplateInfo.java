package com.ssmData.dbase;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerDmdplateInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String _id;

    @Indexed
    public String uid;                      // 玩家唯一ID
    
    public int draw_cnt = 0;			//已经抽奖的次数
}
