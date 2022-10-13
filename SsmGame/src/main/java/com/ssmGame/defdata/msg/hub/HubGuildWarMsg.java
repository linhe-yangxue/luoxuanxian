package com.ssmGame.defdata.msg.hub;

import java.util.List;
import java.util.Map;

import com.ssmData.dbase.RoleInfo;
import com.ssmGame.defdata.msg.guildwar.GuildWarLogMsg;

public class HubGuildWarMsg {

    public Integer code;    // 结果码， 0-成功
        
    public Byte is_local;	//是否本服匹配，0的话是跨服，游服自己判断
    
    public Map<String, I> f_list; //工会id和历史战力总和和名字等信息,上发hub时是自己的，下发时是敌人的
    
    public Map<String, Player> p_list; //玩家数据列表
    
    public Map<Integer, RoleInfo> roles;  //战斗角色
    public String target_uid;		//要挑战的对手uid
    public String attacker_uid;
    public Integer contri_cnt;		//胜利者的贡献值
    
    public Player one;		//发送单个人的数据
    
    public List<GuildWarLogMsg> logs;           // 战报列表
    
    public class I
    {
    	public Byte stat;  //hub下发时，如果轮空或没匹配，则下面的都是空
    	
    	public String name;        
    	public Double f;
    	public Integer lv;
    	public Integer cnt;
    	public Integer zid;
    	
        public String em_id;		//今日对手工会的工会ID
        public String em_name;		//今日对手工会的工会名 
        public Integer em_lv;		//今日对手工会的等级 
        public Integer em_cnt;		//今日对手会员人数 
        public Integer em_zid ;		//今日对手的区域zid
        
        public Integer my_score;	//我的今日分数
        public Integer ene_score;	//对手的今日分数
        
        public List<String> mem_ids; //所有成员ID
        
        public String ceo_id; 		//会长id
        public String ceo_name;		//会长名称
        
        public Byte win; //胜平负
        public Integer contri; //每日胜利的会员贡献奖励
        public Integer rank;		//本周名次，榜以外的都是上限
    }
    
    public class Player
    {
    	public String name;			//玩家名
    	public Integer vip;			//vip
    	public Integer fight;		//战力
    	public Integer daily;		//当天被扣除积分
    	public Integer raid;		//当天掠夺积分
    	public Byte cnt;			//当天已经挑战次数
    	public Integer team_lv;		//等级
    	public String img;			//头像
    }
}
