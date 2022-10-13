package com.ssmGame.module.battle;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmData.dbase.RoleInfo;

@Service
@Scope("prototype")
public class BattleImpl {
	private static final Logger log = LoggerFactory.getLogger(BattleImpl.class);
    /**
     * 数据访问对象
     */
    @Autowired PlayerRolesInfoDB player_roles_db;

    /**
     * 获取实例
     * @return
     */
    public  final static BattleImpl getInstance(){
        return SpringContextUtil.getBean(BattleImpl.class);
    }

    /**
     * 模拟一场战斗, 测试用
     * @return
     */
    public BattlePack simulateTest(String player_id) {

        BattleSimulator sim = new BattleSimulator();
        
        //todo 要不同的对局模块传入不同的数据
        Map<Integer, RoleInfo> left = new HashMap<Integer, RoleInfo>();
        PlayerRolesInfo info = player_roles_db.load(player_id);
        if (info == null)
        {
        	log.error("simulate ERROR: no player id {}", player_id);
        	return null;
        }
        for (Entry<Integer, Integer> m : info.pve_team.entrySet())
        {
        	for (RoleInfo i : info.roles)
        	{
        		if (i.role_id == m.getValue())
        		{
        			left.put(m.getKey(), i.Clone());
        			break;
        		}
        	}
        }
        
        //右边复制左边
        Map<Integer, RoleInfo> right = new HashMap<Integer, RoleInfo>();
        for (Entry<Integer, RoleInfo> m : left.entrySet())
        {
        	right.put(m.getKey(), m.getValue().Clone());
        }
        sim.InitAllActorLogic(left, right, ConfigConstant.tConf.getBattlelimit()
        		, ConfigConstant.tConf.getBCTime()
        		, Math.random() > 0.5 ? BattleType.LEVEL_NORMAL : BattleType.LEVEL_BOSS
        				, null, null, null, null);
        
        return sim.Exe();
    }
    
    public BattlePack simulate(Map<Integer, RoleInfo> left, Map<Integer, RoleInfo> right
    		, int max_step, int min_step, int battle_type)
    {
    	//log.info("开始模拟一场战斗！");
    	BattleSimulator sim = new BattleSimulator();
    	sim.InitAllActorLogic(left, right, ConfigConstant.tConf.getBattlelimit()
        		, ConfigConstant.tConf.getBCTime()
        		, Math.random() > 0.5 ? BattleType.LEVEL_NORMAL : BattleType.LEVEL_BOSS
        				, null, null, null, null);
        
        return sim.Exe();
    }
}
