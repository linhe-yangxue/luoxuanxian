package com.ssmGame.module.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Awaken;
import com.ssmData.config.entity.Breach;
import com.ssmData.config.entity.Grade;
import com.ssmData.config.entity.Role;
import com.ssmData.config.entity.Talent;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmData.dbase.RoleInfo;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.defdata.msg.role.RoleResultMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.broadcast.BroadcastImpl;
import com.ssmGame.module.equip.EquipImpl;
import com.ssmGame.module.player.PlayerImpl;
import com.ssmGame.module.task.TaskImpl;
import com.ssmGame.module.task.TaskType;

@Service
@Scope("prototype")
public class RoleImpl {

	private static final Logger log = LoggerFactory.getLogger(RoleImpl.class);

	@Autowired
	PlayerRolesInfoDB player_roles_info_db;
	@Autowired
	PlayerInfoDB player_db;
	@Autowired
	PlayerBagDB player_bag_db;

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public final static RoleImpl getInstance() {
		return SpringContextUtil.getBean(RoleImpl.class);
	}

	public PlayerRolesInfo loadPlayerRoles(String player_id) {
		PlayerRolesInfo result = player_roles_info_db.load(player_id);
		// testInitPlayerRoles(result);
		// Destory();
		return result;
	}

	/*
	 * private void testInitPlayerRoles(PlayerRolesInfo roles) { for (RoleInfo
	 * role_info : roles.roles) { Role role_config =
	 * ConfigConstant.tRole.get(role_info.role_id); if (null == role_config)
	 * continue; role_info.star = role_config.getStar(); role_info.lv =
	 * role_config.getLv(); role_info.hp = role_config.getHp(); role_info.atk =
	 * role_config.getAtk(); role_info.def = role_config.getDef();
	 * role_info.atkSp = role_config.getAtkSp(); role_info.defSp =
	 * role_config.getDefSp(); role_info.critRate = role_config.getCritRate();
	 * role_info.critDamage = role_config.getCritDamage();
	 * role_info.damageReduce = role_config.getDamageReduce(); role_info.vampire
	 * = role_config.getVampire(); role_info.combo = role_config.getCombo();
	 * role_info.spIgnore = role_config.getSpIgnore(); role_info.debuffRes =
	 * role_config.getDebuffRes(); } player_roles_info_db.save(); }
	 */

	// private PlayerRolesInfo RefreshPlayerRole

	public void Destory() {
		player_roles_info_db = null;
		player_db = null;
		player_bag_db = null;
	}

	public CommonMsg HandleAddNewRole(CommonMsg respond, int role_id, boolean check_fragment) {
		AddNewRoleResult r = AddNewRole(respond.header.uid, role_id, check_fragment);
		respond.body.role_result = new RoleResultMsg();
		if (r.success == true) {
			respond.body.sync_bag = new SyncBagMsg();
			respond.body.sync_bag.items = new HashMap<Integer, Integer>();
			respond.body.sync_bag.items.put(r.frag_id, r.frag_count);
			respond.body.sync_roles = new PlayerRolesInfo();
			respond.body.sync_roles.roles = new ArrayList<RoleInfo>();
			respond.body.sync_roles.roles.add(r.role);
			respond.body.role_result.is_success = true;
			respond.body.sync_player_info = new SyncPlayerInfoMsg();
			respond.body.sync_player_info.is_refresh = false;
			respond.body.sync_player_info.team_current_fighting = r.p.team_current_fighting;
		} else {
			respond.body.role_result.is_success = false;
			respond.header.rt = MsgCode.HTTP_INCORRECT_PARAM; // todo
			respond.header.rt_sub = r.msg_id;
		}
		return respond;
	}

