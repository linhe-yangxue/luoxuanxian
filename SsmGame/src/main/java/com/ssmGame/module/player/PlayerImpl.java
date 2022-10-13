package com.ssmGame.module.player;

import com.ssmCore.memcached.MemAccess;
import com.ssmCore.tool.colligate.CommUtil;
import com.ssmCore.tool.colligate.HttpRequest;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Config;
import com.ssmData.config.entity.Role;
import com.ssmData.dbase.*;
import com.ssmGame.defdata.msg.common.CommonHeaderMsg;
import com.ssmGame.defdata.msg.player.PlayerLoginMsg;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncEquipBagMsg;
import com.ssmGame.module.activity.ActivityHandleImpl;
import com.ssmGame.module.boss.BossImpl;
import com.ssmGame.module.equip.EquipImpl;
import com.ssmGame.module.goldbuy.GoldBuyImpl;
import com.ssmGame.module.lvgift.LvgiftImpl;
import com.ssmGame.module.mail.MailImpl;
import com.ssmGame.module.pve.PveImpl;
import com.ssmGame.module.role.RoleAttrCalc;
import com.ssmGame.module.sns.SnsImpl;
import com.ssmGame.module.task.TaskImpl;
import com.ssmGame.module.task.TaskType;
import com.ssmGame.servlet.AdvancedServlet.MsgTimer;
import com.ssmShare.entity.UserBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;

@Service
@Scope("prototype")
public class PlayerImpl {

    private static final Logger log = LoggerFactory.getLogger(PlayerImpl.class);
    private @Value("${LOGIN_RUL}")
    String loginUrl; // 登录服务器地址
    /**
     * 数据访问对象
     */
    @Autowired
    private PlayerInfoDB player_info_db;

    @Autowired
    private PlayerLevelDB player_level_db;

    @Autowired
    private PlayerBagDB player_bag_db;

    @Autowired
    private PlayerMailDB mail_db;

    @Autowired
    private PlayerMonthcardDB month_db;

    @Autowired
    private PlayerGuildDB guild_db;

    @Autowired
    private PlayerRolesInfoDB player_roles_info_db;

    public PlayerInfo getM_player_info() {
        return m_player_info;
    }

    private PlayerInfo m_player_info;

    // region 生命周期管理

    /**
     * 获取实例
     *
     * @return
     */
    public final static PlayerImpl getInstance() {
        return SpringContextUtil.getBean(PlayerImpl.class);
    }

    /**
     * 初始化
     */
    public PlayerImpl init(String uid) {

        // 加载依赖的数据
        this.m_player_info = player_info_db.loadById(uid);

        // 角色判空
        if (m_player_info == null) {
            log.error("无法初始化PlayerImpl，找不到角色 " + uid);
            return null;
        }

        return this;
    }

    /**
     * 手动销毁
     */
    public void Destory() {
        m_player_info = null;
        player_info_db = null;
        player_level_db = null;
        player_bag_db = null;
        player_roles_info_db = null;
    }
    // endregion

    // region 用户账户操作

