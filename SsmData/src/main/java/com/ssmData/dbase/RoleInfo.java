package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Role;
import com.ssmData.dbase.enums.EquipPos;

/*
 * 场外单个角色数据集合，也是数据库里保存的数据
 */
public class RoleInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public int role_id;              //角色配置表id
	public int lv;			//角色等级
	public int breach;     //角色突破等级	
	public int talent;		//角色资质
	public int awaken;		//觉醒等级
	
	public int base_lv; 	//普通攻击技能等级
	public int skill_lv;   	//特技技能等级
	
	public int max_hp;		//血量上限
	public int atk;			//物理攻击
	public int def;			//物理防御
	public int skill_atk;	//技能攻击
	public int skill_def;	//技能防御
	
	public int atk_speed;	//攻击速度
	public int crit_rate;	//暴击率
	public int crit_damage;	//暴击伤害
	public int dmg_reduce;	//伤害减免
	public int vampire;		//吸血
	public int combo;		//连击
	public int sp_ignore;	//穿透
	public int debuff_res;	//buff抗性
	public int en;			//怒气
	public int en_recover;	//普通攻击怒气恢复
	public int en_hit;		//怒气受击恢复
	public int combo_dam;	//追击伤害
	public int crit_res;	//暴击抵抗
	
	public int fighting;	//战力
	
	public List<EquipInfo> equips; //装备列表
	
	public List<JewelryInfo> jewelries; //饰品列表
	
	public RoleInfo Clone()
	{
		RoleInfo new_r = new RoleInfo();
		
		new_r.role_id = role_id;
		new_r.lv = lv;
		new_r.breach = breach;
		new_r.talent = talent;
		new_r.awaken = awaken;
		
		new_r.base_lv = base_lv;
		new_r.skill_lv = skill_lv;
		
		new_r.max_hp = max_hp;
		new_r.atk = atk;
		new_r.def = def;
		new_r.skill_atk = skill_atk;
		new_r.skill_def = skill_def;
		
		new_r.atk_speed = atk_speed;
		new_r.crit_rate = crit_rate;
		new_r.crit_damage = crit_damage;
		new_r.dmg_reduce = dmg_reduce;
		new_r.vampire = vampire;
		new_r.combo = combo;
		new_r.sp_ignore = sp_ignore;
		new_r.debuff_res = debuff_res;
		new_r.en = en;
		new_r.en_recover = en_recover;
		new_r.en_hit = en_hit;
		new_r.combo_dam = combo_dam;
		new_r.crit_res = crit_res;
		
		new_r.fighting = fighting;
		
		new_r.equips = new ArrayList<EquipInfo>();
		for (EquipInfo e : this.equips)
		{
			new_r.equips.add(e.Clone());
		}
		
		if (this.jewelries != null) {
			new_r.jewelries = new ArrayList<JewelryInfo>();
			for (JewelryInfo e : this.jewelries)
			{
				new_r.jewelries.add(e.Clone());
			}
		}
		
		return new_r;
	}
	
	public void InitByRoleConfigId(int id)
	{
		Role c = ConfigConstant.tRole.get(id);
		if (c == null)
		{
			return;
		}
		role_id = id;
		lv = c.getLv();
		breach = 0;
		talent = 0;
		awaken = c.getAwakenID();
		
		base_lv = 1;
		skill_lv = 1;
		
		max_hp = c.getHp();
		atk = c.getAtk();
		def = c.getDef();
		skill_atk = c.getAtkSp();
		skill_def = c.getDefSp();
		
		atk_speed = c.getAtkspeed();
		crit_rate = c.getCritRate();
		crit_damage = c.getCritDamage();
		dmg_reduce = c.getDamageReduce();
		vampire = c.getVampire();
		combo = c.getCombo();
		sp_ignore = c.getSpIgnore();
		debuff_res = c.getDebuffRes();
		en = c.getEn();
		en_recover = c.getEnRecover();
		en_hit = c.getEnHit();
		combo_dam = c.getComboDam();
		crit_res = c.getCritRes();
		
		fighting = CalcFighting();
		
		equips = new ArrayList<EquipInfo>();
		for (int e_p = EquipPos.Weapon; e_p <= EquipPos.Shoe; ++e_p)
		{
			EquipInfo e = new EquipInfo();
			e.equip_id = 0;
			e.stg_lv = 0;
			e.rfn_lv = 0;
			e.eht_breach = 0;
			e.pos = e_p;
			e.eht_ids = new ArrayList<Integer>();
			e.eht_temp = new ArrayList<Integer>();
			e.eht_lvs = new ArrayList<Integer>();
			equips.add(e);
		}
		
		jewelries = new ArrayList<JewelryInfo>();
		for (int i = 0; i < c.getJewelryId().length; ++i) {
			JewelryInfo j = new JewelryInfo();
			j.idx = i;
			jewelries.add(j);
		}
	}

	public void InitByRoleConfigIdAndLv(int id, int lv)
	{
		InitByRoleConfigId(id);
		this.lv = lv;
		
		Role c = ConfigConstant.tRole.get(id);
		if (c == null)
		{
			return;
		}
		
        int this_awaken_idx = this.awaken - c.getAwakenID();
        if (this_awaken_idx < 0)
            this_awaken_idx = 0;
		
		int lv_arg = this.lv - 1;
		this.max_hp += (int)Math.floor(c.getTalent() * lv_arg * (float)(c.getHpGrowth()) / 1000 + max_hp * c.getAwakenRate()[this_awaken_idx] / 1000.0f);
		this.atk += (int)Math.floor(c.getTalent() * lv_arg * (float)(c.getAtkGrowth()) / 1000 + atk * c.getAwakenRate()[this_awaken_idx] / 1000.0f);
		this.def += (int)Math.floor(c.getTalent() * lv_arg * (float)(c.getDefGrowth()) / 1000 + def * c.getAwakenRate()[this_awaken_idx] / 1000.0f);
		this.skill_atk += (int)Math.floor(c.getTalent() *  lv_arg * (float)(c.getAtkSpGrowth()) / 1000 + skill_atk * c.getAwakenRate()[this_awaken_idx] / 1000.0f);
		this.skill_def += (int)Math.floor(c.getTalent() *  lv_arg * (float)(c.getDefSpGrowth()) / 1000 + skill_def * c.getAwakenRate()[this_awaken_idx] / 1000.0f);
		
		fighting = CalcFighting();
	}
	
	public int CalcFighting()
	{	//斗士战斗力 = 生命值 * 0.1 + ( 物理攻击+物理防御 ) * 0.6 + (特技攻击 + 特技防御 ) * 0.5 + （（暴击率-50）+ 暴击抵抗 +  异常抗性 + 初始怒气 - 300）*2.4 
		//+ （吸血 + 追击 + 追击伤害）*2 + （怒气回复 + 被击怒气 - 250）* 8 + （暴击伤害-1500 + 穿透）* 0.8 + 伤害减免*4
		fighting = (int)Math.floor(max_hp*0.1 + (atk+def)*0.6 + (skill_atk+skill_def)*0.5 + ((crit_rate-50)+crit_res+debuff_res+en-300)*2.4 
				+ (vampire+combo+combo_dam)*2.0 + (en_recover+en_hit-250)*8.0+ (crit_damage-1500+sp_ignore)*0.8 + dmg_reduce*4.0);
		return fighting;
	}
	
	public EquipInfo GetEquipByPos(int pos)
	{
		for (EquipInfo i : equips)
		{
			if (i.pos == pos)
				return i;
		}
		return null;
	}
	
	public EquipInfo GetEquipById(int id)
	{
		for (EquipInfo i : equips)
		{
			if (i.equip_id == id)
				return i;
		}
		return null;
	}
}
