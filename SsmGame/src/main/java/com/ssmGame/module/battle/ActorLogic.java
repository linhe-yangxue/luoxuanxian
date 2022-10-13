package com.ssmGame.module.battle;

import java.util.ArrayList;
import java.util.List;

import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Buff;
import com.ssmData.config.entity.Skill;
import com.ssmData.dbase.RoleInfo;
import com.ssmGame.module.role.RoleAttrType;

/*
 * 战斗时的即时数据集合
 */
public class ActorLogic {
	public int m_gaming_id = 0;              //对局内的唯一ID
	public int m_side = BattleSide.Null;
	public int m_pos = 0;                    //站位 ， 1到5

	public boolean m_is_alive = true;

	//场外基础计算部分=========
	/*public int m_max_hp = 0;
	public int m_atk = 0;
	public int m_def = 0;
	public int m_skill_atk = 0;
	public int m_skill_def = 0;
	
	public int m_crit_rate = 0;
	public int m_crit_damage = 0;
	public int m_damage_reduce = 0;
	public int m_vampire = 0;
	public int m_combo = 0;
	public int m_sp_ignore = 0;
	public int m_debuff_res = 0;
	public int m_atk_speed = 0;*/
	//=========
	
	
	public int m_base_id = 0;
	public int m_skill_id = 0;
	
	public RoleInfo m_r_inf = null;
	
	public int m_current_hp = 0;
	public int m_current_rage = 0;
	public int m_current_target = -1;

	public List<BattleCommand> m_command_list = new ArrayList<BattleCommand>();
	
	public List<BuffInfo> m_buff_list = new ArrayList<BuffInfo>();
	
	public Integer dps = 0; //秒伤
	public Integer t_dmg = 0; //总伤害
	public Integer d_rate = 0; //伤害百分比，精度2，要除100
	public Integer d_t = null; //死亡时间
	
    public BattleCommand HasAttackCmd(){
        for (int i = 0; i < m_command_list.size(); ++i) {
        	BattleCommand b = m_command_list.get(i);
            if (b.m_action_type == BattleCommandType.Attack) {
                return b;
            }
        }
        return null;
    }
    
    public void AddAllSkillCmdSeachTime(){
    	for (int i = 0; i < m_command_list.size(); ++i) {
    		BattleCommand b = m_command_list.get(i);
            if (b.m_action_type == BattleCommandType.Attack) {
                b.m_step += ConfigConstant.tConf.getAddspeed();
            }
        }
    }
    
    public int GetClosestCmdStep(){
        int mim_step = ConfigConstant.tConf.getBattlelimit();
        for (int i = 0; i < m_command_list.size(); ++i)
        {
        	BattleCommand c = m_command_list.get(i);
        	if (c.m_step < mim_step) {
                mim_step = c.m_step;
            }
        }
        return mim_step;
    }
    