    /**
     * 用户登录
     *
     * @param username
     *            用户名
     * @return
     */
	/*public PlayerLoginMsg login(String username) {
		PlayerLoginMsg result = new PlayerLoginMsg();
		//result.is_new = false;

		PlayerInfo info = player_info_db.loadByUsernameNotCreate(username);
		if (null == info) {
			//result.is_new = true;
			info = player_info_db.loadByUsername(username);
			log.info("新用户创建，用户名：{}，id:{}", username, info._id);
			DataEyeSdk.Act(username);
			DataEyeSdk.Reg(username);
		} else if (info.user_base.getNickname() == null || info.user_base.getuImg() == null) {
			log.info("新用户创建，还没有填昵称和头像，用户名：{}，id:{}", username, info._id);
			//result.is_new = true;
		}
		DataEyeSdk.Online(username);

		// 读取或创建关卡信息
		PlayerLevelInfo levelInfo = player_level_db.loadByUid(info._id);
		if (levelInfo == null)
			levelInfo = (PlayerLevelInfo)player_level_db.createDB(info._id);

		// 读取或创建背包信息
		PlayerBagInfo bagInfo = player_bag_db.loadByUid(info._id);
		if (bagInfo == null)
		{
			bagInfo = (PlayerBagInfo)player_bag_db.createDB(info._id);
		}
		SyncBagMsg bag = new SyncBagMsg(true);
		bag.items = bagInfo.items;
		SyncEquipBagMsg equip_bag = new SyncEquipBagMsg(true);
		equip_bag.add = bagInfo.equips;

		// 创建初始的角色列表
		PlayerRolesInfo roles = player_roles_info_db.load(info._id);
		if (roles == null)
		{
			roles = (PlayerRolesInfo)player_roles_info_db.createDB(info._id);
			roles.roles = new ArrayList<RoleInfo>();
			roles.pve_team = new HashMap<Integer, Integer>();
			roles.backup_info = new HashMap<Integer, Map<Integer, Integer>>();
			Config c = ConfigConstant.tConf;
			Map<Integer, Role> role_config = ConfigConstant.tRole;
			int j = 1;
        
			List<Integer> temp_c = new ArrayList<Integer>();
			for (int i : c.getIniPart()) {
				temp_c.add(i);
			}
			for (int i = 0; i < c.getIniPet().length; ++i)
			{
				Integer role_id = c.getIniPet()[i];
				Role r = role_config.get(role_id);
				if (null == r)
					continue;
				if (j <= 5 && temp_c.contains(role_id))
					roles.pve_team.put(j++, role_id);
				RoleInfo r_info = new RoleInfo();
				r_info.InitByRoleConfigId(role_id);
				EquipImpl.changeRoleEnchantId(r_info);
				roles.roles.add(r_info);
			}

			if (null == mail_db.loadByUid(info._id))
				mail_db.createDB(info._id);
		}
		RoleAttrCalc.RecalcAllInfo(roles, info);

		player_info_db.save();
		player_roles_info_db.save();
		player_level_db.save();

		log.info("用户登陆，用户名：{}， id:{}", username, info._id);

		// 初始化用到的其他模块
		PveImpl pve_impl = PveImpl.getInstance().init(info._id);

		// 初始化下发信息
		result._id = info._id;
		
		result.current_level_id = levelInfo.cur_level;
		result.diamond = info.diamond;
		result.gold = info.gold;
		result.icon_url = info.user_base.getuImg();
		result.team_current_fighting = info.team_current_fighting;
		result.team_exp = info.team_exp;
		result.team_history_max_fighting = info.team_history_max_fighting;
		result.team_lv = info.team_lv;
		result.username = username;
		result.nickname = info.user_base.getNickname();
		result.vip_level = info.vip_level;
		result.pve_offline = pve_impl.reqOfflineReward();// 离线收益信息
		result.roles = roles;
		result.bag = bag;
		result.equip_bag = equip_bag;
		result.acc_rmb = info.acc_rmb;
		result.vip_award = info.vip_award;
		result.last_gold_time = info.last_gold_time;
		result.gold_buy_cnt = info.gold_buy_cnt;

		// PVE模块登录响应
		pve_impl.onPlayerLogin(); // 调用PVE模块登陆响应

		// 回收用到的其他模块
		pve_impl.destroy();

		Destory();
		return result;
	}*/

