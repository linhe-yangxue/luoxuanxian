package com.ssmGame.module.battle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Buff;
import com.ssmData.config.entity.Skill;
import com.ssmGame.module.role.RoleAttrType;

class DmgResult
{
	public int hp;
	public int is_crit = 0;  // 0- 不是 ， 1-是
}

public class BattleCmdCenter {
	
	//用于目标死后要进行一次搜索
	private static void SubjectAddNextSearch(ActorLogic subject, BattleCommand old_cmd
			, int now, BattleSimulator sim)
	{
		int new_target = sim.SearchTargetGamingId(subject.m_pos, subject.m_side, subject.HasBuffSpType(BuffSpType.SpecialTarget));
        if (new_target != -1)
        {
        	BattleCommand new_seach_cmd = new BattleCommand();
            new_seach_cmd.m_step = now + 1;
            new_seach_cmd.m_action_type = BattleCommandType.Search;
            new_seach_cmd.m_action_args.add(new_target);
            subject.m_command_list.add(new_seach_cmd);
            old_cmd.m_action_args.set(0, new_target);
            subject.m_command_list.add(old_cmd);
        }
	}
	
	private static void SubjectAddNextAtk(ActorLogic subject, ActorLogic target, int now)
	{
		BattleCommand next_atk = new BattleCommand();
        next_atk.m_step = now + subject.m_r_inf.atk_speed;
        next_atk.m_action_type = BattleCommandType.Attack;
        next_atk.m_action_args.add(target.m_gaming_id);
        subject.m_command_list.add(next_atk);
	}
	
	public static void ProcessCmd(ActorLogic subject, BattleCommand cmd, int now, List<BattleCommand> old_cmd, BattleSimulator sim)
	{
		if (subject != null && subject.m_is_alive && cmd != null)
        {
            switch (cmd.m_action_type)
            {
                case BattleCommandType.Search:
                    if (subject.m_current_target == cmd.m_action_args.get(0))
                    {
                        break;
                    }       
                    if (subject.HasBuffSpType(BuffSpType.Dizzy))
                        break; 
                    BattleCommand old_atk_cmd = null;
                    subject.m_current_target = cmd.m_action_args.get(0);
                    for (BattleCommand o : old_cmd)
                    {
                    	if (o.m_action_type == BattleCommandType.Attack)
                        {
                            old_atk_cmd = o;
                        }
                    }

                    if (old_atk_cmd == null)
                    {
                        old_atk_cmd = new BattleCommand();
                        old_atk_cmd.m_action_type = BattleCommandType.Attack;
                        old_atk_cmd.m_step = now + subject.m_r_inf.atk_speed;
                        old_atk_cmd.m_action_args.add(cmd.m_action_args.get(0));
                        subject.m_command_list.add(old_atk_cmd);
                    }
                    else
                    {
                        old_atk_cmd.m_action_args.set(0, cmd.m_action_args.get(0));
                        old_atk_cmd.m_step += ConfigConstant.tConf.getAddspeed();
                    }
                    sim.Write(subject.m_gaming_id, cmd);
                break;
                case BattleCommandType.Attack:
                	ProcessAttackCmd(subject, cmd, now, old_cmd, sim);
                break;
                case BattleCommandType.Die:
                    if (subject.m_current_hp <= 0)
                    {
                        subject.m_is_alive = false;
                        sim.Write(subject.m_gaming_id, cmd);
                    }
                break;
                case BattleCommandType.AddBuff:
                	ProcessAddBuffCmd(subject, cmd, now, old_cmd, sim);
                break;
                case BattleCommandType.RemoveBuff:
                	int buff_id = cmd.m_action_args.get(0);
            		Buff buff_config = ConfigConstant.tBuff.get(buff_id);
                    if (buff_config == null)
                    	break;
                	for (int i = 0; i < subject.m_buff_list.size(); ++i)
                	{
                		if (subject.m_buff_list.get(i).id == buff_id)
                		{
                			subject.m_buff_list.remove(i);
                			sim.Write(subject.m_gaming_id, cmd);
                			
                			if (buff_config.getSpecialTp() == BuffSpType.Dizzy
                					&& !subject.HasBuffSpType(BuffSpType.Dizzy))
                			{
                				FindNewTarget(subject, now, sim);
                			}
                			break;
                		}
                	}
                break;
                case BattleCommandType.BuffHit:
                	ProcessBuffHitCmd(subject, cmd, now, sim);
                break;
            }
        }
	}
	