	// 添加新角色
	private AddNewRoleResult AddNewRole(String player_id, int role_id, boolean check_fragment) {
		AddNewRoleResult result = new AddNewRoleResult();

		Role config = ConfigConstant.tRole.get(role_id);
		if (null == config) {
			log.error("RoleImpl.CommonCheck() No Role Id {}", role_id);
			return result;
		}

		PlayerRolesInfo player = loadPlayerRoles(player_id);
		if (null == player) {
			log.error("RoleImpl.CommonCheck() No Player Id {}", player_id);
			return result;
		}

		RoleInfo has = player.GetRole(role_id);
		if (null != has) {
			log.error("RoleImpl.CommonCheck() Player Id {} Already Has Role {}", player_id, role_id);
			return result;
		}

		PlayerInfo p = player_db.loadById(player_id);
		if (null == p) {
			log.error("RoleImpl.CommonCheck() Player No Id {}", player_id);
			return result;
		}

		if (check_fragment) {
			int frag_id = config.getFragment();
			int frag_count = config.getRoleSynthesis();
			PlayerBagInfo bag = player_bag_db.loadByUid(player_id);
			if (bag == null) {
				log.error("AddNewRole() Player Id {} not Bag", player_id);
				return result;
			}
			if (!bag.hasItemCount(frag_id, frag_count)) {
				// log.error("AddNewRole() Player Id {} not enough frag role_id
				// {}", player_id, role_id);
				result.msg_id = 1114; // 碎片不足的id
				return result;
			} else {
				bag.subItemCount(frag_id, frag_count);
				player_bag_db.save();
			}

			result.frag_id = frag_id;
			result.frag_count = bag.getItemCount(frag_id);
		}
		result.success = true;

		RoleInfo role = new RoleInfo();
		role.InitByRoleConfigIdAndLv(role_id, 1);
		EquipImpl.changeRoleEnchantId(role);
		player.roles.add(role);
		RoleAttrCalc.RefreshRoleAttr(role_id, player);
		RoleAttrCalc.RecalcPveTeamInfo(player, p);
		result.p = p;

		player_db.save();
		player_roles_info_db.save();
		// log.info("AddNewRole() Player Id {} Add Role {} success!", player_id,
		// role_id);
		result.msg_id = 0;
		result.role = role.Clone();

		// 主线
		TaskImpl.doTask(player_id, TaskType.HAS_ROLE_BREACH, role.breach);
		TaskImpl.doTask(player_id, TaskType.HAS_ROLE_LV, role.lv);
		TaskImpl.doTask(player_id, TaskType.HAS_ROLE_ID, role_id);
		int all_lv = 0;
		for (RoleInfo r : player.roles) {
			all_lv += r.lv;
		}
		TaskImpl.doTask(player_id, TaskType.HAS_ROLE_LV_ALL_ROLE, all_lv);

		// 公告
		if (config.getStar() >= 3) {
			String context = ConfigConstant.tWordconfig.get(BroadcastImpl.GET_ROLE_4_5_STAR).getWord_cn();
			context = context.replace("$1", p.user_base.getNickname());
			context = context.replace("$2", ConfigConstant.tWordconfig.get(config.getName()).getWord_cn());
			BroadcastImpl bi = BroadcastImpl.getInstance();
			bi.SendBrocast(context, p.user_base.gid, p.user_base.zid);
		}

		return result;
	}

	public CommonMsg HandleLevelUp(CommonMsg respond, int role_id, int add_level, boolean check_money) {
		CheckResult r = LevelUp(respond.header.uid, role_id, add_level, check_money);
		respond.body.role_result = new RoleResultMsg();
		if (r.msg_id == 0) {
			respond.body.sync_player_info = new SyncPlayerInfoMsg();
			respond.body.sync_player_info.gold = r.p.gold;
			respond.body.sync_player_info.team_current_fighting = r.p.team_current_fighting;
			respond.body.sync_roles = new PlayerRolesInfo();
			respond.body.sync_roles.roles = r.player.roles;

			respond.body.role_result.is_success = true;
		} else {
			respond.body.role_result.is_success = false;
			// respond.header.rt = MsgCode.HTTP_INCORRECT_PARAM; //todo
			respond.header.rt_sub = r.msg_id;
		}
		return respond;
	}

	// 升级,返回实际生了的级数，没升返回0
	private CheckResult LevelUp(String player_id, int role_id, int add_level, boolean check_money) {
		CheckResult result = new CheckResult();

		CheckResult c = CommonCheck(player_id, role_id);
		if (null == c)
			return result;
		double old_gold = c.p.gold;
		int max = Math.min(c.p.team_lv, c.config.getLvMax());
		if (c.role.lv >= max) {
			// log.error("LevelUp() Player Id {} role lv max", player_id);
			result.msg_id = 1111; // 等级上限id
			return result;
		}

		int actual_add_level = 0;
		if (add_level == 1) // 升1级
		{
			actual_add_level = add_level;
		} else if (add_level == 5) // 升5级
		{
			if (add_level + c.role.lv > max) {
				actual_add_level = max - c.role.lv;
			} else {
				actual_add_level = add_level;
			}
		} else {
			log.error("LevelUp() Player Id {} add_level ERROR {}", player_id, add_level);
			return result;
		}

		if (check_money) {
			int gold_rate = c.config.getGold(); // 倍率
			int current_lv = c.role.lv;
			double total_cost = 0.0;
			for (int i = 0; i < actual_add_level; ++i) {
				Grade g = ConfigConstant.tGrade.get(current_lv);
				if (g == null) {
					log.error("LevelUp() No Grade Id {}", current_lv);
					return result;
				}

				// 所需金币计算：角色配置表里的金币倍率Gold字段里填写的值/1000再根据等级乘以升级配置表里LvGold字段填写的金币数量
				total_cost += (gold_rate / 1000) * g.getLvGold();
				current_lv++;
			}

			if (!c.p.hasGold(total_cost)) {
				// log.error("LevelUp() Player Id {} not enough money",
				// player_id);
				result.msg_id = 1004; // 金币不足id
				return result;
			}

			c.p.subGold(total_cost);

		}

		c.role.lv += actual_add_level;

		List<RoleInfo> r = CommonEndWork(role_id, c);

		result.p = c.p;
		result.p.gold = -(old_gold - result.p.gold);
		result.player = new PlayerRolesInfo();
		result.player.roles = new ArrayList<RoleInfo>();
		for (RoleInfo i : r) {
			result.player.roles.add(i.Clone());
		}
		result.msg_id = 0;

		// 主线
		TaskImpl.doTask(c.p._id, TaskType.ROLE_LV_UP, actual_add_level);
		TaskImpl.doTask(c.p._id, TaskType.HAS_ROLE_LV, c.role.lv);
		int all_lv = 0;
		for (RoleInfo rr : c.player.roles) {
			all_lv += rr.lv;
		}
		TaskImpl.doTask(player_id, TaskType.HAS_ROLE_LV_ALL_ROLE, all_lv);

		return result;
	}

