package com.ssmGame.module.sns;

import java.util.ArrayList;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Invite;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmGame.defdata.msg.activity.ActivityMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.util.TimeUtils;

@Service
@Scope("prototype")
public class SnsImpl {
	private static final Logger log = LoggerFactory.getLogger(SnsImpl.class);
	
	public final static SnsImpl getInstance(){
        return SpringContextUtil.getBean(SnsImpl.class);
	}
    
	public CommonMsg handleFollowAward(CommonMsg respond, CommonMsg receive)
	{
		String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (null == p_info) {
			log.error("no player info {}", uid);
			return respond;
		}
		
		if (p_info.fl_award == 1) {
			return respond;
		}
		
		int add_dmd = ConfigConstant.tConf.getFollow();
		p_info.fl_award = 1;
		p_info.addDiamond(add_dmd);
		p_db.save();
		respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.diamond = add_dmd;
		msg.r_diamond = add_dmd;
		msg.success = true;
		return respond;
	}
	
	public static void handleLogin(PlayerInfo p_info) {
		if (p_info.fl_award == null) {
			p_info.fl_award = 0;
		}
		if (p_info.share_cnt == null) {
			p_info.share_cnt = 0;
		}
		if (p_info.inv_award == null) {
			p_info.inv_award = new ArrayList<Integer>();
		}
		if (p_info.share_t == null) {
			p_info.share_t = 0L;
		}
		if (p_info.qq_cut == null) {
			p_info.qq_cut = 0;
		}
		if (p_info.inv_t == null) {
			p_info.inv_t = 0L;
		}
		Calendar now_cal = Calendar.getInstance();
		int day = now_cal.get(Calendar.DAY_OF_YEAR);
		Calendar test_cal = Calendar.getInstance();
		test_cal.setTimeInMillis(p_info.share_t);
		int test_day = test_cal.get(Calendar.DAY_OF_YEAR);
		if (day != test_day) {
			p_info.share_cnt = 0;
		}
		Calendar log_cal = Calendar.getInstance();
		log_cal.setTimeInMillis(p_info.inv_t);
		int log_day = log_cal.get(Calendar.DAY_OF_YEAR);
		if (day != log_day) {
			p_info.inv_award.clear();
			p_info.inv_t = now_cal.getTimeInMillis();
		}
	}
	
	public CommonMsg handleInviteOnce(CommonMsg respond, CommonMsg receive)
	{
		String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (null == p_info) {
			log.error("no player info {}", uid);
			return respond;
		}
		
		handleLogin(p_info);
		
		if (p_info.share_cnt >= ConfigConstant.tConf.getInviteNum()) {
			log.error("p_info.share_cnt too big {} {}", uid, p_info.share_cnt);
			return respond;
		}
		
		Long now = Calendar.getInstance().getTimeInMillis();
		if (now - p_info.share_t < ConfigConstant.tConf.getInviteTime() * TimeUtils.ONE_MIN_TIME) {
			log.error("in cd {} now {} shart {}", uid, now, p_info.share_cnt);
			return respond;
		}
		
		p_info.share_t = now;
		p_info.share_cnt++;
		int add_dmd = ConfigConstant.tConf.getInviteReward();
		p_info.addDiamond(add_dmd);
		p_db.save();
		respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.diamond = add_dmd;
		msg.r_diamond = add_dmd;
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleInviteReward(CommonMsg respond, CommonMsg receive) {
		
		String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (null == p_info) {
			log.error("no player info {}", uid);
			return respond;
		}
		
		handleLogin(p_info);
		
		Invite i_cfg = ConfigConstant.tInvite.get(receive.body.activity.reward_id);
		if (null == i_cfg) {
			log.error("no invite cfg id {} player {}", receive.body.activity.reward_id, uid);
			return respond;
		}
		
		if (p_info.inv_award.contains(i_cfg.getID())) {
			return respond;
		}
		
		int now_cnt = 0;
		if (p_info.user_base.getCurrDay() != null) {
			now_cnt = p_info.user_base.getCurrDay();
		}
		if (now_cnt < i_cfg.getPeople()) {
			return respond;
		}
		
		p_info.inv_award.add(i_cfg.getID());
		int add_dmd = i_cfg.getJewel();
		p_info.addDiamond(add_dmd);
		p_db.save();
		respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.diamond = add_dmd;
		msg.r_diamond = add_dmd;
		msg.success = true;
		
		return respond;
	}
	
	public CommonMsg handleQQCutStatusChg(CommonMsg respond, CommonMsg receive) {
		String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (null == p_info) {
			log.error("no player info {}", uid);
			return respond;
		}
		if (null == p_info.qq_cut) {
			p_info.qq_cut = 0;
		}
		if (p_info.qq_cut == 0) {
			p_info.qq_cut = 1;
		}
		p_db.save();
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleQQCutReward(CommonMsg respond, CommonMsg receive) {
		String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (null == p_info) {
			log.error("no player info {}", uid);
			return respond;
		}
		if (null == p_info.qq_cut) {
			p_info.qq_cut = 0;
		}
		if (p_info.qq_cut == 1) {
			p_info.qq_cut = 2;
			int add_dmd = ConfigConstant.tConf.getWanBa();
			p_info.addDiamond(add_dmd);
			respond.body.sync_player_info = new SyncPlayerInfoMsg();
			respond.body.sync_player_info.diamond = add_dmd;
			msg.r_diamond = add_dmd;
		}
		p_db.save();
		msg.success = true;
		return respond;
	}
}