	private static void ProcessBuffHitCmd(ActorLogic subject, BattleCommand cmd, int now, BattleSimulator sim)
	{
		int buff_id = cmd.m_action_args.get(0);
		Buff buff_config = ConfigConstant.tBuff.get(buff_id);
        if (buff_config == null)
        	return;
        int count = cmd.m_action_args.get(1);
        if (count <= 0)
        	return;
        BuffInfo found = null;
        for (BuffInfo b : subject.m_buff_list)
        {
        	if (b.id == buff_id)
        	{
        		found = b;
        		break;
        	}
        }
        if (found == null)
        	return;
        
        //计算伤害
        int hp = (int)Math.floor((buff_config.getTickDamage() + (found.lv - 1) * buff_config.getTickDamageLv()) * found.stack);
        if (hp < 0 && subject.HasBuffSpType(BuffSpType.CantHeal))
        {
        	hp = 0;
        }
        if (cmd.a_id != null) {
        	ActorLogic at = sim.GetActor(cmd.a_id);
        	if (at != null) {
        		at.t_dmg += hp;
        	}
        }
        subject.m_current_hp -= hp;
        
        if (subject.m_current_hp <= 0)
        {
        	SomeOneDying(subject, now, sim);
        }
        else if (count > 1)//还活着，就继续加伤害指令
        {
        	BattleCommand new_cmd = cmd.Clone();
        	new_cmd.m_action_args.set(1, count - 1);
        	new_cmd.m_step = now + buff_config.getTickTime();
        	subject.m_command_list.add(new_cmd);
        }
        cmd.m_action_args.add(hp);
        sim.Write(subject.m_gaming_id, cmd);
        
	}
	
	private static void ProcessAddBuffCmd(ActorLogic subject, BattleCommand cmd, int now,  List<BattleCommand> old_cmd, BattleSimulator sim)
	{
        BattleCommand old_atk_cmd = null;
        int buff_id = cmd.m_action_args.get(0);
        Buff buff_config = ConfigConstant.tBuff.get(buff_id);
        if (buff_config == null)
        	return;
        sim.Write(subject.m_gaming_id, cmd);
        
        for (BattleCommand o : old_cmd)
        {
        	if (o.m_action_type == BattleCommandType.RemoveBuff
        		&& o.m_action_args.get(0) == buff_id)
            {
                old_atk_cmd = o;
            }
        }
        if (old_atk_cmd == null)
        {
            old_atk_cmd = new BattleCommand();
            old_atk_cmd.m_action_type = BattleCommandType.RemoveBuff;
            old_atk_cmd.m_step = now + buff_config.getTime();
            old_atk_cmd.m_action_args.add(buff_id);
            subject.m_command_list.add(old_atk_cmd);
        }
        else
        {
        	old_atk_cmd.m_step = now + buff_config.getTime();
        }
        
        boolean has_same = false;
        for (int i = 0; i < subject.m_buff_list.size(); ++i)
        {
        	if (subject.m_buff_list.get(i).id == buff_id)
        	{
        		has_same = true;
        		subject.m_buff_list.get(i).lv = cmd.m_action_args.get(1);
        		if (buff_config.getCoverTp() == BuffCoverType.AddUP 
        				&& subject.m_buff_list.get(i).stack < buff_config.getMaxStack())
        		{
        			subject.m_buff_list.get(i).stack++;
        		}
        		break;
        	}
        }
        if (!has_same)
        {
        	BuffInfo new_buff = new BuffInfo();
        	new_buff.id = buff_id;
        	new_buff.stack = 1;
        	new_buff.lv = cmd.m_action_args.get(1);
        	subject.m_buff_list.add(new_buff);
        	
        	//眩晕，需要清空身上的搜索和攻击指令
        	if (buff_config.getSpecialTp() == BuffSpType.Dizzy)
        	{
        		subject.m_current_target = -1;
        		List<BattleCommand> cmd_list = new ArrayList<BattleCommand>();
        		for (BattleCommand c : subject.m_command_list)
        		{
        			if (c.m_action_type != BattleCommandType.Search
        					&& c.m_action_type != BattleCommandType.Attack)
        			{
        				cmd_list.add(c);
        			}
        		}
        		subject.m_command_list = cmd_list;
        	}
        }
	}
	
