package com.ssmGame.module.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Awaken;
import com.ssmData.config.entity.Backup;
import com.ssmData.config.entity.Bond;
import com.ssmData.config.entity.Breach;
import com.ssmData.config.entity.Enchant;
import com.ssmData.config.entity.Equip;
import com.ssmData.config.entity.Jewelry;
import com.ssmData.config.entity.Jewelryup;
import com.ssmData.config.entity.Role;
import com.ssmData.config.entity.Skill;
import com.ssmData.config.entity.Suit;
import com.ssmData.config.entity.Talent;
import com.ssmData.config.entity.Tasklv;
import com.ssmData.config.entity.Tech;
import com.ssmData.dbase.EquipInfo;
import com.ssmData.dbase.GuildDB;
import com.ssmData.dbase.GuildInfo;
import com.ssmData.dbase.JewelryInfo;
import com.ssmData.dbase.PlayerGuildDB;
import com.ssmData.dbase.PlayerGuildInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.RoleInfo;
import com.ssmData.dbase.enums.EquipPos;
import com.ssmGame.module.player.PlayerImpl;

public class RoleAttrCalc {
	
	private static final Logger log = LoggerFactory.getLogger(RoleAttrCalc.class);
	
	public static int Hp(RoleInfo r)
	{
		Role c = ConfigConstant.tRole.get(r.role_id);
		if (c != null)
			return c.getHp();
		return 0;
	}
	
	public static int BaseAtk(RoleInfo r)
	{
		Role c = ConfigConstant.tRole.get(r.role_id);
		if (c != null)
			return c.getAtk();
		return 0;
	}
	
	public static int FinalAtkWithoutBuff(RoleInfo r)
	{
		int result = BaseAtk(r);
		Role c = ConfigConstant.tRole.get(r.role_id);
		Skill s= ConfigConstant.tSkill.get(c.getBase());
		if (c != null && s != null)
		{
			int ji_neng_bei_lv = s.getDamageRate() + s.getDamageRateLv() * (r.base_lv - 1);
			int ji_neng_jia_cheng = s.getDamage() + s.getDamageLv() * (r.base_lv - 1);
			result = (int)Math.floor(result * (ji_neng_bei_lv / 1000.0) + ji_neng_jia_cheng);
		}
		return result;
	}
	
	public static int BaseDef(RoleInfo r)
	{
		Role c = ConfigConstant.tRole.get(r.role_id);
		if (c != null)
			return c.getDef();
		return 0;
	}
	
	public static int BaseSkillAtk(RoleInfo r)
	{
		Role c = ConfigConstant.tRole.get(r.role_id);
		if (c != null)
			return c.getAtkSp();
		return 0;
	}
	
	public static int FinalSkillAtkWithoutBuff(RoleInfo r)
	{
		int result = BaseSkillAtk(r);
		Role c = ConfigConstant.tRole.get(r.role_id);
		Skill s= ConfigConstant.tSkill.get(c.getSkill());
		if (c != null && s != null)
		{
			int ji_neng_bei_lv = s.getDamageRate() + s.getDamageRateLv() * (r.skill_lv - 1);
			int ji_neng_jia_cheng = s.getDamage() + s.getDamageLv() * (r.skill_lv - 1);
			result = (int)Math.floor(result * (ji_neng_bei_lv / 1000.0) + ji_neng_jia_cheng);
		}
		return result;
	}
	
	public static int BaseSkillDef(RoleInfo r)
	{
		Role c = ConfigConstant.tRole.get(r.role_id);
		if (c != null)
			return c.getDefSp();
		return 0;
	}
	