    public boolean HasBuffSpType(int buff_sp_type)
    {
    	for (BuffInfo b : this.m_buff_list)
    	{
    		Buff c = ConfigConstant.tBuff.get(b.id);
    		if (c != null && c.getSpecialTp() == buff_sp_type)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    //不是最终攻击
    public int GetFinalAttr(int attr_type, Skill skill, int skill_lv)
    {
    	int char_value = 0;
    	int result = 0;
    	switch (attr_type)
    	{
    	case RoleAttrType.Atk:
    		char_value = m_r_inf.atk;
    		char_value += BuffCal(char_value, attr_type);
    		if (skill != null)
    		{
    			int ji_neng_bei_lv = skill.getDamageRate() + skill.getDamageRateLv() * (skill_lv - 1);
    			int ji_neng_jia_cheng = skill.getDamage() + skill.getDamageLv() * (skill_lv - 1);
    			result = (int)Math.floor(char_value * (ji_neng_bei_lv / 1000.0) + ji_neng_jia_cheng);
    		}
    		break;
    	case RoleAttrType.Skill_Atk:
    		char_value = m_r_inf.skill_atk;
    		char_value += BuffCal(char_value, attr_type);
    		if (skill != null)
    		{
    			int ji_neng_bei_lv = skill.getDamageRate() + skill.getDamageRateLv() * (skill_lv - 1);
    			int ji_neng_jia_cheng = skill.getDamage() + skill.getDamageLv() * (skill_lv - 1);
    			result = (int)Math.floor(char_value * (ji_neng_bei_lv / 1000.0) + ji_neng_jia_cheng);
    		}
    		break;
    	case RoleAttrType.Def:
    		char_value = m_r_inf.def;
    		result = char_value;
    		break;
    	case RoleAttrType.Skill_Def:
    		char_value = m_r_inf.skill_def;
    		result = char_value;
    		break;
    	case RoleAttrType.Atk_Speed:
    		char_value = m_r_inf.atk_speed;
    		result = char_value;
    		break;
    	case RoleAttrType.Crit_Rate:
    		char_value = m_r_inf.crit_rate;
    		result = char_value;
    		break;
    	case RoleAttrType.Crit_Damage:
    		char_value = m_r_inf.crit_damage;
    		result = char_value;
    		break;
    	case RoleAttrType.Damage_Reduce:
    		char_value = m_r_inf.dmg_reduce;
    		result = char_value;
    		break;
    	case RoleAttrType.Vampire:
    		char_value = m_r_inf.vampire;
    		result = char_value;
    		break;
    	case RoleAttrType.Combo:
    		char_value = m_r_inf.combo;
    		result = char_value;
    		break;
    	case RoleAttrType.Sp_Ignore:
    		char_value = m_r_inf.sp_ignore;
    		result = char_value;
    		break;
    	case RoleAttrType.Debuff_Res:
    		char_value = m_r_inf.debuff_res;
    		result = char_value;
    		break;
    	case RoleAttrType.EN:
    		char_value = m_r_inf.en;
    		result = char_value;
    		break;
    	case RoleAttrType.En_Recover:
    		char_value = m_r_inf.en_recover;
    		result = char_value;
    		break;
    	case RoleAttrType.En_Hit:
    		char_value = m_r_inf.en_hit;
    		result = char_value;
    		break;
    	case RoleAttrType.Combo_Dam:
    		char_value = m_r_inf.combo_dam;
    		result = char_value;
    		break;
    	case RoleAttrType.Crit_Res:
    		char_value = m_r_inf.crit_res;
    		result = char_value;
    		break;
    	}
    	if (attr_type != RoleAttrType.Atk && attr_type !=  RoleAttrType.Skill_Atk) {
    		result += BuffCal(char_value, attr_type);
    	}
    	return result;
    }
    
    private int BuffCal(int char_value, int attr_type)
    {
    	int result = 0;
    	boolean has_buff = false;
    	//先乘法
    	for (BuffInfo b : m_buff_list)
    	{
    		Buff c = ConfigConstant.tBuff.get(b.id);
    		if (null == c)
    			continue;
    		if (c.getCalculate() != BuffCalcType.Multiply)
    			continue;
    		for (int i = 0; i < c.getProperty().length; ++i)
    		{
    			if (c.getProperty()[i] != attr_type)
    				continue;
    			has_buff = true;
    			char_value = BuffCalc_Multiply(char_value, c.getValue()[i], b.lv, c.getLvValue()[i], b.stack);
    		}
    	}
        if (has_buff)
            result += char_value;
    	
    	//后加法
    	for (BuffInfo b : m_buff_list)
    	{
    		Buff c = ConfigConstant.tBuff.get(b.id);
    		if (null == c)
    			continue;
    		if (c.getCalculate() != BuffCalcType.Add)
    			continue;
    		for (int i = 0; i < c.getProperty().length; ++i)
    		{
    			if (c.getProperty()[i] != attr_type)
    				continue;
    			result += BuffCalc_Add(c.getValue()[i], b.lv, c.getLvValue()[i], b.stack);
    		}
    	}
    	return result;
    }
    
    //属性的乘法运算
    private int BuffCalc_Multiply(int char_value, int buff_base, int buff_lv, int buff_lv_value, int stack)
    {
    	//状态加成 = 角色属性 × （基础加成 + （状态等级-1） * 等级成长） *　状态层数  /1000
    	return (int)(char_value * ((buff_base + (buff_lv - 1) * buff_lv_value) * stack / 1000.0));
    }
    
    //属性的加法运算
    private int BuffCalc_Add(int buff_base, int buff_lv, int buff_lv_value, int stack)
    {
    	//状态加成 = （基础加成 + （状态等级-1） * 等级成长） *　状态层数 
    	return (buff_base + (buff_lv - 1) * buff_lv_value) * stack;
    }
    
    public ActorLogic Clone()
    {
    	ActorLogic result = new ActorLogic();
        result.m_current_hp = m_current_hp;
        result.m_current_rage = m_current_rage;
        result.m_gaming_id = m_gaming_id;
        result.m_is_alive = m_is_alive;
        result.m_side = m_side;
        result.m_pos = m_pos;
        result.m_base_id = m_base_id;
        result.m_skill_id = m_skill_id;
        
        /*result.m_max_hp = m_max_hp;
        result.m_atk = m_atk;
        result.m_def = m_atk;
        result.m_skill_atk = m_skill_atk;
        result.m_skill_def = m_skill_def;
        result.m_crit_rate = m_crit_rate;
        result.m_atk_speed = m_atk_speed;
        result.m_crit_damage = m_crit_damage;
        result.m_damage_reduce = m_damage_reduce;
        result.m_vampire = m_vampire;
        result.m_combo = m_combo;
        result.m_sp_ignore = m_sp_ignore;
        result.m_debuff_res = m_debuff_res;*/
        
        result.m_current_target = m_current_target;
        result.m_r_inf = m_r_inf.Clone();
        
        for (BattleCommand b : m_command_list)
        {
        	result.m_command_list.add(b.Clone());
        }
        
        for (BuffInfo b : m_buff_list)
        {
        	result.m_buff_list.add(b.Clone());
        }
        
        return result;
    }
}