	private static void ProcessAttackCmd(ActorLogic subject, BattleCommand cmd, int now, List<BattleCommand> old_cmd, BattleSimulator sim)
	{
        if (subject.HasBuffSpType(BuffSpType.Dizzy))
            return;
		boolean is_use_skill = subject.m_current_rage >= ConfigConstant.tConf.getRageMax();
		if (is_use_skill && subject.HasBuffSpType(BuffSpType.Silent))
		{
			is_use_skill = false;
		}
    	Skill skill = ConfigConstant.tSkill.get(is_use_skill ? subject.m_skill_id : subject.m_base_id);
    	if (skill == null)
    		return;
    	ActorLogic main_target = sim.GetActor(cmd.m_action_args.get(0));
    	if (!main_target.m_is_alive)
    	{
    		SubjectAddNextSearch(subject, cmd, now, sim);
    		return;
    	}
    	cmd.m_action_args.add(skill.getID());
    	
    	//主语怒气
    	int delta_rage = 0;
    	if (is_use_skill)
    	{
    		delta_rage = -subject.m_current_rage;
    		subject.m_current_rage = 0;
    	}
    	else
    	{
    		delta_rage = subject.GetFinalAttr(RoleAttrType.En_Recover, skill, 1); //普通攻击永远是1级
            subject.m_current_rage += delta_rage;
            subject.m_current_rage = Math.min(subject.m_current_rage, ConfigConstant.tConf.getRageMax());
    	}
    	cmd.m_action_args.add(delta_rage);
    	
    	//吸血
    	int vampire_rate = subject.GetFinalAttr(RoleAttrType.Vampire, skill, 1); //跟技能和等级无关
    	//=================董卓技能
    	if (skill.getID() == 1018)
    	{
    		vampire_rate += (subject.m_r_inf.skill_lv - 1) * skill.getValueLv() + skill.getValue();
    	}
    	//=================董卓技能
    	
    	cmd.m_action_args.add(0); //先加0进去，等算完伤害再改
    	
        //查找击中目标
        List<ActorLogic> hit_targets = new ArrayList<ActorLogic>();
        switch (skill.getTargeTp())
        {
        case SkillTarget.Self:
        	hit_targets.add(subject);
        	break;
        case SkillTarget.RandomEnemy:
        	int random_count = skill.getTargetRate();
        	List<ActorLogic> alive = new ArrayList<ActorLogic>();
        	for ( ActorLogic a : sim.m_all_actors_logic.values())
        	{
        		if (!a.m_is_alive || a.m_side == subject.m_side )
                        continue;
        		alive.add(a);
        	}
        	if (random_count > alive.size())
        	{
        		random_count = alive.size();
        	}
        	for (int i = 0; i < random_count; ++i)
        	{
        		int index = (int)(Math.random() * (alive.size() - i));
        		hit_targets.add(alive.get(index));
        		alive.set(index, alive.get(alive.size() - i - 1));
        	}
        	break;
        case SkillTarget.AllEnemy:
        	for ( ActorLogic a : sim.m_all_actors_logic.values())
        	{
        		if (!a.m_is_alive || a.m_side == subject.m_side)
                    continue;
                hit_targets.add(a);
        	}
        	break;
        case SkillTarget.AllFriendly:
        	for ( ActorLogic a : sim.m_all_actors_logic.values())
        	{
        		if (!a.m_is_alive || a.m_side != subject.m_side)
                    continue;
                hit_targets.add(a);
        	}
        	break;
        case SkillTarget.LowHpEnemy:
        	int low_count = skill.getTargetRate();
        	List<ActorLogic> live = new ArrayList<ActorLogic>();
        	for ( ActorLogic a : sim.m_all_actors_logic.values())
        	{
        		if (!a.m_is_alive || a.m_side == subject.m_side )
                        continue;
        		live.add(a);
        	}
        	if (low_count > live.size())
        	{
        		for ( ActorLogic a : live)
        		{
        			hit_targets.add(a);
        		}
        	}
        	else
        	{
        		live.sort(new ActorLogicHpAscComp());
        		for (int i = 0; i < low_count; ++i)
        		{
        			hit_targets.add(live.get(i));
        		}
        	}
        	break;
        case SkillTarget.Special:
        	int sp_id = sim.SearchTargetGamingId(subject.m_pos, subject.m_side, true);
        	if (sp_id != -1)
        	{
        		main_target = sim.GetActor(sp_id);
        		hit_targets.add(main_target);
        		subject.m_current_target = sp_id;
        	}
        	break;
        case SkillTarget.LowHpSelf:
        	int self_low_count = skill.getTargetRate();
        	List<ActorLogic> self_live = new ArrayList<ActorLogic>();
        	for ( ActorLogic a : sim.m_all_actors_logic.values())
        	{
        		if (!a.m_is_alive || a.m_side != subject.m_side )
                        continue;
        		self_live.add(a);
        	}
        	if (self_low_count > self_live.size())
        	{
        		for ( ActorLogic a : self_live)
        		{
        			hit_targets.add(a);
        		}
        	}
        	else
        	{
        		self_live.sort(new ActorLogicHpAscComp());
        		for (int i = 0; i < self_low_count; ++i)
        		{
        			hit_targets.add(self_live.get(i));
        		}
        	}
        	break;
        case SkillTarget.ZhaoyunSkill:
        	///==========赵云技能
        	hit_targets.clear();
        	List<ActorLogic> zy_skill = new ArrayList<ActorLogic>();
        	for ( ActorLogic a : sim.m_all_actors_logic.values())
        	{
        		if (a.m_is_alive && a.m_current_hp > 0 && a.m_side != subject.m_side)
        		{
        			zy_skill.add(a);
        		}
        	}
        	if (zy_skill.size() > 0)
        	{
        		for (int i = 0; i < skill.getValue(); ++i)
            	{
        			int ix = (int)(zy_skill.size() * Math.random());
        			hit_targets.add(zy_skill.get(ix));
            	}
        	}
        	///==========赵云技能
        case SkillTarget.SingleEnemy:
        default:
        	hit_targets.add(main_target);
        	break;
        }
        
        //自我加buf
        for (int i = 0; i < skill.getBuffSelf().length; ++i)
        {
			Buff b = ConfigConstant.tBuff.get(skill.getBuffSelf()[i]);
			if (b == null)
				continue;
			if (b.getTime() <= 0)
				continue;
        	BattleCommand add_cmd = new BattleCommand();
        	add_cmd.m_step = now + 1;
        	add_cmd.m_action_type = BattleCommandType.AddBuff;
        	add_cmd.m_action_args.add(b.getID());
        	add_cmd.m_action_args.add(is_use_skill ? subject.m_r_inf.skill_lv : subject.m_r_inf.base_lv);
        	subject.m_command_list.add(add_cmd);
        	
        	//检查是否是持续伤害
        	if (b.getTickDamage() != 0)
        	{
        		int hit_count = b.getTime() / b.getTickTime();
        		if (hit_count > 0)
        		{
        			BattleCommand hit_cmd = null;
        			for (BattleCommand old : subject.m_command_list)
        			{
        				if (old.m_action_type == BattleCommandType.BuffHit
        						&& old.m_action_args.get(0) == b.getID())
        				{
        					hit_cmd = old;
        					hit_cmd.m_step = now + 1 + b.getTickTime();
        					//注意，是替换次数
                        	hit_cmd.m_action_args.set(1, hit_count);
        					break;
        				}
        			}
        			if (hit_cmd == null)
        			{
        				hit_cmd = new BattleCommand();
                    	hit_cmd.m_step = now + 1 + b.getTickTime();
                    	hit_cmd.m_action_type = BattleCommandType.BuffHit;
                    	hit_cmd.m_action_args.add(b.getID());
                    	hit_cmd.m_action_args.add(hit_count);
                    	subject.m_command_list.add(hit_cmd);
        			}
        		}
        	}
        }
        
        //自我队伍加buff
        List<ActorLogic> self_live = new ArrayList<ActorLogic>();
    	for ( ActorLogic a : sim.m_all_actors_logic.values())
    	{
    		if (!a.m_is_alive || a.m_side != subject.m_side )
                    continue;
    		self_live.add(a);
    	}
        for (int i = 0; i < skill.getBuffAll().length; ++i)
        {
			Buff b = ConfigConstant.tBuff.get(skill.getBuffAll()[i]);
			if (b == null)
				continue;
			if (b.getTime() <= 0)
				continue;
			for ( ActorLogic a : self_live){
				BattleCommand add_cmd = new BattleCommand();
	        	add_cmd.m_step = now + 1;
	        	add_cmd.m_action_type = BattleCommandType.AddBuff;
	        	add_cmd.m_action_args.add(b.getID());
	        	add_cmd.m_action_args.add(is_use_skill ? subject.m_r_inf.skill_lv : subject.m_r_inf.base_lv);
	        	a.m_command_list.add(add_cmd);
	        	
	        	//检查是否是持续伤害
	        	if (b.getTickDamage() != 0)
	        	{
	        		int hit_count = b.getTime() / b.getTickTime();
	        		if (hit_count > 0)
	        		{
	        			BattleCommand hit_cmd = null;
	        			for (BattleCommand old : a.m_command_list)
	        			{
	        				if (old.m_action_type == BattleCommandType.BuffHit
	        						&& old.m_action_args.get(0) == b.getID())
	        				{
	        					hit_cmd = old;
	        					hit_cmd.m_step = now + 1 + b.getTickTime();
	        					//注意，是替换次数
	                        	hit_cmd.m_action_args.set(1, hit_count);
	        					break;
	        				}
	        			}
	        			if (hit_cmd == null)
	        			{
	        				hit_cmd = new BattleCommand();
	                    	hit_cmd.m_step = now + 1 + b.getTickTime();
	                    	hit_cmd.m_action_type = BattleCommandType.BuffHit;
	                    	hit_cmd.m_action_args.add(b.getID());
	                    	hit_cmd.m_action_args.add(hit_count);
	                    	a.m_command_list.add(hit_cmd);
	        			}
	        		}
	        	}
			}
		}
        
        //追击
        int combo = subject.GetFinalAttr(RoleAttrType.Combo, skill, 1);//跟技能和等级无关
        //===============曹操技能=====
    	if (skill.getID() == 1024)
    	{
    		combo += (subject.m_r_inf.skill_lv - 1) * skill.getValueLv() + skill.getValue();
    	}
    	//===============曹操技能=====
        
        int combo_count = 0;
        int rate_count = ConfigConstant.tConf.getComboRate().length;
        double combo_rate = Math.random();
        for (int i = 0; i < rate_count; ++i)
        {
        	if (combo_rate < ((double)combo/ConfigConstant.tConf.getComboRate()[i]))
        	{
        		combo_count = rate_count - i;
        		break;
        	}
        }
        
        int combo_dmg = (int)(subject.m_r_inf.atk * 0.05)  + subject.GetFinalAttr(RoleAttrType.Combo_Dam, skill, 1);//跟技能和等级无关
        cmd.m_action_args.add(combo_count);
        cmd.m_action_args.add(combo_dmg);
        
        //计算伤害
        int all_dmg = 0;
        for (ActorLogic t : hit_targets)
        {
        	//=======赵云技能
        	if (skill.getTargeTp() == SkillTarget.ZhaoyunSkill)
        	{
        		if (t.m_current_hp <= 0)
        		{
        			for ( ActorLogic a : sim.m_all_actors_logic.values())
                	{
                		if (a.m_is_alive && a.m_current_hp > 0 && a.m_side != subject.m_side)
                		{
                			t = a;
                			break;
                		}
                	}
        		}
        		if (t.m_current_hp <= 0)
        			continue;
        	}
        	//=========
        	int actual_dmg = 0;
        	int target_hp = t.m_current_hp;
        	DmgResult dmg_re = CalcDmg(subject, t, skill, is_use_skill);
        	all_dmg += dmg_re.hp;
        	t.m_current_hp -= dmg_re.hp; //加血的话hp是负数
        	actual_dmg += dmg_re.hp;
            t.m_current_hp -= combo_count * combo_dmg; //追击
            actual_dmg += combo_count * combo_dmg;
            cmd.m_action_args.add(t.m_gaming_id);
            cmd.m_action_args.add(dmg_re.hp + combo_count * combo_dmg);
            cmd.m_action_args.add(dmg_re.is_crit);
     
            int t_delta_rage = t.GetFinalAttr(RoleAttrType.En_Hit, skill, 1) + skill.getSpAdd();
            t.m_current_rage += t_delta_rage;
            cmd.m_action_args.add(t_delta_rage);
            t.m_current_rage = Math.min(t.m_current_rage, ConfigConstant.tConf.getRageMax()); 
            
            
            if (t.m_current_hp <= 0)
            {
            	subject.t_dmg += target_hp;
            	SomeOneDying(t, now, sim);
                /*t.m_current_hp = 0;
                BattleCommand die_cmd = new BattleCommand();
                die_cmd.m_step = now + 1;
                die_cmd.m_action_type = BattleCommandType.Die;
                t.m_command_list.add(die_cmd);

                //他死了，则换所有正在打他的人的目标
                List<ActorLogic> old_enemy = sim.GetActorsAllEnemey(t.m_gaming_id);
                if (old_enemy != null && old_enemy.size() > 0)
                {
                	for (ActorLogic a : old_enemy)
                	{
                		int new_target = sim.SearchTargetGamingId(a.m_pos, a.m_side);
                        {
                            if (new_target != -1)
                            {
                            	BattleCommand new_seach_cmd = new BattleCommand();
                                new_seach_cmd.m_step = now + 1;
                                new_seach_cmd.m_action_type = BattleCommandType.Search;
                                new_seach_cmd.m_action_args.add(new_target);
                                a.m_command_list.add(new_seach_cmd);
                            }
                        }
                	}
                }*/                                
            }
            else
            {
            	if (actual_dmg > 0)
            		subject.t_dmg += actual_dmg;
                //对于还活着的人，状态命令的插入
            	CalcAddBuff(subject, t, skill, is_use_skill, now);
            }
            
            //修正吸血
            if (all_dmg > 0 && vampire_rate > 0 && !subject.HasBuffSpType(BuffSpType.CantHeal))
            {
            	int delta_v = (int)(Math.floor(all_dmg* (vampire_rate / 1000.0)));
            	subject.m_current_hp += delta_v;
            	subject.m_current_hp = Math.min(subject.m_current_hp, subject.m_r_inf.max_hp); 
            	if (cmd.m_action_args.get(3) == null) {
            		cmd.m_action_args.set(3, 0);
            	}
            	cmd.m_action_args.set(3, cmd.m_action_args.get(3) + delta_v);
            }
        }
                                               
        sim.Write(subject.m_gaming_id, cmd);

        //直接目标没死，继续打它
        if (main_target.m_current_hp > 0)
        {
            SubjectAddNextAtk(subject, main_target, now);
        }
	}
	