	public static void RefreshRoleAttr(int role_id, PlayerRolesInfo info)
	{
		//log.info("========开始计算角色数据 id {}", role_id);
		//String[] name_b = {"血", "物攻", "物防", "技攻", "技防"};
		//String[] name_s = {"速", "暴击率", "爆伤", "伤害减免", "吸血", "连击", "穿透", "buff抗性", "初始怒","怒回", "怒击", "连击伤害"};
		if (info.tech_ids == null) {
			info.tech_ids = new ArrayList<Integer>();
			info.tech_lvs = new ArrayList<Integer>();
		}
		RoleInfo r_info = info.GetRole(role_id);
		if (null == r_info)
		{
			log.debug("RefreshRoleAttr Info Error role_id {}", role_id);
			return;
		}
		
		Role r_config = ConfigConstant.tRole.get(role_id);
		if (null == r_config)
		{
			log.debug("RefreshRoleAttr Config Error role_id {}", role_id);
			return;
		}
		
		Awaken r_awaken = ConfigConstant.tAwaken.get(r_info.awaken);
		if (null == r_awaken)
		{
			log.debug("RefreshRoleAttr Awaken Error role_id {} awaken{} ", role_id, r_info.awaken);
			return;
		}
		
		//突破加成
		List<Breach> breach_list = new ArrayList<Breach>();
		for (int i = 0; i < r_info.breach; ++i)
		{
			Breach breach_config = ConfigConstant.tBreach.get(r_config.getBreachId()[i]);
			if (breach_config == null)
			{
				log.debug("RefreshRoleAttr Breach Error role_id {} awaken{} ", role_id, r_info.awaken);
				return;
			}
			breach_list.add(breach_config);
		}
		int breach_rate = 0;
		Map<Integer, Integer> breach_attr_list = new HashMap<Integer, Integer>();
		for (Breach b : breach_list)
		{
			breach_rate += b.getBreachRate();
			for (int i = 0; i < b.getBreachAtt().length; ++i)
			{
				int id = b.getBreachAtt()[i];
				if (!breach_attr_list.containsKey(id))
				{
					breach_attr_list.put(id, 0);
				}
				breach_attr_list.put(id, breach_attr_list.get(id) + b.getBreachAttvalue()[i]);
			}
		}
		
		/*for (Entry<Integer, Integer> i : breach_attr_list.entrySet())
		{
			log.info("突破加成    属性 {} 计算 {}", i.getKey(), i.getValue());
		}*/
		
		//工会科技加成
		Map<Integer, Integer> tech_attr_list = new HashMap<Integer, Integer>();
		
		//是否上阵
		Map<Integer, Integer> bond_attr_list = new HashMap<Integer, Integer>();
		Map<Integer, Integer> backup_attr_list = new HashMap<Integer, Integer>();
		boolean is_hero = info.pve_team.containsValue(role_id);
		if(is_hero)
		{
			//工会科技加成
			PlayerGuildDB pg_db = SpringContextUtil.getBean(PlayerGuildDB.class);
			PlayerGuildInfo pg_info = pg_db.loadByUid(info.player_info_id);
			if (pg_info != null && !pg_info.gd_id.isEmpty()){
				GuildDB g_db = SpringContextUtil.getBean(GuildDB.class);
				GuildInfo g_info = g_db.loadByGuildId(pg_info.gd_id);
				if (g_info != null) {
					for (int i = 0; i < info.tech_ids.size(); ++i) {
						int t_id = info.tech_ids.get(i);
						int t_lv = info.tech_lvs.get(i);
						if (t_lv == 0) {
							continue;
						}
						for (int j = 0; j < g_info.tech_ids.size(); ++j) {
							if (g_info.tech_ids.get(j).equals(t_id) && g_info.tech_lvs.get(j) > 0) {
								Tech t_cfg = ConfigConstant.tTech.get(t_id);
								if (t_cfg == null) {
									log.error("no tech cfg {} uid {}", t_id, info.player_info_id);
									continue;
								}
								for (int k = 0; k < t_cfg.getTech().length; ++k) {
									int a_id = t_cfg.getTech()[k];
									int a_v = t_cfg.getTechValue()[k];
									float a_up = t_cfg.getTechUp()[k];
									int add = (int) (a_v + (t_lv - 1) * a_up);
									tech_attr_list.put(a_id, tech_attr_list.getOrDefault(a_id, 0) + add);
								}
								break;
							}
						}
					}
				}
			}
			
			
			//羁绊加成
			List<Integer> bond_ids = info.ActiveBonds(role_id);
			for (int b_id : bond_ids)
			{
				Bond bond_config = ConfigConstant.tBond.get(b_id);
				if (bond_config == null)
				{
					log.debug("RefreshRoleAttr Bond Error no BondConfig  {}", b_id);
					continue;
				}
				
				for (int i = 0; i < bond_config.getBondAtt().length; ++i)
				{
					int attr_id = bond_config.getBondAtt()[i];
					if (!bond_attr_list.containsKey(attr_id))
					{
						bond_attr_list.put(attr_id, 0);
					}
					bond_attr_list.put(attr_id, bond_attr_list.get(attr_id) + bond_config.getBondAttvalue()[i]);
				}
			}
			
			/*for (Entry<Integer, Integer> i : bond_attr_list.entrySet())
			{
				log.info("羁绊加成    属性 {} 计算 {}", i.getKey(), i.getValue());
			}*/
			
			//副将加成
			Map<Integer, Integer> backup = info.GetSomeoneBackup(role_id);
			if (backup != null)
			{
				for (Entry<Integer, Integer> b : backup.entrySet())
				{
					RoleInfo b_info = info.GetRole(b.getValue());
					if (null == b_info)
					{
						log.debug("RefreshRoleAttr Backup Error no RoleInfo  {}", b.getValue());
						continue;
					}
					Role b_config = ConfigConstant.tRole.get(b_info.role_id);
					if (null == b_config)
					{
						log.debug("RefreshRoleAttr Backup Error no RoleConfig  {}", b_info.role_id);
						continue;
					}
					//log.info("拥有副将 {}", b.getValue());
					for (int i = 0; i < b_config.getBackupForce().length; ++i)
					{
						//log.info("副将 {} 检测战力 {} 是否达到 {}", b.getValue(), b_info.fighting, b_config.getBackupForce()[i]);
						if (b_info.fighting >= b_config.getBackupForce()[i])
						{
							//log.info("达到！");
							Backup backup_config = ConfigConstant.tBackup.get(b_config.getBackupId()[i]);
							if (null == backup_config)
							{
								log.debug("RefreshRoleAttr Backup Error no BackupConfig {} role_id {}", b_config.getBackupId()[i], b_config.getID());
								continue;
							}
							for (int attr_i = 0; attr_i < backup_config.getBackupAtt().length; ++attr_i)
							{
								int attr_id = backup_config.getBackupAtt()[attr_i];
								if (!backup_attr_list.containsKey(attr_id))
								{
									backup_attr_list.put(attr_id, 0);
								}
								//log.info("属性 {} 计算 {}", attr_id, backup_config.getBackupAttvalue()[attr_i]);
								backup_attr_list.put(attr_id, backup_attr_list.get(attr_id) + backup_config.getBackupAttvalue()[attr_i]);
							}
						}
					}
				}
				
				/*for (Entry<Integer, Integer> i : backup_attr_list.entrySet())
				{
					log.info("副将加成    属性 {} 计算 {}", i.getKey(), i.getValue());
				}*/
			}
		}
		
		//装备加成
		Map<Integer, Integer> equip_attr_list = new HashMap<Integer, Integer>();
		Map<Integer, Integer> suit_count = new HashMap<Integer, Integer>(); 
		for (int i = EquipPos.Weapon; i <= EquipPos.Shoe; ++i)
		{
			EquipInfo e_info = r_info.GetEquipByPos(i);
			
			//装备附魔
			for (int en_cnt = 0; en_cnt < e_info.eht_ids.size(); en_cnt++)
			{
				int lv = e_info.eht_lvs.get(en_cnt);
				if (lv <= 0)
					continue;
				int e_id = e_info.eht_ids.get(en_cnt);
				Enchant eh_cfg = ConfigConstant.tEnchant.get(e_id);
				if (null == eh_cfg)
					continue;
				int id = eh_cfg.getEnchantBasics();
				if (!equip_attr_list.containsKey(id))
				{
					equip_attr_list.put(id, 0);
				}
				//附魔属性 = 基础附魔值 + （附魔等级 - 1）* 附魔属性成长率
				int zhuang_bei_fu_mo = (int)Math.floor(eh_cfg.getEnchantValue() + (lv - 1)*eh_cfg.getEnchantUp());
				equip_attr_list.put(id, equip_attr_list.get(id) + zhuang_bei_fu_mo);
			}
			
			if (e_info.equip_id == 0)
				continue;
			Equip e_cfg = ConfigConstant.tEquip.get(e_info.equip_id);
			if (null == e_cfg)
			{
				log.info("装备 ID 没找到  {}", e_info.equip_id);
				continue;
			}
			if (!suit_count.containsKey(e_cfg.getSuitID()))
			{
				suit_count.put(e_cfg.getSuitID(), 1);
			}
			else
			{
				suit_count.put(e_cfg.getSuitID(), suit_count.get(e_cfg.getSuitID()) + 1);
			}
			
			for (int j = 0; j < e_cfg.getBasics().length; ++j)
			{
				int id = e_cfg.getBasics()[j];
				if (!equip_attr_list.containsKey(id))
				{
					equip_attr_list.put(id, 0);
				}
				//装备基础属性 = 装备基础值 + 强化等级 * 基础属性成长率
				int zhuang_bei_ji_chu = (int)Math.floor(e_cfg.getBasicsValue()[j] + e_info.stg_lv * e_cfg.getBasicsUp()[j]);
				equip_attr_list.put(id, equip_attr_list.get(id) + zhuang_bei_ji_chu);
			}
			
			if (e_info.rfn_lv > 0)
			{
				for (int j = 0; j < e_cfg.getRefine().length; ++j)
				{
					int id = e_cfg.getRefine()[j];
					if (!equip_attr_list.containsKey(id))
					{
						equip_attr_list.put(id, 0);
					}
					//装备精炼属性 = 装备精炼值 + (精炼等级 - 1) * 精炼属性成长率
					int zhuang_bei_jing_lian = (int)Math.floor(e_cfg.getRefineValue()[j] + (e_info.rfn_lv - 1) * e_cfg.getRefineUp()[j]);
					equip_attr_list.put(id, equip_attr_list.get(id) + zhuang_bei_jing_lian);
				}
			}
		}
		for (Entry<Integer, Integer> i : suit_count.entrySet())
		{
			if (i.getValue() < 2)
				continue;
			Suit s_cfg = ConfigConstant.tSuit.get(i.getKey());
			if (null == s_cfg)
			{
				log.info("装备套装 ID 没找到  {}", i.getKey());
				continue;
			}
			if (i.getValue() >= 2)
			{
				for (int j = 0; j < s_cfg.getSuitAtt1().length; ++j)
				{
					int id = s_cfg.getSuitAtt1()[j];
					if (!equip_attr_list.containsKey(id))
					{
						equip_attr_list.put(id, 0);
					}
					equip_attr_list.put(id, equip_attr_list.get(id) + s_cfg.getSuitAttvalue1()[j]);
				}
			}
			if (i.getValue() >= 3)
			{
				for (int j = 0; j < s_cfg.getSuitAtt2().length; ++j)
				{
					int id = s_cfg.getSuitAtt2()[j];
					if (!equip_attr_list.containsKey(id))
					{
						equip_attr_list.put(id, 0);
					}
					equip_attr_list.put(id, equip_attr_list.get(id) + s_cfg.getSuitAttvalue2()[j]);
				}
			}
			if (i.getValue() >= 4)
			{
				for (int j = 0; j < s_cfg.getSuitAtt3().length; ++j)
				{
					int id = s_cfg.getSuitAtt3()[j];
					if (!equip_attr_list.containsKey(id))
					{
						equip_attr_list.put(id, 0);
					}
					equip_attr_list.put(id, equip_attr_list.get(id) + s_cfg.getSuitAttvalue3()[j]);
				}
			}
		}
		/*for (Entry<Integer, Integer> i : equip_attr_list.entrySet())
		{
			log.info("装备加成    属性 {} 计算 {}", i.getKey(), i.getValue());
		}*/
		
		//饰品加成
		Map<Integer, Integer> jew_attr_list = new HashMap<Integer, Integer>();
		if (r_info.jewelries != null) {
			int js = r_config.getJewelryId().length;
			for (int i = 0; i < js; ++i) {
				Jewelry j_cfg = ConfigConstant.tJewelry.get(r_config.getJewelryId()[i]);
				if (j_cfg == null) {
					continue;
				}
				JewelryInfo j_i = null;
				for (int j = 0; j < r_info.jewelries.size(); ++j) {
					if (r_info.jewelries.get(j).idx == i) {
						j_i = r_info.jewelries.get(j);
						break;
					}
				}
				if (j_i == null) {
					continue;
				}
				if (j_i.evo_lv >= j_cfg.getAdvanced().length) {
					continue;
				}
				Jewelryup jup_cfg = ConfigConstant.tJewelryup.get(j_cfg.getAdvanced()[j_i.evo_lv]);
				if (jup_cfg == null) {
					continue;
				}
				for (int j = 0; j < j_cfg.getBasics().length; ++j) {
					int id = j_cfg.getBasics()[j];
					int basic = jup_cfg.getBasicsValue()[j];
					float basic_up = jup_cfg.getBasicsUp()[j];
					if (!jew_attr_list.containsKey(id)) {
						jew_attr_list.put(id, 0);
					}
					int ori = jew_attr_list.get(id);
					int diff = (int)Math.floor(basic + j_i.stg_lv * basic_up);
					jew_attr_list.put(id, ori + diff);
				}
			}
		}
		
		
		//属性初始值
		int[] basic_attr = {r_config.getHp(), r_config.getAtk(), r_config.getDef(), r_config.getAtkSp(), r_config.getDefSp()};
		int[] basic_attr_grow = {r_config.getHpGrowth(), r_config.getAtkGrowth(), r_config.getDefGrowth(), r_config.getAtkSpGrowth(), r_config.getDefSpGrowth()};
		int[] basic_attr_ids = {RoleAttrType.Max_HP, RoleAttrType.Atk, RoleAttrType.Def, RoleAttrType.Skill_Atk, RoleAttrType.Skill_Def};
		
		//觉醒加成
		//int awaken_rate = r_awaken.getAwakenRate();
		int awaken_idx = r_info.awaken - r_config.getAwakenID();
		if (awaken_idx < 0)
			awaken_idx = 0;
		if (awaken_idx >= r_config.getAwakenRate().length)
			awaken_idx = r_config.getAwakenRate().length - 1;
		int awaken_rate = r_config.getAwakenRate()[awaken_idx];
		
		//基础属性
		Talent talent_config = ConfigConstant.tTalent.get(r_info.talent);
		if (talent_config == null)
		{
			log.debug("RefreshRoleAttr Talent Error role_id {} Talent {} ", role_id, r_info.talent);
			return;
		}
		
		//魅力加成
		Tasklv mei_li_cfg = ConfigConstant.tTaskLv.get(info.m_mei_li_lv);
		int[] basic_attr_mei_li = {0, 0, 0, 0, 0};
		if (mei_li_cfg != null)
		{
			basic_attr_mei_li[0] = mei_li_cfg.getHp();
			basic_attr_mei_li[1] = mei_li_cfg.getAtk();
			basic_attr_mei_li[2] = mei_li_cfg.getDef();
			basic_attr_mei_li[3] = mei_li_cfg.getAtkSp();
			basic_attr_mei_li[4] = mei_li_cfg.getDefSp();
		}
		
		for (int i = 0; i < basic_attr.length; ++i)
		{
			int sheng_ji_shu_xing = basic_attr[i] + (int)(((r_info.lv - 1) / 1000.0) * basic_attr_grow[i] 
					* (talent_config.getTalentNum() + r_config.getTalent())
					);
			//log.info("=========== id{} 升级属性：{}", basic_attr_ids[i], sheng_ji_shu_xing);
			//log.info("=========== id{} 属性成长率：{}", basic_attr_ids[i], basic_attr_grow[i]);
			//log.info("=========== id{} 资质：{}", basic_attr_ids[i], talent_config.getTalentNum());
			//log.info("=========== id{} 属性基础：{}", basic_attr_ids[i], basic_attr[i]);
			int jue_xing_jia_cheng = awaken_rate;
			int tu_po_jia_cheng = breach_rate;
			
			int ji_ban_jia_cheng = bond_attr_list.containsKey(basic_attr_ids[i])
					? bond_attr_list.get(basic_attr_ids[i])
					: 0;
			int tian_fu_shu_xing = breach_attr_list.containsKey(basic_attr_ids[i]) 
					? breach_attr_list.get(basic_attr_ids[i])
					: 0;
			int fu_jiang_jia_cheng = backup_attr_list.containsKey(basic_attr_ids[i])
					? backup_attr_list.get(basic_attr_ids[i])
					: 0;
			int zhuang_bei_jia_cheng = equip_attr_list.containsKey(basic_attr_ids[i])
					? equip_attr_list.get(basic_attr_ids[i])
					: 0;
					
			int mei_li_jia_cheng = basic_attr_mei_li[i];
			
			int shi_pin_jia_cheng = jew_attr_list.containsKey(basic_attr_ids[i])
					? jew_attr_list.get(basic_attr_ids[i])
					: 0;
					
			int gong_hui_ke_ji = tech_attr_list.containsKey(basic_attr_ids[i])
					? tech_attr_list.get(basic_attr_ids[i])
					: 0;
			
			//旧的：最终基础属性 = 升级属性 * (觉醒加成 + 突破加成 + 羁绊加成）/1000  + 天赋属性  + 副将加成 + 装备属性加成 + 魅力加成 + 
			//最终基础属性 = 升级属性 * (1000 + 羁绊加成)/1000  + 天赋属性  + 副将加成 + 装备属性加成 + 魅力属性 + 初始值 * 觉醒加成/1000 + 初始值 * 突破加成/1000 + 饰品加成  + 工会科技
			basic_attr[i] = (int)(sheng_ji_shu_xing * ((1000.0 + ji_ban_jia_cheng) / 1000.0)
					+ tian_fu_shu_xing + fu_jiang_jia_cheng + zhuang_bei_jia_cheng + mei_li_jia_cheng + shi_pin_jia_cheng + gong_hui_ke_ji
					+ basic_attr[i] * (jue_xing_jia_cheng / 1000.0) + basic_attr[i] * (tu_po_jia_cheng / 1000.0) );
		}
		
		r_info.max_hp = basic_attr[0];
		r_info.atk = basic_attr[1];
		r_info.def = basic_attr[2];
		r_info.skill_atk = basic_attr[3];
		r_info.skill_def = basic_attr[4];		
		
		//特殊属性
		int[] sp_attr = {r_config.getAtkspeed(), r_config.getCritRate(), r_config.getCritDamage(), r_config.getDamageReduce()
				, r_config.getVampire(), r_config.getCombo(), r_config.getSpIgnore(), r_config.getDebuffRes()
				, r_config.getEn(), r_config.getEnRecover(), r_config.getEnHit(), r_config.getComboDam(), r_config.getCritRes()};
		int[] sp_attr_ids = {RoleAttrType.Atk_Speed, RoleAttrType.Crit_Rate, RoleAttrType.Crit_Damage, RoleAttrType.Damage_Reduce
				, RoleAttrType.Vampire, RoleAttrType.Combo, RoleAttrType.Sp_Ignore, RoleAttrType.Debuff_Res
				, RoleAttrType.EN, RoleAttrType.En_Recover, RoleAttrType.En_Hit, RoleAttrType.Combo_Dam, RoleAttrType.Crit_Res};
		for (int i = 0; i < sp_attr.length; ++i)
		{
			//最终特殊属性 = 属性初始值 + 天赋属性 + 羁绊加成 + 副将加成 + 装备属性加成
			int tian_fu_shu_xing = breach_attr_list.containsKey(sp_attr_ids[i]) 
					? breach_attr_list.get(sp_attr_ids[i])
					: 0;
			int ji_ban_jia_cheng = bond_attr_list.containsKey(sp_attr_ids[i])
					? bond_attr_list.get(sp_attr_ids[i])
					: 0;		
			int fu_jiang_jia_cheng = backup_attr_list.containsKey(sp_attr_ids[i])
					? backup_attr_list.get(sp_attr_ids[i])
					: 0;
			int zhuang_bei_jia_cheng = equip_attr_list.containsKey(sp_attr_ids[i])
					? equip_attr_list.get(sp_attr_ids[i])
					: 0;
					
			int shi_pin_jia_cheng = jew_attr_list.containsKey(sp_attr_ids[i])
					? jew_attr_list.get(sp_attr_ids[i])
					: 0;
				
			int gong_hui_ke_ji = tech_attr_list.containsKey(sp_attr_ids[i])
					? tech_attr_list.get(sp_attr_ids[i])
					: 0;
			
			sp_attr[i] = sp_attr[i] + tian_fu_shu_xing + ji_ban_jia_cheng + fu_jiang_jia_cheng + zhuang_bei_jia_cheng + shi_pin_jia_cheng + gong_hui_ke_ji;
		}
		
		r_info.atk_speed = sp_attr[0];
		r_info.crit_rate = sp_attr[1];
		r_info.crit_damage = sp_attr[2];
		r_info.dmg_reduce = sp_attr[3];
		r_info.vampire = sp_attr[4];
		r_info.combo = sp_attr[5];
		r_info.sp_ignore = sp_attr[6];
		r_info.debuff_res = sp_attr[7];
		r_info.en = sp_attr[8];
		r_info.en_recover = sp_attr[9];
		r_info.en_hit = sp_attr[10];
		r_info.combo_dam = sp_attr[11];
		r_info.crit_res = sp_attr[12];
		
		//战力
		r_info.fighting = r_info.CalcFighting();
		
		/*int[] old_b = {old_info.max_hp, old_info.atk, old_info.def, old_info.skill_atk, old_info.skill_def};
		for (int i = 0; i < basic_attr.length; ++i)
		{
			log.info("计算结果：基础属性 {} 数值 {} 计算前是 {} 变化 {}", name_b[i], basic_attr[i], old_b[i], old_b[i] - basic_attr[i]);
		}
		
		int[] old_s = {old_info.atk_speed, old_info.crit_rate, old_info.crit_damage, old_info.dmg_reduce
				, old_info.vampire, old_info.combo, old_info.sp_ignore, old_info.debuff_res, old_info.en
				, old_info.en_recover, old_info.en_hit, old_info.combo_dam};
		for (int i = 0; i < sp_attr.length; ++i)
		{
			log.info("计算结果：特殊属性 {} 数值 {} 计算前是 {} 变化 {}", name_s[i], sp_attr[i], old_s[i], old_s[i] - sp_attr[i]);
		}
		log.info("计算结果：战斗力 {} 计算前是 {}， 变化 {}", r_info.fighting, old_info.fighting, old_info.fighting - r_info.fighting);
		log.info("**********结束计算角色数据 id {}", role_id);*/
	}

