package com.ssmGame.module.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Role;
import com.ssmData.dbase.RoleInfo;

public class BattleSimulator {
	public static final Logger log = LoggerFactory.getLogger(BattleSimulator.class);
	
	private int m_current_step = 0;
	
    //外层索引是参战者的 gaming_id
    public List<ScriptElement> m_actor_script = new ArrayList<ScriptElement>();
    
    public Map<Integer, ActorLogic>m_all_actors_logic = new HashMap<Integer, ActorLogic>();
    
    public int m_battle_type = 0;  //战斗类型，是否boss战
    public int m_search_time = 0;  //搜索时段的时长，毫秒
    public int m_max_step = 0;
    public int m_min_step = 0;
    
    //参数格式  Key坐位编号-Value场外数据
    public void InitAllActorLogic(Map<Integer, RoleInfo> left, Map<Integer, RoleInfo> right
    		, int max_step, int min_step, int battle_type
    		, Map<Integer, Integer> modify_left_hp
    		, Map<Integer, Integer> modify_left_rage
    		, Map<Integer, Integer> modify_right_hp
    		, Map<Integer, Integer> modify_right_rage)
    {
    	m_battle_type = battle_type;
    	m_search_time = ConfigConstant.tConf.getBattletime();
    	m_max_step = max_step;
    	m_min_step = min_step;
    	
    	m_all_actors_logic = new HashMap<Integer, ActorLogic>();
    	int gaming_id = 0;
    	InitOneSide(left, BattleSide.Left, gaming_id, modify_left_hp, modify_left_rage);
    	gaming_id = left.size();
    	InitOneSide(right, BattleSide.Right, gaming_id, modify_right_hp, modify_right_rage);
    }
    
    private void InitOneSide(Map<Integer, RoleInfo> data, int side, int start_index
    		, Map<Integer, Integer> modify_hp
    		, Map<Integer, Integer> modify_rage)
    {
    	for (Entry<Integer, RoleInfo> e : data.entrySet())
    	{
    		int cur_hp = 0;
    		if (modify_hp != null && modify_hp.containsKey(e.getValue().role_id))
    		{
    			cur_hp = modify_hp.get(e.getValue().role_id);
    			if (cur_hp == 0)
    				continue;
    		}
    		ActorLogic a = new ActorLogic();
    		a.m_gaming_id = start_index++;
    		a.m_side = side;
    		a.m_pos = e.getKey();
    		a.m_r_inf = e.getValue().Clone();
    		Role role_config = ConfigConstant.tRole.get(a.m_r_inf.role_id);
    		a.m_base_id = role_config.getBase();
    		a.m_skill_id = role_config.getSkill();
    		a.m_current_hp = e.getValue().max_hp;
    		if (cur_hp > 0)
    			a.m_current_hp = cur_hp;
    		a.m_current_rage = a.m_r_inf.en;
    		if (modify_rage != null && modify_rage.containsKey(a.m_r_inf.role_id))
    		{
    			a.m_current_rage = modify_rage.get(a.m_r_inf.role_id);
    		}
    		m_all_actors_logic.put(a.m_gaming_id, a);
    		
    		/*a.m_max_hp = RoleAttrCalc.Hp(e.getValue());
    		a.m_atk_speed = role_config.getAtkspeed();
    		a.m_atk = RoleAttrCalc.BaseAtk(e.getValue());
    		a.m_def = RoleAttrCalc.BaseDef(e.getValue());
    		a.m_skill_atk = RoleAttrCalc.BaseSkillAtk(e.getValue());
    		a.m_skill_def = RoleAttrCalc.BaseSkillDef(e.getValue());
    		a.m_crit_rate = role_config.getCritRate(); //todo
    		a.m_crit_damage = role_config.getCritDamage();
    		a.m_damage_reduce = role_config.getDamageReduce();
    		a.m_debuff_res = role_config.getDebuffRes();
    		a.m_sp_ignore = role_config.getSpIgnore();
    		a.m_vampire = role_config.getVampire();
    		a.m_combo = role_config.getCombo();*/
    		//log.info("BattleSim Init role {}", a.m_r_inf.role_id);
    	}
    }
    