	private List<RoleInfo> CommonEndWork(int role_id, CheckResult c) {
		List<RoleInfo> r = new ArrayList<RoleInfo>();
		RoleAttrCalc.RefreshRoleAttr(role_id, c.player);
		r.add(c.player.GetRole(role_id));
		if (c.player.IsHero(role_id)) {
			PlayerImpl.UpdateTeamFightingCheckMax(c.p, c.player.CalcTeamFighting());
		} else {
			int hero_id = c.player.GetBackupsHeroId(role_id);
			if (null != c.player.GetRole(hero_id)) {
				RoleAttrCalc.RefreshRoleAttr(hero_id, c.player);
				r.add(c.player.GetRole(hero_id));
			}
			PlayerImpl.UpdateTeamFightingCheckMax(c.p, c.player.CalcTeamFighting());
		}
		player_roles_info_db.save();
		player_db.save();
		return r;
	}

	public CommonMsg HandleBreach(CommonMsg respond, int role_id, boolean check_money) {
		ResultWithBag r = Breach(respond.header.uid, role_id, check_money);
		respond.body.role_result = new RoleResultMsg();
		if (r.msg_id == 0) {
			respond.body.sync_player_info = new SyncPlayerInfoMsg();
			respond.body.sync_player_info.gold = r.r.p.gold;
			respond.body.sync_player_info.team_current_fighting = r.r.p.team_current_fighting;

			respond.body.sync_roles = new PlayerRolesInfo();
			respond.body.sync_roles.roles = r.r.player.roles;

			respond.body.sync_bag = new SyncBagMsg();
			respond.body.sync_bag.items = new HashMap<Integer, Integer>();
			for (int i = 0; i < r.ids.size(); ++i) {
				respond.body.sync_bag.items.put(r.ids.get(i), r.counts.get(i));
			}

			respond.body.role_result.is_success = true;
		} else {
			respond.body.role_result.is_success = false;
			respond.header.rt = MsgCode.HTTP_INCORRECT_PARAM; // todo
			respond.header.rt_sub = r.msg_id;
		}
		return respond;
	}

	// 突破
	private ResultWithBag Breach(String player_id, int role_id, boolean check) {
		ResultWithBag result = new ResultWithBag();
		CheckResult c = CommonCheck(player_id, role_id);
		if (null == c)
			return result;
		double old_gold = c.p.gold;
		if (c.role.breach >= c.config.getBreachId().length) {
			// log.error("Breach() Player Id {} Role id {} Breach Max",
			// player_id, role_id);
			return result;
		}

		int next_breach_id = c.config.getBreachId()[c.role.breach];
		Breach breach_config = ConfigConstant.tBreach.get(next_breach_id);
		if (null == breach_config) {
			log.error("Breach() Player Id {} Role id {}  No Breach Id {}", player_id, role_id, next_breach_id);
			return result;
		}

		if (check) {
			if (c.role.lv < breach_config.getRoleLv()) {
				// log.error("Breach() Player Id {} Role id {} Breach Id {}
				// RoleLevel Not Enough", player_id, role_id, next_breach_id);
				result.msg_id = 1112; // 等级不足id
				return result;
			}

			if (!c.p.hasGold(breach_config.getBreachMoney())) {
				// log.error("Breach() Player Id {} Role id {} Breach Id {} not
				// enough money", player_id, role_id, next_breach_id);
				result.msg_id = 1004; // 金币不足id
				return result;
			}

			PlayerBagInfo bag = player_bag_db.loadByUid(player_id);
			if (bag == null) {
				log.error("Breach() Player Id {} not Bag", player_id);
				return result;
			}

			int f_id = c.config.getFragment();
			int f_cnt = breach_config.getFragmentNum();
			if (bag.hasItemCount(f_id, f_cnt)) {
				bag.subItemCount(f_id, f_cnt);
				result.ids.add(f_id);
				result.counts.add(bag.getItemCount(f_id));
			} else {
				result.msg_id = 1113; // 道具不足id
				return result;
			}

			for (int i = 0; i < breach_config.getBreachItem().length; ++i) {
				int id = breach_config.getBreachItem()[i];
				int count = breach_config.getBreachCounts()[i];
				if (bag.hasItemCount(id, count)) {
					bag.subItemCount(id, count);
				} else {
					// log.error("Breach() Player Id {} Role id {} Breach Id {}
					// not enough Item {}"
					// , player_id, role_id, next_breach_id, id);
					result.msg_id = 1113; // 道具不足id
					return result;
				}
				result.ids.add(id);
				result.counts.add(bag.getItemCount(id));
			}

			player_bag_db.save();

			c.p.subGold(breach_config.getBreachMoney());
		}

		c.role.breach++;

		List<RoleInfo> n = CommonEndWork(role_id, c);

		//player_roles_info_db.save();

		result.r.p = c.p;
		result.r.p.gold = -(old_gold - result.r.p.gold);
		result.r.player = new PlayerRolesInfo();
		result.r.player.roles = new ArrayList<RoleInfo>();
		for (RoleInfo r : n) {
			result.r.player.roles.add(r.Clone());
		}
		result.msg_id = 0;

		// 主线
		TaskImpl.doTask(c.p._id, TaskType.ROLE_BREACH, 1);
		TaskImpl.doTask(c.p._id, TaskType.HAS_ROLE_BREACH, c.role.breach);

		// 公告
		if (c.role.breach >= 6) {
			String context = ConfigConstant.tWordconfig.get(BroadcastImpl.ROLE_BREACH).getWord_cn();
			context = context.replace("$1", c.p.user_base.getNickname());
			context = context.replace("$2", ConfigConstant.tWordconfig.get(c.config.getName()).getWord_cn());
			context = context.replace("$3", Integer.toString(c.role.breach));
			BroadcastImpl bi = BroadcastImpl.getInstance();
			// System.out.println(context);
			bi.SendBrocast(context, c.p.user_base.gid, c.p.user_base.zid);
		}

		return result;
	}