	private static void SomeOneDying(ActorLogic t, int now, BattleSimulator sim)
	{
		t.m_current_hp = 0;
        BattleCommand die_cmd = new BattleCommand();
        die_cmd.m_step = now + 1;
        die_cmd.m_action_type = BattleCommandType.Die;
        t.d_t = now + 1;
        t.m_command_list.add(die_cmd);

        //他死了，则换所有正在打他的人的目标
        List<ActorLogic> old_enemy = sim.GetActorsAllEnemey(t.m_gaming_id);
        if (old_enemy != null && old_enemy.size() > 0)
        {
        	for (ActorLogic a : old_enemy)
        	{
        		FindNewTarget(a, now, sim);
        		/*int new_target = sim.SearchTargetGamingId(a.m_pos, a.m_side);
                {
                    if (new_target != -1)
                    {
                    	BattleCommand new_seach_cmd = new BattleCommand();
                        new_seach_cmd.m_step = now + 1;
                        new_seach_cmd.m_action_type = BattleCommandType.Search;
                        new_seach_cmd.m_action_args.add(new_target);
                        a.m_command_list.add(new_seach_cmd);
                    }
                }*/
        	}
        }
	}
	
	private static void FindNewTarget(ActorLogic a, int now, BattleSimulator sim)
	{
		int new_target = sim.SearchTargetGamingId(a.m_pos, a.m_side, a.HasBuffSpType(BuffSpType.SpecialTarget));
		if (new_target != -1)
        {
        	BattleCommand new_seach_cmd = new BattleCommand();
            new_seach_cmd.m_step = now + 1;
            new_seach_cmd.m_action_type = BattleCommandType.Search;
            new_seach_cmd.m_action_args.add(new_target);
            a.m_command_list.add(new_seach_cmd);
        }
	}
	