    public ActorLogic GetActor(int gaming_id)
    {
    	return m_all_actors_logic.get(gaming_id);
    }
    
    public List<ActorLogic> GetActorsAllEnemey(int gaming_id)
    {
    	List<ActorLogic> result = new ArrayList<ActorLogic>();
    	for (ActorLogic a : m_all_actors_logic.values())
    	{
    		if (a.m_is_alive && a.m_current_target == gaming_id)
            {
                result.add(a);
            }
    	}
        return result;
    }
    
    public int SearchTargetGamingId(int subject_pos, int side, boolean is_special)
    {
    	int target_side = side == BattleSide.Left ? BattleSide.Right : BattleSide.Left;
        int[] targets = null;
        int result = -1;
        switch (subject_pos)
        {
            case 1:
                targets = ConfigConstant.tConf.getPos_1_targets();
                break;
            case 2:
            case 4:
                targets = ConfigConstant.tConf.getPos_2_4_targets();
                break;
            case 3:
            case 5:
                targets = ConfigConstant.tConf.getPos_3_5_targets();
                break;
        }
        if (is_special)
        {
        	targets = SkillTarget.SpecialList;
        }
        if (targets == null)
        {
        	System.out.println("subject_id ERROR!");
            return result;
        }

        for (int i = 0; i < targets.length; ++i)
        {
        	for (ActorLogic logic : m_all_actors_logic.values())
        	{
        		if (!logic.m_is_alive || logic.m_side != target_side || logic.m_current_hp <= 0)
                {
                    continue;
                }
                if (logic.m_pos == targets[i])
                {
                    result = logic.m_gaming_id;
                    break;
                }
        	}
            if (result != -1)
            {
                break;
            }
        }
        return result;
    }
    
    public void Write(int subject_id, BattleCommand cmd)
    {
    	ScriptElement found = null;
    	for (int i = 0; i < m_actor_script.size(); ++i)
    	{
    		if (m_actor_script.get(i).m_gaming_id == subject_id)
    		{
    			found = m_actor_script.get(i);
    		}
    	}
    	if (found == null)
    	{
    		found = new ScriptElement();
    		found.m_gaming_id = subject_id;
    		this.m_actor_script.add(found);
    	}
    	found.m_cmd_list.add(cmd);
    }
    
    public BattlePack Exe()
    {
    	BattlePack result = new BattlePack();
    	result.m_battle_type = m_battle_type;
    	result.m_max_step = m_max_step;
    	result.m_search_time = m_search_time;
        Ready();
        int enemy_max_hp = 0;
        result.m_actors_data = new ArrayList<ActorLogic>();
        for (ActorLogic a : m_all_actors_logic.values())
        {
        	result.m_actors_data.add(a.Clone());
        	if (a.m_side == BattleSide.Right) {
        		enemy_max_hp += a.m_r_inf.max_hp;
        	}
        }

        this.Go();
        
        result.m_actors_script = m_actor_script;
        result.m_result = BattleResult.LEFT_WIN;
        for (ActorLogic a : m_all_actors_logic.values())
        {
        	if (a.m_side == BattleSide.Right && a.m_is_alive)
            {
                result.m_result = BattleResult.LEFT_LOSE;
                break;
            }
        }        
        if (this.m_current_step >= m_max_step)
        {
            result.m_result = BattleResult.LEFT_LOSE;
        }
        if (this.m_current_step < m_min_step)
        {
        	result.m_search_time += (m_min_step - this.m_current_step);
        }
        
        ///////////一骑当千的平局
        if (result.m_battle_type == BattleType.DUEL
        		||result.m_battle_type ==  BattleType.DUEL_FIRST
        		|| result.m_battle_type == BattleType.GUILD_WAR)
        {
        	boolean left_alive = false;
        	boolean right_alive = false;
        	for (ActorLogic a : m_all_actors_logic.values())
            {
            	if (a.m_side == BattleSide.Left)
                {
            		if (a.m_is_alive) {
            			left_alive = true;
            		} else {
            			result.l_dead++;
            		}
                }
            	if (a.m_side == BattleSide.Right && a.m_is_alive)
            	{
            		if (a.m_is_alive) {
            			right_alive = true;
            		} else {
            			result.r_dead++;
            		}
            	}
            }
        	if (left_alive && right_alive && this.m_current_step >= m_max_step)
        	{
        		result.m_result = BattleResult.DRAW;
        	}
        	else if (!left_alive && !right_alive)
        	{
        		result.m_result = BattleResult.DRAW;
        	}
        }
        
        for (ActorLogic a : m_all_actors_logic.values()) {
        	for (ActorLogic b : result.m_actors_data) {
            	if (a.m_side == BattleSide.Left && a.m_base_id == b.m_base_id) {
            		int t = m_current_step;
            		if (a.d_t != null) {
            			t = a.d_t;
            		}
            		b.t_dmg = a.t_dmg;
            		b.d_rate = (int)((a.t_dmg / (float)enemy_max_hp) * 10000);
            		b.dps = (int)((a.t_dmg / (float)t) * 1000);
            		break;
            	}
        	}
        }

        BattleCommand end_cmd = new BattleCommand();
        end_cmd.m_step = m_current_step + 1500;
        end_cmd.m_action_type = BattleCommandType.GameOver;
        Write(-1, end_cmd);

        return result;
    }
    