	public CommonMsg HandleTalent(CommonMsg respond, int role_id, boolean check_money) {
		ResultWithBag r = Talent(respond.header.uid, role_id, check_money);
		respond.body.role_result = new RoleResultMsg();
		if (r.msg_id == 0) {
			respond.body.sync_player_info = new SyncPlayerInfoMsg();
			respond.body.sync_player_info.gold = r.r.p.gold;
			respond.body.sync_player_info.team_current_fighting = r.r.p.team_current_fighting;

			respond.body.sync_roles = new PlayerRolesInfo();
			respond.body.sync_roles.roles = r.r.player.roles;

			respond.body.sync_bag = new SyncBagMsg();
			respond.body.sync_bag.items = new HashMap<Integer, Integer>();

			respond.body.role_result.is_success = true;

			for (int i = 0; i < r.ids.size(); ++i) {
				respond.body.sync_bag.items.put(r.ids.get(i), r.counts.get(i));
			}
		} else {
			respond.body.role_result.is_success = false;
			respond.header.rt = MsgCode.HTTP_INCORRECT_PARAM; // todo
			respond.header.rt_sub = r.msg_id;
		}
		return respond;
	}

	// 升阶
	private ResultWithBag Talent(String player_id, int role_id, boolean check) {
		ResultWithBag result = new ResultWithBag();
		CheckResult c = CommonCheck(player_id, role_id);
		if (null == c)
			return result;
		double old_gold = c.p.gold;
		Talent talent_config = ConfigConstant.tTalent.get(c.role.talent);
		if (talent_config == null) {
			log.error("Talent() Player Id {} Role id {} Talent ID{} MAX Level", player_id, role_id, c.role.talent);
			return result;
		}

		if (check) {
			if (!c.p.hasGold(talent_config.getTalentMoney())) {
				// log.error("Talent() Player Id {} Role id {} Talent Id {} not
				// enough money", player_id, role_id, c.role.talent);
				result.msg_id = 1004; // 金币不足id
				return result;
			}

			PlayerBagInfo bag = player_bag_db.loadByUid(player_id);
			if (bag == null) {
				log.error("Talent() Player Id {} not Bag", player_id);
				return result;
			}

			for (int i = 0; i < talent_config.getTalentItem().length; ++i) {
				int id = talent_config.getTalentItem()[i];
				int count = talent_config.getTalentCounts()[i];
				if (bag.hasItemCount(id, count)) {
					bag.subItemCount(id, count);
				} else {
					// log.error("Talent() Player Id {} Role id {} Talent Id {}
					// not enough Item {}"
					// , player_id, role_id, c.role.talent, id);
					result.msg_id = 1113; // 道具不足id
					return result;
				}
				result.ids.add(id);
				result.counts.add(bag.getItemCount(id));
			}
			player_bag_db.save();

			c.p.subGold(talent_config.getTalentMoney());
		}

		c.role.talent++;

		Talent next_talent_config = ConfigConstant.tTalent.get(c.role.talent);
		if (next_talent_config != null) {
			c.role.skill_lv = next_talent_config.getTalentSkill();
			c.role.base_lv = c.role.skill_lv;
		}

		List<RoleInfo> n = CommonEndWork(role_id, c);

		//player_roles_info_db.save();

		result.r.p = c.p;
		result.r.p.gold = result.r.p.gold - old_gold;
		result.r.player = new PlayerRolesInfo();
		result.r.player.roles = new ArrayList<RoleInfo>();
		for (RoleInfo r : n) {
			result.r.player.roles.add(r.Clone());
		}
		result.msg_id = 0;

		// 主线
		TaskImpl.doTask(c.p._id, TaskType.ROLE_TALENT, 1);

		// 公告
		if (c.role.talent >= 4) {
			String context = ConfigConstant.tWordconfig.get(BroadcastImpl.ROLE_TALENT).getWord_cn();
			context = context.replace("$1", c.p.user_base.getNickname());
			context = context.replace("$2", ConfigConstant.tWordconfig.get(c.config.getName()).getWord_cn());
			context = context.replace("$3", Integer.toString(c.role.talent));
			BroadcastImpl bi = BroadcastImpl.getInstance();
			bi.SendBrocast(context, c.p.user_base.gid, c.p.user_base.zid);
		}

		return result;
	}