	//如果是加血，返回的是负数, 如果是0， 返回 1 或者 -1
	private static DmgResult CalcDmg(ActorLogic subject, ActorLogic target, Skill skill, boolean is_skill)
	{
		DmgResult result = new DmgResult();
		int special_skill_crit = 0;
		if (skill.getID() == 1084) //吕布技能
		{
			special_skill_crit = skill.getValue() + ((is_skill ? subject.m_r_inf.skill_lv : subject.m_r_inf.base_lv) - 1) * skill.getValueLv();
		}
		int subject_crit = subject.GetFinalAttr(RoleAttrType.Crit_Rate, null, 0);
		int target_crit_res = target.GetFinalAttr(RoleAttrType.Crit_Res, null, 0);
		boolean is_crit = Math.random() < ((double)(subject_crit - target_crit_res + special_skill_crit)/ 1000.0);
		switch (skill.getDamageType())
		{
		case SkillDmgType.Fixed:
			result.hp = (int)Math.floor(skill.getDamage() + skill.getDamageLv() 
						* (is_skill ? subject.m_r_inf.skill_lv : subject.m_r_inf.base_lv));
			result.hp = result.hp < 1 ? 1 : result.hp;
			break;
		case SkillDmgType.Heal:
			if (!target.HasBuffSpType(BuffSpType.CantHeal))
			{
				result.hp = subject.GetFinalAttr(RoleAttrType.Skill_Atk, skill, is_skill ? subject.m_r_inf.skill_lv : subject.m_r_inf.base_lv);
				result.hp *= -1;
				result.hp = result.hp > -1 ? -1 : result.hp;
			}
			break;
		case SkillDmgType.Skill:
			float zui_zhong_te_ji_fang_yu = target.GetFinalAttr(RoleAttrType.Skill_Def, null, 0) - subject.GetFinalAttr(RoleAttrType.Sp_Ignore, null, 0);
			if (zui_zhong_te_ji_fang_yu < 0.0f)
				zui_zhong_te_ji_fang_yu = 0.0f;
			int target_level_f = (int)(Math.ceil((float)target.m_r_inf.lv / 10)) + 5;
			int dmg = subject.GetFinalAttr(RoleAttrType.Skill_Atk, skill, is_skill ? subject.m_r_inf.skill_lv : subject.m_r_inf.base_lv);
			float te_ji_jian_mian = zui_zhong_te_ji_fang_yu / target_level_f;
			if (te_ji_jian_mian > 950)
				te_ji_jian_mian = 950f;
			result.hp = (int)Math.floor(((float)(1000 - te_ji_jian_mian) / 1000) * dmg);
			if (is_crit)
			{
				result.is_crit = 1;
				result.hp *= subject.GetFinalAttr(RoleAttrType.Crit_Damage, skill, 0) / 1000.0;
			}
			result.hp = result.hp < 1 ? 1 : result.hp;
			break;
		case SkillDmgType.Base: 
			int subject_dmg = subject.GetFinalAttr(RoleAttrType.Atk, skill, is_skill ? subject.m_r_inf.skill_lv : subject.m_r_inf.base_lv);
			int ten_percent = (int)Math.floor(subject_dmg * 0.1);
			int delta_force = subject_dmg - target.GetFinalAttr(RoleAttrType.Def, null, 0);
			if (delta_force < ten_percent)
			{
				result.hp = ten_percent;
			}
			else
			{
				int dmg_reduce_calc = 1000 - target.GetFinalAttr(RoleAttrType.Damage_Reduce, skill, 0);
				if (dmg_reduce_calc < 10)
					dmg_reduce_calc = 10;
				result.hp = (int)(delta_force * (dmg_reduce_calc / 1000.0));
			}
			if (is_crit)
			{
				result.is_crit = 1;
				result.hp *= subject.GetFinalAttr(RoleAttrType.Crit_Damage, skill, 0) / 1000.0;
			}
			result.hp = result.hp < 1 ? 1 : result.hp;
			break;
		default:
			result.hp = 0;
		}
        if (result.hp > 0 && target.m_current_hp - result.hp < 0)
        {
            result.hp = target.m_current_hp;
        }
        else if (result.hp < 0 && target.m_current_hp - result.hp > target.m_r_inf.max_hp)
        {
            result.hp = target.m_current_hp - target.m_r_inf.max_hp;
        }
		return result;
	}
	
