package com.ssmData.dbase;

import java.io.Serializable;
import java.util.Calendar;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerWishInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String _id;

    @Indexed
    public String uid;                      // 玩家唯一ID
    
    public Integer lv = 0;				//许愿等级
    public Integer exp = 0;				//当前许愿经验
    public Integer eng = 0;				//当前能量
    public Integer role = 0;			//当前角色
    public Long	last = Calendar.getInstance().getTimeInMillis();	//上次刷新时间
}