	public CommonMsg HandleAwaken(CommonMsg respond, int role_id, boolean check_money) {
		ResultWithBag r = Awaken(respond.header.uid, role_id, check_money);
		respond.body.role_result = new RoleResultMsg();
		if (r.msg_id == 0) {
			respond.body.sync_player_info = new SyncPlayerInfoMsg();
			respond.body.sync_player_info.gold = r.r.p.gold;
			respond.body.sync_player_info.team_current_fighting = r.r.p.team_current_fighting;

			respond.body.sync_roles = new PlayerRolesInfo();
			respond.body.sync_roles.roles = r.r.player.roles;

			respond.body.sync_bag = new SyncBagMsg();
			respond.body.sync_bag.items = new HashMap<Integer, Integer>();

			respond.body.role_result.is_success = true;

			for (int i = 0; i < r.ids.size(); ++i) {
				respond.body.sync_bag.items.put(r.ids.get(i), r.counts.get(i));
			}
		} else {
			respond.body.role_result.is_success = false;
			respond.header.rt = MsgCode.HTTP_INCORRECT_PARAM; // todo
			respond.header.rt_sub = r.msg_id;
		}
		return respond;
	}

	// 觉醒
	private ResultWithBag Awaken(String player_id, int role_id, boolean check) {
		ResultWithBag result = new ResultWithBag();
		CheckResult c = CommonCheck(player_id, role_id);
		if (null == c)
			return result;
		double old_gold = c.p.gold;
		Awaken awaken_config = ConfigConstant.tAwaken.get(c.role.awaken);
		if (awaken_config == null || c.role.awaken >= c.config.getAwakenMax()) {
			// log.error("Awaken() Player Id {} Role id {} Awaken ID{} MAX
			// Level", player_id, role_id, c.role.awaken);
			return result;
		}

		if (check) {
			if (!c.p.hasGold(awaken_config.getAwakenMoney())) {
				// log.debug("Awaken() Player Id {} Role id {} Awaken Id {} not
				// enough money", player_id, role_id, c.role.awaken);
				result.msg_id = 1004; // 金币不足id
				return result;
			}

			PlayerBagInfo bag = player_bag_db.loadByUid(player_id);
			if (bag == null) {
				log.error("Awaken() Player Id {} not Bag", player_id);
				return result;
			}

			int id = c.config.getFragment();
			int[] frag = awaken_config.getAwakenFragment();
			int count = 9999;
			if (frag.length >= c.config.getStar())
				count = frag[c.config.getStar() - 1];
			if (bag.hasItemCount(id, count)) {
				bag.subItemCount(id, count);
			} else {
				// log.error("Awaken() Player Id {} Role id {} Awaken Id {} not
				// enough Item {}"
				// , player_id, role_id, c.role.talent, id);
				result.msg_id = 1113; // 道具不足id
				return result;
			}
			result.ids.add(id);
			result.counts.add(bag.getItemCount(id));
			player_bag_db.save();
			c.p.subGold(awaken_config.getAwakenMoney());
		}

		c.role.awaken++;

		List<RoleInfo> n = CommonEndWork(role_id, c);

		//player_roles_info_db.save();

		result.r.p = c.p;
		result.r.p.gold = result.r.p.gold - old_gold;
		result.r.player = new PlayerRolesInfo();
		result.r.player.roles = new ArrayList<RoleInfo>();
		for (RoleInfo r : n) {
			result.r.player.roles.add(r.Clone());
		}
		result.msg_id = 0;

		// 主线
		TaskImpl.doTask(c.p._id, TaskType.ROLE_AWAKEN, 1);

		// 公告
		if (c.role.awaken % 5 == 0) {
			String context = ConfigConstant.tWordconfig.get(BroadcastImpl.ROLE_AWAKEN).getWord_cn();
			context = context.replace("$1", c.p.user_base.getNickname());
			context = context.replace("$2", ConfigConstant.tWordconfig.get(c.config.getName()).getWord_cn());
			BroadcastImpl bi = BroadcastImpl.getInstance();
			bi.SendBrocast(context, c.p.user_base.gid, c.p.user_base.zid);
		}

		return result;
	}

	public CommonMsg handleAwakenReset(CommonMsg respond, CommonMsg receive)
	{
		String uid = respond.header.uid;
		RoleResultMsg msg = new RoleResultMsg();
		respond.body.role_result = msg;
		msg.is_success = false;
		
		PlayerBagInfo bag = player_bag_db.loadByUid(uid);
		if (bag == null) {
			log.error("Player Id {} not Bag", uid);
			return respond;
		}
		
		int role_id = receive.body.role_result.role_id;
		CheckResult c = CommonCheck(uid, role_id);
		if (null == c)
		{
			return respond;
		}
		
		int cost_dmd = ConfigConstant.tConf.getAwakenReset();
		if (!c.p.hasDiamond(cost_dmd))
		{
			respond.header.rt_sub = 1005;
			return respond;
		}
		
		int id = c.config.getFragment();
		int current_awaken = c.role.awaken;
		int init_awaken = c.config.getAwakenID();
		int all_frag = 0;
		for (int i = init_awaken; i < current_awaken; ++i)
		{
			Awaken awaken_config = ConfigConstant.tAwaken.get(i);
			int[] frag = awaken_config.getAwakenFragment();
			int count = 9999;
			if (frag.length >= c.config.getStar())
				count = frag[c.config.getStar() - 1];
			all_frag += count;
		}
		
		PlayerImpl.SubDiamond(c.p, cost_dmd);
		c.role.awaken = init_awaken;
		bag.addItemCount(id, all_frag);
		player_bag_db.save();
		CommonEndWork(role_id, c);
		
		msg.r_items = new ArrayList<SyncBagItem>();
		SyncBagItem ri = new SyncBagItem();
		ri.id = id;
		ri.count = all_frag;
		msg.r_items.add(ri);
		
		respond.body.sync_roles = c.player;
		respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.items = new HashMap<Integer, Integer>();
		respond.body.sync_bag.items.put(id, bag.getItemCount(id));
		respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.is_refresh = false;
		respond.body.sync_player_info.team_current_fighting = c.p.team_current_fighting;
		respond.body.sync_player_info.diamond = -cost_dmd;
		
		msg.is_success = true;
		return respond;
	}