    private void Ready()
    {
    	m_actor_script = new ArrayList<ScriptElement>();

        //填入初始命令
        for (ActorLogic a : m_all_actors_logic.values())
        {
        	int search_gaming_id = SearchTargetGamingId(a.m_pos, a.m_side, false);
            if (-1 != search_gaming_id)
            {
            	BattleCommand cmd = new BattleCommand();
                cmd.m_step = 0;
                cmd.m_action_type = BattleCommandType.Search;
                cmd.m_action_args.add(search_gaming_id);
                a.m_command_list.add(cmd);
            }
        }
    }
    
    private void Go()
    {
    	boolean left_alive = false;
    	boolean right_alive = false;
    	List<ActorLogic> alive_actor = new ArrayList<ActorLogic>(10);
    	int closest_cmd_step = 0;
    	int a_min = 0;
        while (true)
        {
            if (m_current_step >= m_max_step)
            {
                break;
            }

            left_alive = false;
            right_alive = false;
            alive_actor.clear();
            for (ActorLogic a : m_all_actors_logic.values())
            {
            	if (a.m_is_alive)
                {
                    alive_actor.add(a);
                    if (a.m_side == BattleSide.Left)
                	{
                		left_alive = true;
                	}
                	else if (a.m_side == BattleSide.Right)
                	{
                		right_alive = true;
                	}
                }
            }
            if (alive_actor.size() <= 0 || !right_alive || !left_alive)
            {
                break;
            }

            closest_cmd_step = m_max_step;
            for (int i = 0; i < alive_actor.size(); ++i)
            {
            	ActorLogic alive = alive_actor.get(i);
            	a_min = alive.GetClosestCmdStep();
                if (a_min < closest_cmd_step)
                {
                    closest_cmd_step = a_min;
                }
            }
            
            if (closest_cmd_step == m_max_step)
            {
                break;
            }

            //如果产生状态变化的，下一毫秒生效
            for (int i = 0; i < alive_actor.size(); ++i)
            {
            	ActorLogic a = alive_actor.get(i);
                List<BattleCommand> old_cmd = a.m_command_list;
                a.m_command_list = new ArrayList<BattleCommand>();

                for (int j = 0; j < old_cmd.size(); ++j)
                {
                	BattleCommand c = old_cmd.get(j);
                	if (c.m_step == closest_cmd_step)
                    {
                        BattleCmdCenter.ProcessCmd(a, c, closest_cmd_step, old_cmd, this);
                        if (c.m_action_type == BattleCommandType.Die)
                        {
                            break;
                        }
                    }
                    else
                    {
                        a.m_command_list.add(c);
                    }
                }
            }

            m_current_step = closest_cmd_step;
        }
    }
}
