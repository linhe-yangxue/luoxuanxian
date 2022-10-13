package com.ssmGame.defdata.msg.player;

import com.ssmData.dbase.PlayerInstanceInfo;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerScrollInfo;
import com.ssmGame.defdata.msg.pve.PveOfflineMsg;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncEquipBagMsg;

import java.util.List;

/**
 * 用户登录信息
 * Created by WYM on 2016/10/26.
 */
public class PlayerLoginMsg {
	
	public boolean is_new = false;
	public boolean need_create = false;  //是否需要弹创建页面
	public String _id;
	public Long guid;
	public String username;  
	public String icon_url;
	public String nickname;
	public double gold;
	public double diamond;
	public int team_lv;            //战队等级
	public double team_exp;        //战队经验
	public int current_level_id;   //当前打到的关卡表
	public int vip_level;        
	public int team_history_max_fighting;     //战队的历史最高战斗力
	public int team_current_fighting;
	public int acc_rmb;                     // 累计冲的钱数（必定全值）
	public List<Integer> vip_award;         // vip奖励（必定全值）
	public long last_gold_time;				// 上一次回复金币购买次数的时间
	public long gold_buy_cnt;				// 当天金币已经购买次数

	// 剧情
	public boolean fin_dialog;                  // 剧情对话是否已通过
	public boolean fin_battle;                  // 剧情战斗是否已通过

	// 时间
	public long server_now;		// 登陆时间戳
	public long server_start;   // 开服时间戳
	
	public Boolean login_1st;	// 是否当天第一次登陆
	public Long create_time; 	// 玩家角色创建时间
	
	public String guild_id;  //工会id

	// 周边系统
	public PveOfflineMsg pve_offline; // 离线收益信息
	public PlayerRolesInfo roles; // 玩家角色信息
	public SyncBagMsg bag; // 玩家背包信息
	public SyncEquipBagMsg equip_bag; // 装备背包信息
	public PlayerScrollInfo scroll; // 挑战券信息
	public PlayerInstanceInfo instance; // 副本信息
	
	public Integer need_fl;		//是否需要打开关注入口 0 不需要 1需要
	public Integer fl_award;	//是否已经领取关注奖励 0 未领取 1已经领取
	public Integer is_fl;		//是否已经关注 0 未关注 1已经关注

	public Integer qq_cut;		// QQ快捷方式状态 0 未创建 1 已创建未领取 2 已领取
	
	public Integer share_on;		//是否开启分享0 不需要 1需要
	public Integer share_cnt;		//今日分享次数
	public Integer inv_cnt;			//今日成功
	public List<Integer> inv_award;	//当日已经领奖邀请奖励的id列表
	public Long share_t;			//分享时间
}