	public CommonMsg HandleHeroChange(CommonMsg respond, int role_id, int pos) {
		CheckResult result = ChangeHeroPos(respond.header.uid, role_id, pos);
		respond.body.role_result = new RoleResultMsg();
		if (result != null) {
			respond.body.role_result.is_success = true;
			respond.body.sync_roles = result.player;
			respond.body.sync_player_info = new SyncPlayerInfoMsg();
			respond.body.sync_player_info.is_refresh = false;
			respond.body.sync_player_info.team_current_fighting = result.p.team_current_fighting;
		} else {
			respond.body.role_result.is_success = false;
			respond.header.rt = MsgCode.HTTP_INCORRECT_PARAM; // todo
		}

		return respond;
	}

	// 主将上阵, 会把原来所在的位置清除掉
	private CheckResult ChangeHeroPos(String player_id, int role_id, int pos) {
		if (pos < 1 || pos > 5) {
			log.error("RoleImpl.ChangeHeroPos() Player Id {} pos {} Error", player_id, pos);
			return null;
		}

		CheckResult c = CommonCheck(player_id, role_id);
		if (null == c)
			return null;

		// 检查数量与等级
		int hero_count = 0;
		boolean add = false;
		int hero_id = 0;
		for (Entry<Integer, Integer> hero_pos : c.player.pve_team.entrySet()) {
			if (hero_pos.getValue() != 0) {
				if (hero_pos.getKey() == pos) {
					hero_id = hero_pos.getValue();
				}
				hero_count++;
			} else if (hero_pos.getKey() == pos) {
				add = true;
			}
		}
		if (hero_count >= c.p.GetMaxHeroCounts() && add) {
			// log.error("RoleImpl.ChangeHeroPos() Player Id {} Hero Counts
			// FULL", player_id);
			return null;
		}

		CheckAndCleanRoleHeroAndBackup(role_id, c.player);

		// 上阵
		c.player.pve_team.put(pos, role_id);

		// 算换下来的人的战力
		RoleAttrCalc.RefreshRoleAttr(hero_id, c.player);

		for (Entry<Integer, Integer> i : c.player.pve_team.entrySet()) {
			RoleAttrCalc.RefreshRoleAttr(i.getValue(), c.player);
		}
		boolean max_change = PlayerImpl.UpdateTeamFightingCheckMax(c.p, c.player.CalcTeamFighting());
		player_db.save();
		player_roles_info_db.save();

		PlayerRolesInfo result = new PlayerRolesInfo();
		result.pve_team = new HashMap<Integer, Integer>();
		for (Entry<Integer, Integer> i : c.player.pve_team.entrySet()) {
			result.pve_team.put(i.getKey().intValue(), i.getValue().intValue());
		}
		result.backup_info = new HashMap<Integer, Map<Integer, Integer>>();
		for (Entry<Integer, Map<Integer, Integer>> i : c.player.backup_info.entrySet()) {
			result.backup_info.put(i.getKey().intValue(), new HashMap<Integer, Integer>());
			for (Entry<Integer, Integer> j : i.getValue().entrySet()) {
				result.backup_info.get(i.getKey()).put(j.getKey().intValue(), j.getValue().intValue());
			}
		}
		result.roles = new ArrayList<RoleInfo>();
		for (RoleInfo i : c.player.roles) {
			if (i.role_id == hero_id || result.pve_team.containsValue(i.role_id))
				result.roles.add(i.Clone());
		}

		CheckResult x = new CheckResult();
		x.player = result;
		x.p = new PlayerInfo();
		x.p.team_current_fighting = c.p.team_current_fighting;
		if (max_change)
			x.p.team_history_max_fighting = c.p.team_history_max_fighting;
		x.msg_id = 0;

		// 主线任务
		int role_cnt = 0;
		for (Entry<Integer, Integer> m : c.player.pve_team.entrySet()) {
			if (c.player.GetRole(m.getValue()) != null) {
				role_cnt++;
			}
		}
		TaskImpl.doTask(c.p._id, TaskType.HAS_ROLE_PVE_CNT, role_cnt);
		return x;
	}