	public static void RecalcAllInfo(PlayerRolesInfo roles, PlayerInfo info)
	{
		//刷新所有人的属性
		List<Integer> calced = new ArrayList<Integer>();
		for (Entry<Integer, Map<Integer, Integer>> fu_jiang : roles.backup_info.entrySet())
		{
			for (Entry<Integer, Integer> f : fu_jiang.getValue().entrySet())
			{
				RoleAttrCalc.RefreshRoleAttr(f.getValue(), roles);
				calced.add(f.getValue());
			}
		}
		for (Entry<Integer, Integer> zhu_jiang : roles.pve_team.entrySet())
		{
			RoleAttrCalc.RefreshRoleAttr(zhu_jiang.getValue(), roles);
			calced.add(zhu_jiang.getValue());
		}
		for (RoleInfo role : roles.roles)
		{
			if (calced.contains(role.role_id))
				continue;
			RoleAttrCalc.RefreshRoleAttr(role.role_id, roles);
		}
		PlayerImpl.UpdateTeamFightingCheckMax(info, roles.CalcTeamFighting());
	}
	
	public static boolean RecalcPveTeamInfo(PlayerRolesInfo roles, PlayerInfo info) {
		for (Entry<Integer, Integer> zhu_jiang : roles.pve_team.entrySet())
		{
			RoleAttrCalc.RefreshRoleAttr(zhu_jiang.getValue(), roles);
		}
		return PlayerImpl.UpdateTeamFightingCheckMax(info, roles.CalcTeamFighting());
	}
}
