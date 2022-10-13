package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public class PlayerSendRcdInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String id;						// 玩家唯一ID
	
	public List<Rcd> rcds = new ArrayList<Rcd>();
    
    public class Rcd implements Serializable {
    	private static final long serialVersionUID = 1L;
    
        public Integer send_id;             // 任务id
        public Integer result;              // 战斗结果 [负0, 胜1, 平2]
        public Integer type;                // 探险类型

        public String atk_uid;              // 进攻方uid
        public String atk_name;             // 进攻方名字
        public Integer atk_zid;              // 进攻方区服id
        public List<Integer> atk_roles;     // 进攻方角色id列表
        
        public String def_uid;              // 防守方uid
        public String def_name;             // 防守方名字
        public Integer def_zid;              // 防守方区服id
        public List<Integer> def_roles;     // 防守方角色id列表
        
    	public String pk = null;
    }
}