	private static void CalcAddBuff(ActorLogic subject, ActorLogic target, Skill skill, boolean is_skill, int now)
	{
		int def = target.GetFinalAttr(RoleAttrType.Debuff_Res, null, 0);
		int lv = is_skill ? subject.m_r_inf.skill_lv : subject.m_r_inf.base_lv;
		for (int i = 0; i < skill.getBuff().length; ++i)
		{
			double hit = (skill.getBuffHit()[i] + (lv - 1) * skill.getBuffLv()[i] - def) / 1000.0;
			if (hit < Math.random())
				continue;
			Buff b = ConfigConstant.tBuff.get(skill.getBuff()[i]);
			if (b == null)
				continue;
			if (b.getTime() <= 0)
				continue;
        	BattleCommand add_cmd = new BattleCommand();
        	add_cmd.m_step = now + 1;
        	add_cmd.m_action_type = BattleCommandType.AddBuff;
        	add_cmd.m_action_args.add(b.getID());
        	add_cmd.m_action_args.add(lv);
        	target.m_command_list.add(add_cmd);
        	
        	//检查是否是持续伤害
        	if (b.getTickDamage() != 0)
        	{
        		int hit_count = b.getTime() / b.getTickTime();
        		if (hit_count > 0)
        		{
        			BattleCommand hit_cmd = null;
        			for (BattleCommand old : target.m_command_list)
        			{
        				if (old.m_action_type == BattleCommandType.BuffHit
        						&& old.m_action_args.get(0) == b.getID())
        				{
        					hit_cmd = old;
        					hit_cmd.m_step = now + 1 + b.getTickTime();
        					//注意，是替换次数
                        	hit_cmd.m_action_args.set(1, hit_count);
                        	hit_cmd.a_id = subject.m_gaming_id;
        					break;
        				}
        			}
        			if (hit_cmd == null)
        			{
        				hit_cmd = new BattleCommand();
                    	hit_cmd.m_step = now + 1 + b.getTickTime();
                    	hit_cmd.m_action_type = BattleCommandType.BuffHit;
                    	hit_cmd.m_action_args.add(b.getID());
                    	hit_cmd.m_action_args.add(hit_count);
                    	hit_cmd.a_id = subject.m_gaming_id;
                    	target.m_command_list.add(hit_cmd);
        			}
        		}
        	}
		}
	}
}

class ActorLogicHpAscComp implements Comparator<ActorLogic>
{
	public int compare(ActorLogic arg0, ActorLogic arg1) 
	{
		if (arg0 == null && arg1 != null)
			return -1;
		if (arg0 != null && arg1 == null)
			return 1;
		if (arg0 == null && arg1 == null)
			return 0;
		
		float h0 = (float)arg0.m_current_hp / arg0.m_r_inf.max_hp;
		float h1 = (float)arg1.m_current_hp / arg1.m_r_inf.max_hp;
		if (h0 < h1)
			return -1;
		if (h0 > h1)
			return 1;	
		if (arg0.m_pos < arg1.m_pos)
			return -1;
		if (arg0.m_pos > arg1.m_pos)
			return 1;
		return 0;
	}
}
