package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ssmData.config.constant.ConfigConstant;
import com.ssmShare.entity.UserBase;

@Document
public class PlayerInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	public String _id;
	
	public String username;						//账号 （目前平台信息里没有账号的）
	
	public UserBase user_base;					//平台信息

	public double gold;							// 金币数

	public double diamond;						// 钻石数

	public int team_lv;            				// 战队等级

	public double team_exp;        				// 战队经验

	public int vip_level;       				// VIP等级
	
	public int acc_rmb;							//累计冲的钱数

	public int team_history_max_fighting;     	// 战队的历史最高战斗力

	public int team_current_fighting;         	// 战队当前战力

	public long last_active_time;				// 最后活跃时间(用于离线收益计算)

	public long month_card_expired;				// 月卡过期时间

	public long lifetime_card_expired;				// 终生卡过期时间
	
	public int history_arena;					//竞技场历史最高
	
	public List<Integer> arena_rank_award;		//已经领取的竞技场奖励
	
	public List<Integer> vip_award;				//vip奖励
	
	public long last_gold_time;					//上一次回复金币购买次数的时间
	
	public long gold_buy_cnt;					//当天金币已经购买次数
	
	public Long login_t;						//登陆时间戳
	
	public Long lv_t = 0L;						//升级时间戳
	
	public Boolean drama_end = false;				//已完成剧情对话
	
	public Boolean drama_b_end = false;				//已完成剧情战斗
	
	private static final Logger log = LoggerFactory.getLogger(PlayerInfo.class);
	
	public Long creat_t = 0L;					//创建时间
	public Double t_gold = 0.0;					//总金币
	public Double t_dmd = 0.0;					//总钻石
	public Double t_c_dmd = 0.0;				//总充值钻石
	public Integer dmd_draw = 0;				//总钻石单抽次数
	public Integer dmd_draw_ten = 0;				//总钻石10抽次数
	public Integer lmt_draw = 0;				//总限量抽次数
	
	public Integer fl_award = 0;			//是否领取了关注奖励 

	public Long share_t = 0L;		//上一次分享时间戳
	public Integer share_cnt = 0;	//今日已分享领奖次数
	public List<Integer> inv_award = new ArrayList<Integer>();	//当日已经领奖邀请奖励的id列表
	public Long inv_t = 0L;
	
	public Integer qq_cut = 0;  //QQ快捷方式状态 0 未创建 1 已创建未领取 2 已领取
	
	// region 金币

	/**
	 * 增加金钱
	 * @param offset 增加值
	 * @return 增加后金钱数
	 */
	public double addGold(double offset){
		if(offset < 0){
			log.error("无法增加金钱" + offset + "，因为增加值是一个负数。");
			return 0;
		}
		gold += offset;
		if (t_gold == null) {
			t_gold = gold;
		} else {
			t_gold += offset;
		}
		return gold;
	}

	/**
	 * 扣减金钱
	 * @param offset 扣减值
	 * @return 扣减后金钱数
	 */
	public double subGold(double offset){
		if(offset < 0){
			log.error("无法扣减金钱" + offset + "，因为扣减值是一个负数。");
			return 0;
		}

		if(offset > gold){
			log.error("无法扣减金钱" + offset + "，因为没有足够的金钱，请先做判断。");
			return 0;
		}

		gold -= offset;
		//player_info_db.save();
		return gold;
	}

	/**
	 * 检查是否有足够的金币
	 * @param value 检查值
	 * @return 是否足够
	 */
	public boolean hasGold(double value){
		if(gold >= value){
			return true;
		}
		return false;
	}

	// endregion

	// region 钻石

	/**
	 * 增加钻石
	 * @param offset 增加值
	 * @return 增加后钻石数
	 */
	public double addDiamond(double offset){
		if(offset < 0){
			log.error("无法增加金钱" + offset + "，因为增加值是一个负数。");
			return 0;
		}
		diamond += offset;
		if (t_dmd == null) {
			t_dmd = offset;
		} else {
			t_dmd += offset;
		}
		return diamond;
	}

	/**
	 * 扣减钻石              !!!!!!!!!!!!!!!!!不要直接使用这个，要用PlayerImpl里的SubDiamond!!!!!!!!!!!!!!!
	 * @param offset 扣减值
	 * @return 扣减后钻石数
	 !!!!!!!!!!!!!!!!!不要直接使用这个，要用PlayerImpl里的SubDiamond!!!!!!!!!!!!!!!   */
	public double subDiamond(double offset){
		if(offset < 0){
			log.error("无法扣减金钱" + offset + "，因为扣减值是一个负数。");
			return 0;
		}

		if(offset > diamond){
			log.error("无法扣减金钱" + offset + "，因为没有足够的金钱，请先做判断。");
			return 0;
		}

		diamond -= offset;
		return diamond;
	}

	/**
	 * 检查是否有足够的钻石
	 * @param value 检查值
	 * @return 是否足够
	 */
	public boolean hasDiamond(double value){
		if(diamond >= value){
			return true;
		}
		return false;
	}

	// endregion

	// region 经验值操作

	/**
	 * 增加经验值
	 * @param offset 增加值
	 * @return 是否升级  *************不要直接调用，要通过PlayerImpl*************
	 */
	public boolean addExp(int offset){
		if(offset < 0){
			log.error("无法增加经验值" + offset + "，因为增加值是一个负数。");
			return false;
		}
		if (team_lv >= ConfigConstant.tConf.getLevelCap()){
			return false;
		}

		// 记录当前等级
		boolean is_levelup = false;

		// 升级逻辑
		while((team_exp + offset) >= this.getNextExp(team_lv) && team_lv < ConfigConstant.tConf.getLevelCap()){ // 设定等级上限
			double nextExp = this.getNextExp(team_lv);
			offset -= nextExp - team_exp;
			team_lv += 1;
			team_exp = 0;
			is_levelup = true;
		}
		if (is_levelup) {
			lv_t = Calendar.getInstance().getTimeInMillis();
		}

		team_exp += offset;
		return is_levelup;
	}

	/**
	 * 获取下级所需经验值
	 * @param lv
	 * @return
	 */
	public double getNextExp(int lv){
		return ConfigConstant.tGrade.get(lv).getLvExp();
	}

	// endregion

	// region 阵容

	/**
	 * 返回当前等级对应开启的上阵人数上限
	 */
	public int GetMaxHeroCounts()
	{
		int max_count = 0;
		for (int i = 0; i < ConfigConstant.tConf.getJoin().length; ++i)
		{
			if (team_lv < ConfigConstant.tConf.getJoin()[i])
			{
				break;	
			}
			max_count++;
		}
		return max_count;
	}
	
	/**
	 * 返回当前等级对应开启的副将位置上限
	 */
	public int GetMaxBackupPosCounts()
	{
		int max_count = 0;
		for (int i = 0; i < ConfigConstant.tConf.getBackup().length; ++i)
		{
			if (team_lv < ConfigConstant.tConf.getBackup()[i])
			{
				break;	
			}
			max_count++;
		}
		return max_count;
	}
	
	//更新队伍战力，如果队伍战力超过历史战力，返回true
	/*public boolean UpdateTeamFightingCheckMax(int new_fighting)
	{
		team_current_fighting = new_fighting;
		if (team_history_max_fighting < team_current_fighting)
		{
			team_history_max_fighting = team_current_fighting;
			return true;
		}
		return false;
	}*/

	// endregion
}
