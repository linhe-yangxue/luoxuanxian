package com.statistics.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.mongo.MultiMongoSource;
import com.ssmData.dbase.ArenaInfo;
import com.ssmData.dbase.ArenaRankInfo;
import com.ssmData.dbase.BaseDataSource;
import com.ssmData.dbase.PlayerDmgRewardInfo;
import com.ssmData.dbase.PlayerDuelInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInvestActivityInfo;
import com.ssmData.dbase.PlayerLevelInfo;
import com.ssmData.dbase.PlayerMonthcardInfo;
import com.ssmData.dbase.PlayerTaskInfo;
import com.ssmData.dbase.PlayerTowerInfo;
import com.ssmData.dbase.PlayerWishInfo;
import com.ssmShare.constants.E_PayType;
import com.statistics.constants.I_Constants;
import com.statistics.facde.I_GetWebData;

@Service
@Scope("prototype")
public class GetWebData implements I_GetWebData {
	
	@Autowired
	MultiMongoSource db_loader;

	private static final String UID = "uid";
	
	private static final int ELEM_LEN = 120;
	
	private static final String DOT = ",";
	
	private static final String NEXT = "\n";
	
	@Override
	public ReInfo handleGetWebData(String db_name) {
		
		ReInfo result = new ReInfo();
		long now = Calendar.getInstance().getTimeInMillis();
		System.out.println("=== now :" + now);
		try {
			BaseDaoImpl db = db_loader.getBaseDaoImpl(I_Constants.DEFAULT_DB, db_name);
			
			List<PlayerInfo> playerInfoList = db.findAll(PlayerInfo.class);
			if (playerInfoList == null || (playerInfoList != null && playerInfoList.size() == 0)) {
				ReInfo m = new ReInfo(1);
	        	m.msg = "No DB Name";
				return m;
			}
			int data_cnt = playerInfoList.size();
			List<WebDataObj> wd_list = new ArrayList<WebDataObj>(data_cnt);
			ArenaInfo arenaInfo = db.find(new Query(Criteria.where(GetWebData.UID).is(BaseDataSource.SERVER_UUID + db_name)), ArenaInfo.class);
		
			int s = 0;
			long nowX = Calendar.getInstance().getTimeInMillis();
			List<PlayerTaskInfo> taskInfoList = db.findAll(PlayerTaskInfo.class);
			s = taskInfoList.size();
			System.out.println("=== size :" + s);
			Map<String, PlayerTaskInfo> taskInfoMap = new HashMap<String, PlayerTaskInfo>(s);
			for (int i = 0; i < s; ++i) {
				PlayerTaskInfo t = taskInfoList.get(i);
				taskInfoMap.put(t.uid, t);
			}
			
			List<PlayerLevelInfo> PlayerLevelInfoList = db.findAll(PlayerLevelInfo.class);
			s = PlayerLevelInfoList.size();
			Map<String, PlayerLevelInfo> PlayerLevelInfoMap = new HashMap<String, PlayerLevelInfo>(s);
			for (int i = 0; i < s; ++i) {
				PlayerLevelInfo t = PlayerLevelInfoList.get(i);
				PlayerLevelInfoMap.put(t.uid, t);
			}
			
			List<PlayerDuelInfo> PlayerDuelInfoList = db.findAll(PlayerDuelInfo.class);
			s = PlayerDuelInfoList.size();
			Map<String, PlayerDuelInfo> PlayerDuelInfoMap = new HashMap<String, PlayerDuelInfo>(s);
			for (int i = 0; i < s; ++i) {
				PlayerDuelInfo t = PlayerDuelInfoList.get(i);
				PlayerDuelInfoMap.put(t.uid, t);
			}
			
			List<PlayerTowerInfo> PlayerTowerInfoList = db.findAll(PlayerTowerInfo.class);
			s = PlayerTowerInfoList.size();
			Map<String, PlayerTowerInfo> PlayerTowerInfoMap = new HashMap<String, PlayerTowerInfo>(s);
			for (int i = 0; i < s; ++i) {
				PlayerTowerInfo t = PlayerTowerInfoList.get(i);
				PlayerTowerInfoMap.put(t.uid, t);
			}
			
			List<PlayerWishInfo> PlayerWishInfoList = db.findAll(PlayerWishInfo.class);
			s = PlayerWishInfoList.size();
			Map<String, PlayerWishInfo> PlayerWishInfoMap = new HashMap<String, PlayerWishInfo>(s);
			for (int i = 0; i < s; ++i) {
				PlayerWishInfo t = PlayerWishInfoList.get(i);
				PlayerWishInfoMap.put(t.uid, t);
			}
			
			List<PlayerDmgRewardInfo> PlayerDmgRewardInfoList = db.findAll(PlayerDmgRewardInfo.class);
			s = PlayerDmgRewardInfoList.size();
			Map<String, PlayerDmgRewardInfo> PlayerDmgRewardInfoMap = new HashMap<String, PlayerDmgRewardInfo>(s);
			for (int i = 0; i < s; ++i) {
				PlayerDmgRewardInfo t = PlayerDmgRewardInfoList.get(i);
				PlayerDmgRewardInfoMap.put(t.uid, t);
			}
			
			List<PlayerMonthcardInfo> PlayerMonthcardInfoList = db.findAll(PlayerMonthcardInfo.class);
			s = PlayerMonthcardInfoList.size();
			Map<String, PlayerMonthcardInfo> PlayerMonthcardInfoMap = new HashMap<String, PlayerMonthcardInfo>(s);
			for (int i = 0; i < s; ++i) {
				PlayerMonthcardInfo t = PlayerMonthcardInfoList.get(i);
				PlayerMonthcardInfoMap.put(t.uid, t);
			}
			
			List<PlayerInvestActivityInfo> PlayerInvestActivityInfoList = db.findAll(PlayerInvestActivityInfo.class);
			s = PlayerInvestActivityInfoList.size();
			Map<String, PlayerInvestActivityInfo> PlayerInvestActivityInfoMap = new HashMap<String, PlayerInvestActivityInfo>(s);
			for (int i = 0; i < s; ++i) {
				PlayerInvestActivityInfo t = PlayerInvestActivityInfoList.get(i);
				PlayerInvestActivityInfoMap.put(t.uid, t);
			}
			
			long nowY = Calendar.getInstance().getTimeInMillis();
			System.out.println("=== delta 1: " + (nowY - nowX));
			
			for (int i = 0; i < data_cnt; ++i) {
				PlayerInfo playerInfo = playerInfoList.get(i);
				WebDataObj webDataObj = new WebDataObj();
				
				wd_list.add(webDataObj);
				webDataObj.uid = playerInfo._id;
				webDataObj.pid = playerInfo.user_base.pid;
				webDataObj.lv = playerInfo.team_lv;
				webDataObj.finish_story = playerInfo.drama_b_end ? 1 : 0;
				webDataObj.name = playerInfo.user_base.getNickname();
				webDataObj.vip = playerInfo.vip_level;
				webDataObj.money = playerInfo.acc_rmb;
				
				if (playerInfo.creat_t != null) {
					webDataObj.creat_t = playerInfo.creat_t;
				}
				if (playerInfo.t_gold != null) {
					webDataObj.t_gold = playerInfo.t_gold;
				}
				if (playerInfo.t_dmd != null) {
					webDataObj.t_dmd = playerInfo.t_dmd;
				}
				if (playerInfo.t_c_dmd != null) {
					webDataObj.t_c_dmd = playerInfo.t_c_dmd;
				}
				if (playerInfo.dmd_draw != null) {
					webDataObj.dmd_draw = playerInfo.dmd_draw;
				}
				if (playerInfo.dmd_draw_ten != null) {
					webDataObj.dmd_draw_ten = playerInfo.dmd_draw_ten;
				}
				if (playerInfo.lmt_draw != null) {
					webDataObj.lmt_draw = playerInfo.lmt_draw;
				}
				
				PlayerTaskInfo taskInfo = taskInfoMap.get(playerInfo._id);
				if (taskInfo != null) {
					webDataObj.cur_task = taskInfo.cur_id;
				}
				
				PlayerLevelInfo levelInfo = PlayerLevelInfoMap.get(playerInfo._id);
				if (levelInfo != null) {
					webDataObj.cur_pve = levelInfo.cur_level;
				}
				
				Entry<Integer, ArenaRankInfo> my_info_entry = arenaInfo.findMyRank(playerInfo._id);
				if (my_info_entry != null) {
					webDataObj.arena = my_info_entry.getKey();
				}
				
				PlayerDuelInfo duel_info = PlayerDuelInfoMap.get(playerInfo._id);
                if (duel_info != null) {
                	webDataObj.duel_score = duel_info.max_score;
                }
                
                PlayerTowerInfo tower_info = PlayerTowerInfoMap.get(playerInfo._id);
                if (tower_info != null) {
                	webDataObj.tower = tower_info.history_box;
                }
                
                PlayerWishInfo wish_info = PlayerWishInfoMap.get(playerInfo._id);
                if (wish_info != null) {
                	webDataObj.wish_lv = wish_info.lv;
                }
                
                PlayerDmgRewardInfo dmg_info = PlayerDmgRewardInfoMap.get(playerInfo._id);
                if (dmg_info != null) {
                	webDataObj.dps = dmg_info.max;
                }
                
                PlayerMonthcardInfo mt_info = PlayerMonthcardInfoMap.get(playerInfo._id);
                if (mt_info != null) {
                	webDataObj.month_card = mt_info.hasType(E_PayType.MONTH.getCode()) ? 1 : 0;
                	webDataObj.life_card = mt_info.hasType(E_PayType.LIFEALL.getCode()) ? 1 : 0;
                	
                	int mk_s = mt_info.lvgift_st.size();
                	for (int mk = 0; mk < mk_s; mk++) {
                		if (mk == 0 
                		&& (mt_info.lvgift_st.get(mk) == PlayerMonthcardInfo.LVGIFT_FINISH 
                			|| mt_info.lvgift_st.get(mk) == PlayerMonthcardInfo.LVGIFT_HOLDING))
                		{
                			webDataObj.libao_1 = 1;
                		} else if (mk == 1 
                        		&& (mt_info.lvgift_st.get(mk) == PlayerMonthcardInfo.LVGIFT_FINISH 
                    			|| mt_info.lvgift_st.get(mk) == PlayerMonthcardInfo.LVGIFT_HOLDING))
                    	{
                    		webDataObj.libao_2 = 1;
                    	}
                	}
                }
                
                PlayerInvestActivityInfo iv_info = PlayerInvestActivityInfoMap.get(playerInfo._id);
                if (iv_info != null) {
                	webDataObj.invest = iv_info.has_invest ? 1 : 0;
                }
			}
			
			long nowZ = Calendar.getInstance().getTimeInMillis();
			System.out.println("=== delta 2: " + (nowZ - nowY));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuffer bf = new StringBuffer(data_cnt * ELEM_LEN);
			bf.append("ID,平台,名字,等级,主线,关卡,竞技排名,累计功勋,爬塔,许愿等级,最高DPS,月卡,终身卡,投资,vip,充值,完成剧情,创建时间,总金币,总钻石,总充值钻石,单抽,十连,限量,礼包1,礼包2\n");
			Calendar cal = Calendar.getInstance();
			for(int i = 0; i < data_cnt; ++i) {
				WebDataObj obj = wd_list.get(i);
				bf.append(obj.uid).append(DOT);
				bf.append(obj.pid).append(DOT);
				bf.append(obj.name).append(DOT);
				bf.append(obj.lv).append(DOT);
				bf.append(obj.cur_task).append(DOT);
				bf.append(obj.cur_pve).append(DOT);
				bf.append(obj.arena).append(DOT);
				bf.append(obj.duel_score).append(DOT);
				bf.append(obj.tower).append(DOT);
				bf.append(obj.wish_lv).append(DOT);
				bf.append(obj.dps).append(DOT);
				bf.append(obj.month_card).append(DOT);
				bf.append(obj.life_card).append(DOT);
				bf.append(obj.invest).append(DOT);
				bf.append(obj.vip).append(DOT);
				bf.append(obj.money).append(DOT);
				bf.append(obj.finish_story).append(DOT);
				cal.setTimeInMillis(obj.creat_t);
				bf.append(formatter.format(cal.getTime())).append(DOT);
				bf.append(obj.t_gold).append(DOT);
				bf.append(obj.t_dmd).append(DOT);
				bf.append(obj.t_c_dmd).append(DOT);
				bf.append(obj.dmd_draw).append(DOT);
				bf.append(obj.dmd_draw_ten).append(DOT);
				bf.append(obj.lmt_draw).append(DOT);
				bf.append(obj.libao_1).append(DOT);
				bf.append(obj.libao_2).append(DOT);
				
				bf.append(NEXT);
			}
			long nowA = Calendar.getInstance().getTimeInMillis();
			System.out.println("=== delta 3: " + (nowA - nowZ));
			result.msg = bf.toString();
			long nowB = Calendar.getInstance().getTimeInMillis();
			System.out.println("=== delta 4: " + (nowB - nowA));
		} catch (Exception e) {
			e.printStackTrace();
        	ReInfo m = new ReInfo(1);
        	m.msg = e.getMessage();
			return m;
		}
		long now2 = Calendar.getInstance().getTimeInMillis();
		System.out.println("=== pro now :" + now2);
		System.out.println("=== end delta : " + (now2 - now));
		return result;
	}
}

//下发的数据对象
class WebDataObj {
    public String uid = "0";
    public String pid = "";
    public String name = "";
    public int lv = 0;  //队伍等级
    public int cur_task = 0;  //当前主线任务
    public int cur_pve = 0;     //当前关卡
    public int arena = 0;       //当前竞技场排名
    public int duel_score = 0;   //一骑当千累计功勋
    public int tower = 0;       //爬塔记录
    public int wish_lv = 0;        //许愿等级
    public int dps = 0;             //最高dps
    public int month_card = 0;     
    public int life_card = 0;
    public int invest = 0;
    public int vip = 0;
    public int money = 0;
    public int finish_story = 0;
    
	public Long creat_t = 0L;					//创建时间
	public Double t_gold = 0.0;					//总金币
	public Double t_dmd = 0.0;					//总钻石
	public Double t_c_dmd = 0.0;				//总充值钻石
	public Integer dmd_draw = 0;				//总钻石单抽次数
	public Integer dmd_draw_ten = 0;				//总钻石10抽次数
	public Integer lmt_draw = 0;				//总限量抽次数
	
	public int libao_1 = 0;			//是否购买限时礼包1
	public int libao_2 = 0;			//是否购买限时礼包2
}