	public CommonMsg HandleChangeBackupPos(CommonMsg respond, int role_id, int hero_pos, int backup_pos) {
		CheckResult result = ChangeBackupPos(respond.header.uid, role_id, hero_pos, backup_pos);
		respond.body.role_result = new RoleResultMsg();
		if (result.msg_id == 0) {
			respond.body.role_result.is_success = true;
			respond.body.sync_roles = result.player;

			respond.body.sync_player_info = new SyncPlayerInfoMsg();
			respond.body.sync_player_info.is_refresh = false;
			respond.body.sync_player_info.team_current_fighting = result.p.team_current_fighting;
		} else {
			respond.body.role_result.is_success = false;
			respond.header.rt = MsgCode.HTTP_INCORRECT_PARAM; // todo
		}
		return respond;
	}

	// 副将上阵
	private CheckResult ChangeBackupPos(String player_id, int role_id, int hero_pos, int backup_pos) {
		CheckResult x = new CheckResult();
		x.msg_id = 1;
		if (hero_pos < 1 || hero_pos > 5) {
			log.error("RoleImpl.ChangeBackupPos() Player Id {} hero_pos {} Error", player_id, hero_pos);
			return x;
		}

		if (backup_pos < 1 || backup_pos > 3) {
			log.error("RoleImpl.ChangeBackupPos() Player Id {} backup_pos {} Error", player_id, backup_pos);
			return x;
		}

		CheckResult c = CommonCheck(player_id, role_id);
		if (null == c)
			return x;

		boolean pos_has_hero = false;
		// int hero_id = 0;
		for (Entry<Integer, Integer> hero : c.player.pve_team.entrySet()) {
			if (hero.getKey() == hero_pos && hero.getValue() != 0) {
				// hero_id = hero.getValue();
				pos_has_hero = true;
				break;
			}
		}
		if (!pos_has_hero) {
			// log.error("RoleImpl.ChangeBackupPos() Player Id {} hero_pos {}
			// Empty", player_id, hero_pos);
			return x;
		}

		if (backup_pos > c.p.GetMaxBackupPosCounts()) {
			// log.error("RoleImpl.ChangeBackupPos() Player Id {} Backup Counts
			// FULL", player_id);
			return x;
		}

		CheckAndCleanRoleHeroAndBackup(role_id, c.player);

		// 副将上阵
		if (!c.player.backup_info.containsKey(hero_pos)) {
			c.player.backup_info.put(hero_pos, new HashMap<Integer, Integer>());
		}
		c.player.backup_info.get(hero_pos).put(backup_pos, role_id);

		// log.info("计算上阵副将");
		RoleAttrCalc.RefreshRoleAttr(role_id, c.player);
		for (Entry<Integer, Integer> i : c.player.pve_team.entrySet()) {
			// log.info("计算上阵副将导致主将的变化");
			RoleAttrCalc.RefreshRoleAttr(i.getValue(), c.player);
		}
		boolean max_change = PlayerImpl.UpdateTeamFightingCheckMax(c.p, c.player.CalcTeamFighting());
		player_db.save();
		player_roles_info_db.save();

		PlayerRolesInfo result = new PlayerRolesInfo();
		result.pve_team = new HashMap<Integer, Integer>();
		for (Entry<Integer, Integer> i : c.player.pve_team.entrySet()) {
			result.pve_team.put(i.getKey().intValue(), i.getValue().intValue());
		}
		result.backup_info = new HashMap<Integer, Map<Integer, Integer>>();
		for (Entry<Integer, Map<Integer, Integer>> i : c.player.backup_info.entrySet()) {
			result.backup_info.put(i.getKey().intValue(), new HashMap<Integer, Integer>());
			for (Entry<Integer, Integer> j : i.getValue().entrySet()) {
				result.backup_info.get(i.getKey()).put(j.getKey().intValue(), j.getValue().intValue());
			}
		}
		result.roles = new ArrayList<RoleInfo>();
		for (RoleInfo i : c.player.roles) {
			if (i.role_id == role_id || result.pve_team.containsValue(i.role_id))
				result.roles.add(i.Clone());
		}

		x.player = result;
		x.p = new PlayerInfo();
		x.p.team_current_fighting = c.p.team_current_fighting;
		if (max_change)
			x.p.team_history_max_fighting = c.p.team_history_max_fighting;
		x.msg_id = 0;

		// 主线任务
		TaskImpl.doTask(player_id, TaskType.HAS_ONE_BACKUP, 0);

		return x;
	}

	public CommonMsg HandleRefreshHeroPos(CommonMsg respond, Map<Integer, Integer> new_pos_info) {
		PlayerRolesInfo r = RefreshHeroPos(respond.header.uid, new_pos_info);
		respond.body.role_result = new RoleResultMsg();
		if (r != null) {
			respond.body.role_result.is_success = true;
			respond.body.sync_roles = new PlayerRolesInfo();
			respond.body.sync_roles.pve_team = r.pve_team;
			respond.body.sync_roles.backup_info = r.backup_info;
		} else {
			respond.body.role_result.is_success = false;
			respond.header.rt = MsgCode.HTTP_INCORRECT_PARAM; // todo
		}
		return respond;
	}