    /**
     * 创建新用户
     *
     * @param _id      唯一id
     * @param nickname 昵称
     * @param icon_url 头像url
     * @return
     */
    public boolean create(String _id, String nickname, String icon_url) {
        boolean result = false;

        PlayerInfo info = player_info_db.loadById(_id);
        if (info != null) {
            info.user_base.setNickname(nickname);
            info.user_base.setuImg(icon_url);
            System.out.println(7);
            info.vip_level = 7;
            System.out.println(info.vip_level);

            String url = loginUrl + "modfy?uuid=" + info._id
                    + "&nickname=" + info.user_base.getNickname() + "&rolelevel=" + info.team_lv
                    + "&zid=" + info.user_base.zid + "&guid=" + info.user_base.getGuid() + "&gid=" + info.user_base.gid
                    + "&type=0";
            try {
                //服务器访问连接地址（登录服）修改名称头像
                HttpRequest.GetFunction(url);


                //if (logMsg.trim().equals("success")) {
                player_info_db.save();
                result = true;
                PlayerLevelInfo levelInfo = player_level_db.loadByUid(info._id);
                levelInfo.cur_level = ConfigConstant.tConf.getInitialLevel();
                player_level_db.save();

                // vip邮件
                if (ConfigConstant.tConf.getCreateMail() != 0) {
                    //发邮件
                    PlayerMailDB my_mail_db = SpringContextUtil.getBean(PlayerMailDB.class);
                    PlayerMailInfo mail_info = my_mail_db.loadByUid(_id);
                    if (mail_info == null) {
                        mail_info = new PlayerMailInfo();
                        mail_info.uid = _id;
                        mail_info.mails = new ArrayList<MailInfo>();
                    }
                    MailImpl.AddMail(mail_info, ConfigConstant.tConf.getCreateMail(),
                            null, 1, null);
                    my_mail_db.save();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (result) {
            log.info("user create success：name {}， icon:{}， id:{}", nickname, icon_url, _id);
        } else {
            log.info("user create fail, id:{} ", _id);
        }

        Destory();
        return result;
    }

    public PlayerLoginMsg platLogin(CommonHeaderMsg header) {
        // 访问登录服务器
//		log.info("访问登录服务器");
        PlayerLoginMsg result = new PlayerLoginMsg();
        result.is_new = false;
        result.need_create = false;
        try {
            String url = loginUrl + "user?mkey=" + header.mkey + "&zid=" + header.zid;
            //服务器访问连接地址（登录服）拿用户基本信息
            String logMsg = HttpRequest.GetFunction(url);
            System.out.println(url);
            if (logMsg == null) // 注册登录服务器信息
            {
                return null;
            }

            UserBase net_userbase = JsonTransfer._In(logMsg, UserBase.class);
            header.uid = net_userbase.getUid();
            String old_key = (String) MemAccess.GetValue(MsgTimer.MTK + header.uid);
            if (header.uid == null || !CommUtil.isNumeric(header.uid) || (old_key != null && old_key.equals(header.mkey))) {
                log.error("Net userbase err log msg {}", logMsg);
                Destory();
                result = null;
                return result;
            }

            cleanAllMem(net_userbase.getUid());

            //================================================================
            PlayerInfo info = player_info_db.loadById(net_userbase.getUid(), net_userbase);
            UserBase old_userbase = info.user_base;
            info.user_base = net_userbase;

            if (net_userbase.getNickname() == null || net_userbase.getuImg() == null) {
                if (old_userbase == null) {
                    result.need_create = true;
                } else if (old_userbase.getNickname() == null || old_userbase.getuImg() == null) {
                    result.need_create = true;
                } else {
                    info.user_base.setNickname(old_userbase.getNickname());
                    info.user_base.setuImg(old_userbase.getuImg());
                }
            }

            Calendar last_login_cal = Calendar.getInstance();
            last_login_cal.setTimeInMillis(0);
            if (info.login_t != null && info.login_t > 0)
                last_login_cal.setTimeInMillis(info.login_t);
            Calendar now_cal = Calendar.getInstance();
            info.login_t = now_cal.getTimeInMillis();
            if (info.lv_t == null || (info.lv_t != null && info.lv_t == 0)) {
                info.lv_t = info.login_t;
            }
            if (now_cal.get(Calendar.YEAR) != last_login_cal.get(Calendar.YEAR) ||
                    (now_cal.get(Calendar.YEAR) == last_login_cal.get(Calendar.YEAR)
                            && now_cal.get(Calendar.DAY_OF_YEAR) != last_login_cal.get(Calendar.DAY_OF_YEAR))
            ) {
                result.login_1st = true;
            } else {
                result.login_1st = false;
            }
            //========================================================
            //DataEyeSdk.Online(net_userbase.getGuid().toString());

            // 读取或创建关卡信息
            PlayerLevelInfo levelInfo = player_level_db.loadByUid(info._id);
            if (levelInfo == null) {
                levelInfo = (PlayerLevelInfo) player_level_db.createDB(info._id);
                levelInfo.cur_level = -1;
            }

            // 读取或创建背包信息
            PlayerBagInfo bagInfo = player_bag_db.loadByUid(info._id);
            if (bagInfo == null) {
                bagInfo = (PlayerBagInfo) player_bag_db.createDB(info._id);
            }
            SyncBagMsg bag = new SyncBagMsg(true);
            bag.items = bagInfo.items;
            SyncEquipBagMsg equip_bag = new SyncEquipBagMsg(true);
            equip_bag.add = bagInfo.equips;

            //活动
            ActivityHandleImpl.InitDB(info._id);

            // 创建初始的角色列表
            PlayerRolesInfo roles = player_roles_info_db.load(info._id);
            if (roles == null) {
                info.creat_t = now_cal.getTimeInMillis();
                result.is_new = true;
                roles = (PlayerRolesInfo) player_roles_info_db.createDB(info._id);
                roles.roles = new ArrayList<RoleInfo>();
                roles.pve_team = new HashMap<Integer, Integer>();
                roles.backup_info = new HashMap<Integer, Map<Integer, Integer>>();
                Config c = ConfigConstant.tConf;
                Map<Integer, Role> role_config = ConfigConstant.tRole;
                int j = 1;

                List<Integer> temp_c = new ArrayList<Integer>();
                for (int i : c.getIniPart()) {
                    temp_c.add(i);
                }
                for (int i = 0; i < c.getIniPet().length; ++i) {
                    Integer role_id = c.getIniPet()[i];
                    Role r = role_config.get(role_id);
                    if (null == r)
                        continue;
                    if (j <= 5 && temp_c.contains(role_id))
                        roles.pve_team.put(j++, role_id);
                    RoleInfo r_info = new RoleInfo();
                    r_info.InitByRoleConfigId(role_id);
                    EquipImpl.changeRoleEnchantId(r_info);
                    roles.roles.add(r_info);
                }

                if (null == mail_db.loadByUid(info._id))
                    mail_db.createDB(info._id);
            } else {
                int rs = roles.roles.size();
                Map<Integer, Role> role_config = ConfigConstant.tRole;
                List<RoleInfo> r_list = roles.roles;
                for (int i = 0; i < rs; ++i) {
                    RoleInfo r_info = r_list.get(i);
                    if (r_info.jewelries == null) {
                        r_info.jewelries = new ArrayList<JewelryInfo>();
                    }
                    Role r = role_config.get(r_info.role_id);
                    if (r_info.jewelries.size() != r.getJewelryId().length) {
                        int dif = r.getJewelryId().length - r_info.jewelries.size();
                        int ori = r_info.jewelries.size();
                        for (int j = 0; j < dif; ++j) {
                            JewelryInfo ji = new JewelryInfo();
                            ji.idx = j + ori;
                            r_info.jewelries.add(ji);
                        }
                    }
                }
            }

            if (null == month_db.loadByUid(info._id))
                month_db.createDB(info._id);

            PlayerGuildInfo pg_info = guild_db.loadByUid(info._id);
            if (null == pg_info)
                pg_info = (PlayerGuildInfo) guild_db.createDB(info._id);

            // 刷新所有人的属性
            List<Integer> calced = new ArrayList<Integer>();
            for (Entry<Integer, Map<Integer, Integer>> fu_jiang : roles.backup_info.entrySet()) {
                for (Entry<Integer, Integer> f : fu_jiang.getValue().entrySet()) {
                    RoleAttrCalc.RefreshRoleAttr(f.getValue(), roles);
                    calced.add(f.getValue());
                }
            }
            for (Entry<Integer, Integer> zhu_jiang : roles.pve_team.entrySet()) {
                RoleAttrCalc.RefreshRoleAttr(zhu_jiang.getValue(), roles);
                calced.add(zhu_jiang.getValue());
            }
            for (RoleInfo role : roles.roles) {
                if (calced.contains(role.role_id))
                    continue;
                RoleAttrCalc.RefreshRoleAttr(role.role_id, roles);
            }
            UpdateTeamFightingCheckMax(info, roles.CalcTeamFighting());
            GoldBuyImpl.Refresh(info);
            BossImpl.handleLogin(info._id);
            SnsImpl.handleLogin(info);

            player_info_db.save();
            player_roles_info_db.save();
            player_level_db.save();
            // 初始化用到的其他模块
            PveImpl pve_impl = PveImpl.getInstance().init(info._id);
            // 初始化下发信息
            result._id = info._id;//uid
            result.guid = info.user_base.getGuid();
            result.current_level_id = levelInfo.cur_level;
            result.diamond = info.diamond;
            result.gold = info.gold;
            result.icon_url = info.user_base.getuImg();
            result.team_current_fighting = info.team_current_fighting;
            result.team_exp = info.team_exp;
            result.team_history_max_fighting = info.team_history_max_fighting;
            result.team_lv = info.team_lv;
            result.username = info._id;
            result.nickname = info.user_base.getNickname();
            result.vip_level = info.vip_level;
            result.pve_offline = pve_impl.reqOfflineReward();// 离线收益信息
            result.roles = roles;
            result.bag = bag;
            result.equip_bag = equip_bag;
            result.acc_rmb = info.acc_rmb;
            result.vip_award = info.vip_award;
            result.last_gold_time = info.last_gold_time;
            result.gold_buy_cnt = info.gold_buy_cnt;
            result.fin_battle = info.drama_b_end;
            result.fin_dialog = info.drama_end;
            result.create_time = info.creat_t;
            result.guild_id = null;
            result.need_fl = info.user_base.getDisFollow();
            result.fl_award = info.fl_award;
            result.is_fl = info.user_base.getIsFollow();
            result.inv_cnt = info.user_base.getCurrDay();
            result.share_on = info.user_base.getDisShare();
            result.share_cnt = info.share_cnt;
            result.inv_award = info.inv_award;
            result.share_t = info.share_t;
            result.qq_cut = info.qq_cut;
            if (pg_info.gd_id.length() > 0) {
                result.guild_id = pg_info.gd_id;
            }
            // PVE模块登录响应
            pve_impl.onPlayerLogin(); // 调用PVE模块登陆响应
            // 回收用到的其他模块
            pve_impl.destroy();

            if (!result.need_create) {
                String to_login_svr_url = loginUrl + "modfy?uuid=" + info._id
                        + "&nickname=" + info.user_base.getNickname() + "&rolelevel=" + info.team_lv
                        + "&zid=" + info.user_base.zid + "&guid=" + info.user_base.getGuid() + "&gid=" + info.user_base.gid
                        + "&type=1";
                HttpRequest.GetFunction(to_login_svr_url);
            }
            log.info("user login net uid {} id:{}", net_userbase.getUid(), info._id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Destory();
        }
        return result;
    }

    // endregion

    // region VIP相关

    /**
     * 是否拥有月卡（是否在有效期内）
     */
    public boolean hasMonthCard() {
        long cur_time = Calendar.getInstance().getTimeInMillis();
        if (this.m_player_info.month_card_expired > cur_time) {
            return true;
        }
        return false;
    }

    /**
     * 是否拥有终生卡（是否在有效期内）
     *
     * @return
     */
    public boolean hasLifetimeCard() {
        long cur_time = Calendar.getInstance().getTimeInMillis();
        if (this.m_player_info.lifetime_card_expired > cur_time) {
            return true;
        }
        return false;
    }

    // endregion

    // region 金币操作

    /**
     * 增加金钱
     *
     * @param offset 增加值
     * @return 增加后金钱数
     */
    public double addGold(int offset) {
        if (offset < 0) {
            log.error("无法增加金钱" + offset + "，因为增加值是一个负数。");
            return 0;
        }
        m_player_info.gold += offset;
        player_info_db.save();
        return this.m_player_info.gold;
    }

    /**
     * 扣减金钱
     *
     * @param offset 扣减值
     * @return 扣减后金钱数
     */
    public double subGold(int offset) {
        if (offset < 0) {
            log.error("无法扣减金钱" + offset + "，因为扣减值是一个负数。");
            return 0;
        }

        if (offset > m_player_info.gold) {
            log.error("无法扣减金钱" + offset + "，因为没有足够的金钱，请先做判断。");
        }

        m_player_info.gold -= offset;
        player_info_db.save();
        return this.m_player_info.gold;
    }

    /**
     * 检查是否有足够的金币
     *
     * @param value 检查值
     * @return 是否足够
     */
    public boolean hasGold(int value) {
        if (this.m_player_info.gold >= value) {
            return true;
        }
        return false;
    }

    // endregion

    // region 钻石操作

    /**
     * 增加钻石
     *
     * @param offset 增加值
     * @return 增加后钻石数
     */
    public double addDiamond(int offset) {
        if (offset < 0) {
            log.error("无法增加钻石" + offset + "，因为增加值是一个负数。");
            return 0;
        }
        m_player_info.diamond += offset;
        player_info_db.save();
        return this.m_player_info.diamond;
    }

    /**
     * 增加钻石
     *
     * @param offset 扣减值
     * @return 增加后钻石数
     */
    public double subDiamond(int offset) {
        if (offset < 0) {
            log.error("无法扣减钻石" + offset + "，因为扣减值是一个负数。");
            return 0;
        }

        if (offset > m_player_info.diamond) {
            log.error("无法扣减钻石" + offset + "，因为没有足够的钻石，请先做判断。");
        }

        m_player_info.diamond -= offset;
        player_info_db.save();
        return this.m_player_info.diamond;
    }

    /**
     * 检查是否有足够的钻石
     *
     * @param value 检查值
     * @return 是否足够
     */
    public boolean hasDiamond(int value) {
        if (this.m_player_info.diamond >= value) {
            return true;
        }
        return false;
    }

    public static boolean addExp(PlayerInfo info, int offset) {
        if (null != info) {
            boolean result = info.addExp(offset);
            //限时礼包触发
            LvgiftImpl.DoLvUp(info);
            //主线
            TaskImpl.doTask(info._id, TaskType.HAS_PLAYER_LEVEL, info.team_lv);
            return result;
        }
        return false;
    }

    /**
     * 获取下级所需经验值
     *
     * @param lv
     * @return
     */
    public double getNextExp(int lv) {
        return ConfigConstant.tGrade.get(lv).getLvExp();
    }

    //更新队伍战力，如果队伍战力超过历史战力，返回true
    public static boolean UpdateTeamFightingCheckMax(PlayerInfo p, int new_fighting) {
        p.team_current_fighting = new_fighting;
        if (p.team_history_max_fighting < p.team_current_fighting) {
            p.team_history_max_fighting = p.team_current_fighting;
            //主线
            TaskImpl.doTask(p._id, TaskType.HAS_HISTORY_FIGHT, p.team_history_max_fighting);
            return true;
        }
        return false;
    }

    private void cleanAllMem(String uuid) {
        cleanMem(uuid, PlayerAccSpendDB.class);
        cleanMem(uuid, PlayerActivityDB.class);
        cleanMem(uuid, PlayerBagDB.class);
        cleanMem(uuid, PlayerBossDB.class);
        cleanMem(uuid, PlayerCheckinActivityDB.class);
        cleanMem(uuid, PlayerDailyTaskDB.class);
        cleanMem(uuid, PlayerDaypayDB.class);
        cleanMem(uuid, PlayerDmdplateDB.class);
        cleanMem(uuid, PlayerDmgRewardDB.class);
        cleanMem(uuid, PlayerDrawCardDB.class);
        cleanMem(uuid, PlayerDuelDB.class);
        cleanMem(uuid, PlayerDuelFightingDB.class);
        cleanMem(uuid, PlayerDuelTeamDB.class);
        cleanMem(uuid, PlayerDurationDB.class);
        cleanMem(uuid, PlayerExRoleGiftDB.class);
        cleanMem(uuid, PlayerGiftActivityDB.class);
        cleanMem(uuid, PlayerGkgrowActivityDB.class);
        cleanMem(uuid, PlayerGuildDB.class);
        cleanMem(uuid, PlayerInfoDB.class);
        cleanMem(uuid, PlayerInstanceDB.class);
        cleanMem(uuid, PlayerInvestActivityDB.class);
        cleanMem(uuid, PlayerLevelDB.class);
        cleanMem(uuid, PlayerLimitGiftDB.class);
        cleanMem(uuid, PlayerLimitSevenActivityDB.class);
        cleanMem(uuid, PlayerLineupDB.class);
        cleanMem(uuid, PlayerLvgrowActivityDB.class);
        cleanMem(uuid, PlayerMailDB.class);
        cleanMem(uuid, PlayerMonthcardDB.class);
        cleanMem(uuid, PlayerRolesInfoDB.class);
        cleanMem(uuid, PlayerScrollDB.class);
        cleanMem(uuid, PlayerSendDB.class);
        cleanMem(uuid, PlayerSendRcdDB.class);
        cleanMem(uuid, PlayerSevenActivityDB.class);
        cleanMem(uuid, PlayerShopDB.class);
        cleanMem(uuid, PlayerTaskDB.class);
        cleanMem(uuid, PlayerTowerDB.class);
        cleanMem(uuid, PlayerTurnplateDB.class);
        cleanMem(uuid, PlayerVipBenefitDB.class);
        cleanMem(uuid, PlayerVipGiftDB.class);
        cleanMem(uuid, PlayerRoleInvestDB.class);
        cleanMem(uuid, PlayerEnchantInvestDB.class);
        cleanMem(uuid, PlayerJewelryInvestDB.class);
        cleanMem(uuid, PlayerWishDB.class);
    }

    private void cleanMem(String uuid, Class<?> cls) {
        BaseDataSource x = (BaseDataSource) SpringContextUtil.getBean(cls);
        x.CleanMem(uuid);
        x = null;
    }

    /**
     * 扣减钻石
     *
     * @param offset 扣减值
     * @return 扣减后钻石数
     */
    public static double SubDiamond(PlayerInfo info, double offset) {
        double result = 0.0;
        if (info != null) {
            result = info.subDiamond(offset);

            PlayerAccSpendDB as_db = SpringContextUtil.getBean(PlayerAccSpendDB.class);
            PlayerAccSpendInfo as_info = as_db.loadByUid(info._id);
            if (as_info != null) {
                as_info.acc_spend += offset;
                as_info.last_t = Calendar.getInstance().getTimeInMillis();
                as_db.save();
            }
            as_db = null;
            as_info = null;
        }
        return result;
    }
    // endregion
}
