package com.ssmGame.module.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Diamondplate;
import com.ssmData.config.entity.Vip;
import com.ssmData.dbase.PlayerDmdplateDB;
import com.ssmData.dbase.PlayerDmdplateInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmGame.defdata.msg.activity.ActivityMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.player.PlayerImpl;
import com.ssmGame.util.RandomMethod;

@Service
@Scope("prototype")
public class DmdplateImpl {
	
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerDmdplateDB m_act_db;
    PlayerDmdplateInfo m_act;
    
    public void destroy()
	{
		m_player = null;
		m_player_db = null;
		m_act_db = null;
		m_act = null;
	}
	
	private static final Logger log = LoggerFactory.getLogger(DmdplateImpl.class);
	
	public final static DmdplateImpl getInstance(){
        return SpringContextUtil.getBean(DmdplateImpl.class);
	}
	
	public CommonMsg handleInfo(CommonMsg respond)
	{
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("DmdplateImpl.handleReward NO player ID {}", uid);
    		return respond;
    	}
    	
    	long now = Calendar.getInstance().getTimeInMillis();
    	if (!ActivityType.checkActivityOpen(ActivityType.Dmdplate, now, m_player)) {
    		return respond;
    	}
		
    	m_act = m_act_db.loadByUid(uid);
    	if (m_act == null) {
    		log.warn("DmdplateImpl.handleInfo NO DB {}", uid);
    		return respond;
    	}
    
    	msg.dmd_plate_cnt = m_act.draw_cnt;
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleRoll(CommonMsg respond)
    {
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
    	
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("TurnplateImpl.handleRoll NO player ID {}", uid);
    		return respond;
    	}
    	
    	long now = Calendar.getInstance().getTimeInMillis();
    	if (!ActivityType.checkActivityOpen(ActivityType.Dmdplate, now, m_player)) {
    		log.warn("DmdplateImpl.handleInfo NO OPEN {}", uid);
    		return respond;
    	}
    	
    	m_act = m_act_db.loadByUid(uid);
    	if (m_act == null){
    		log.warn("DmdplateImpl.handleRoll no act_db {}", uid);
    		return respond;
    	}
    	
    	Vip v_cfg = ConfigConstant.tVip.get(m_player.vip_level);
    	if (v_cfg == null) {
    		return respond;
    	}
    	if (m_act.draw_cnt >= v_cfg.getPlate()) {
    		return respond;
    	}
    	
    	int cost = ConfigConstant.tConf.getDialDiamond();
    	if (!m_player.hasDiamond(cost)) {
    		return respond;
    	}
    	PlayerImpl.SubDiamond(m_player, cost);
    	
    	List<Integer> dmds = new ArrayList<Integer>();
    	List<Integer> probs = new ArrayList<Integer>();
    	List<Integer> ids = new ArrayList<Integer>();
    	getInfo(m_act.draw_cnt++, dmds, probs, ids);
    	    	
    	int hit = RandomMethod.CalcHitWhichIndex(probs);
    	if (hit == -1)
		{
			log.warn("DmdplateImpl() Random Error!");
			return respond;
		}
    	
    	int add_dmd = dmds.get(hit);
    	m_player.addDiamond(add_dmd);
    	msg.turn_id = ids.get(hit);
    	
		if (respond.body.sync_player_info == null)
			respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.diamond = add_dmd - cost;
    	
    	m_act_db.save();
    	m_player_db.save();
    	
    	msg.success = true;
    	return respond;
    }
	
	private void getInfo(int my_cnt, List<Integer> dmds, List<Integer> probs, List<Integer> ids) {
		dmds.clear();
		probs.clear();
		ids.clear();
		for (Entry<Integer, Diamondplate> cfg : ConfigConstant.tDiamondplate.entrySet()) {
			Diamondplate d_cfg = cfg.getValue();
			dmds.add(d_cfg.getDiamond() + my_cnt * d_cfg.getAdd());
			probs.add(d_cfg.getWeight());
			ids.add(d_cfg.getID());
		}
	}
}
