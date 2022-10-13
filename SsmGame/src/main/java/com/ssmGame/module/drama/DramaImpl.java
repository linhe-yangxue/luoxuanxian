package com.ssmGame.module.drama;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Role;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.RoleInfo;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.drama.DramaMsg;
import com.ssmGame.module.battle.BattlePack;
import com.ssmGame.module.battle.BattleSimulator;
import com.ssmGame.module.battle.BattleType;

@Service
@Scope("prototype")
public class DramaImpl {
	@Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    public void destroy()
	{
		m_player = null;
		m_player_db = null;
	}
	
	private static final Logger log = LoggerFactory.getLogger(DramaImpl.class);
	
	public final static DramaImpl getInstance(){
        return SpringContextUtil.getBean(DramaImpl.class);
	}
	
	public CommonMsg handleBattle(CommonMsg respond)
	{
		DramaMsg msg = new DramaMsg();
		respond.body.drama = msg;
		msg.success = false;
		
        // 生成双方阵容
        Map<Integer, RoleInfo> my_roles = genRoles(ConfigConstant.tDrama.getFriend(), ConfigConstant.tDrama.getFriendLV());
        Map<Integer, RoleInfo> ene_roles = genRoles(ConfigConstant.tDrama.getEnemy(), ConfigConstant.tDrama.getEnemyLV());

        // 模拟PVE战斗
        BattleSimulator sim = new BattleSimulator();
        sim.InitAllActorLogic(my_roles, ene_roles, ConfigConstant.tConf.getBattlelimit(), ConfigConstant.tConf.getBCTime(), BattleType.DRAMA_BATTLE
        		, null, null, null, null);
        BattlePack battle_result = sim.Exe();
        msg.script = battle_result;

		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleBattleEnd(CommonMsg respond)
	{
    	String uid = respond.header.uid;
		DramaMsg msg = new DramaMsg();
		respond.body.drama = msg;
		msg.success = false;
		
		m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("DramaImpl.handleBattleEnd() no player id {}", uid);
    		return respond;
    	}
    	m_player.drama_b_end = true;
    	m_player_db.save();

		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleDialogueEnd(CommonMsg respond)
	{
    	String uid = respond.header.uid;
		DramaMsg msg = new DramaMsg();
		respond.body.drama = msg;
		msg.success = false;
		
		m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("DramaImpl.handleDialogueEnd() no player id {}", uid);
    		return respond;
    	}
    	m_player.drama_end = true;
    	m_player_db.save();

		msg.success = true;
		return respond;
	}
	
	private Map<Integer, RoleInfo> genRoles(int[] role_id, int[] lv) {
		Map<Integer, RoleInfo> result = new HashMap<Integer, RoleInfo>();
		for(int i = 0; i < role_id.length; i++){

            Role role_cfg = ConfigConstant.tRole.get(role_id[i]);
            if(role_cfg == null) {
                continue;
            }

            RoleInfo role_info = new RoleInfo();

            role_info.InitByRoleConfigIdAndLv(role_id[i], lv[i]);
            role_info.base_lv = 1; // 普攻等级
            role_info.skill_lv = ConfigConstant.tRole.get(role_id[i]).getSkillLv(); // 特殊技能等级

            result.put(i + 1, role_info);
        }
		return result;
	}
}