	// 布阵
	public PlayerRolesInfo RefreshHeroPos(String player_id, Map<Integer, Integer> new_pos_info) {
		PlayerInfo p = player_db.loadById(player_id);
		if (null == p) {
			log.error("RoleImpl.RefreshHeroPos() Player No Id {}", player_id);
			return null;
		}
		if (p.GetMaxHeroCounts() < new_pos_info.size()) {
			// log.error("RoleImpl.RefreshHeroPos() Player Id {} Hero Counts
			// FULL", player_id);
			return null;
		}

		PlayerRolesInfo player = loadPlayerRoles(player_id);
		if (null == player) {
			log.error("RoleImpl.RefreshHeroPos() No Player Id {}", player_id);
			return null;
		}
		// log.info("rec pos {}", JsonTransfer.getJson(new_pos_info));
		Map<Integer, Integer> old_pve = player.pve_team;
		Map<Integer, Map<Integer, Integer>> old_backup = player.backup_info;
		// log.info("old pve {} old backup {}", JsonTransfer.getJson(old_pve),
		// JsonTransfer.getJson(old_backup));
		Map<Integer, Integer> new_pve = new HashMap<Integer, Integer>();
		Map<Integer, Map<Integer, Integer>> new_backup = new HashMap<Integer, Map<Integer, Integer>>();

		for (Entry<Integer, Integer> new_p : new_pos_info.entrySet()) {
			if (new_p.getKey() < 1 || new_p.getKey() > 5) {
				log.error("RoleImpl.RefreshHeroPos() Player Id {} hero_pos {} Error", player_id, new_p.getKey());
				return null;
			}

			if (player.GetRole(new_p.getValue()) == null) {
				log.error("RoleImpl.RefreshHeroPos() Player Id {} no Role {}", player_id, new_p.getValue());
				return null;
			}
			new_pve.put(new_p.getKey().intValue(), new_p.getValue().intValue());
		}

		// log.info("new pve {}", JsonTransfer.getJson(new_pve));

		for (Entry<Integer, Integer> new_p : new_pve.entrySet()) {
			int new_pos = new_p.getKey();
			int new_hero = new_p.getValue();
			int old_pos = -1;
			int old_hero = -1;
			for (Entry<Integer, Integer> old_p : old_pve.entrySet()) {
				if (old_p.getValue() == new_hero) {
					old_pos = old_p.getKey();
					old_hero = old_p.getValue();
					break;
				}
			}
			if (old_hero == new_hero) {
				for (Entry<Integer, Map<Integer, Integer>> old_b : old_backup.entrySet()) {
					if (old_b.getKey() == old_pos) {
						new_backup.put(new_pos, old_b.getValue());
						break;
					}
				}
			}
		}
		// log.info("CHECK old pve {} old backup {}",
		// JsonTransfer.getJson(old_pve), JsonTransfer.getJson(old_backup));
		// log.info("new backup {}", JsonTransfer.getJson(new_backup));
		player.pve_team = new_pve;
		player.backup_info = new_backup;

		player_roles_info_db.save();
		return player;
	}

	private CheckResult CommonCheck(String player_id, int role_id) {
		Role config = ConfigConstant.tRole.get(role_id);
		if (null == config) {
			log.error("RoleImpl.CommonCheck() No Role Id {}", role_id);
			return null;
		}

		PlayerRolesInfo player = loadPlayerRoles(player_id);
		if (null == player) {
			log.error("RoleImpl.CommonCheck() No Player Id {}", player_id);
			return null;
		}

		RoleInfo has = player.GetRole(role_id);
		if (null == has) {
			log.error("RoleImpl.CommonCheck() Player Id {} no Role {}", player_id, role_id);
			return null;
		}

		PlayerInfo p = player_db.loadById(player_id);
		if (null == p) {
			log.error("RoleImpl.CommonCheck() Player No Id {}", player_id);
			return null;
		}

		CheckResult r = new CheckResult();
		r.config = config;
		r.player = player;
		r.role = has;
		r.p = p;

		return r;
	}

	private void CheckAndCleanRoleHeroAndBackup(int role_id, PlayerRolesInfo info) {
		// 是否在副将位置，在的清除
		for (Entry<Integer, Map<Integer, Integer>> hero_pos : info.backup_info.entrySet()) {
			boolean found = false;
			for (Entry<Integer, Integer> backup : hero_pos.getValue().entrySet()) {
				if (backup.getValue() == role_id) {
					backup.setValue(0);
					found = true;
					break;
				}
			}
			if (found)
				break;
		}

		// 是否已上阵，上阵的清除
		for (Entry<Integer, Integer> hero_pos : info.pve_team.entrySet()) {
			if (hero_pos.getValue() == role_id) {
				hero_pos.setValue(0);
				break;
			}
		}
	}
}

class CheckResult {
	public Role config = null;
	public PlayerRolesInfo player = null;
	public RoleInfo role = null;
	public PlayerInfo p = null;
	public int msg_id = -1;
}

class AddNewRoleResult {
	public boolean success = false;
	public int frag_id = 0;
	public int frag_count = 0;
	public RoleInfo role = null;
	public int msg_id = -1;
	public PlayerInfo p = null;
}

class ResultWithBag {
	public CheckResult r = new CheckResult();
	public List<Integer> ids = new ArrayList<Integer>();
	public List<Integer> counts = new ArrayList<Integer>();
	public int msg_id = -1;
}
