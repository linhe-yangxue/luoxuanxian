package com.ssmGame.module.pve;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Dailytask;
import com.ssmData.config.entity.Level;
import com.ssmData.config.entity.Role;
import com.ssmData.config.entity.Vip;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerDurationDB;
import com.ssmData.dbase.PlayerDurationInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerLevelDB;
import com.ssmData.dbase.PlayerLevelInfo;
import com.ssmData.dbase.PlayerMonthcardDB;
import com.ssmData.dbase.PlayerMonthcardInfo;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmData.dbase.RoleInfo;
import com.ssmGame.defdata.msg.battle.BattleResultMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.defdata.msg.pve.PveBattleResultMsg;
import com.ssmGame.defdata.msg.pve.PveBossResultMsg;
import com.ssmGame.defdata.msg.pve.PveInfoMsg;
import com.ssmGame.defdata.msg.pve.PveOfflineMsg;
import com.ssmGame.defdata.msg.pve.PveQuickMsg;
import com.ssmGame.defdata.msg.pve.PveRewardMsg;
import com.ssmGame.defdata.msg.pve.PveTreasureMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.activity.ActivityType;
import com.ssmGame.module.battle.BattlePack;
import com.ssmGame.module.battle.BattleResult;
import com.ssmGame.module.battle.BattleSimulator;
import com.ssmGame.module.battle.BattleType;
import com.ssmGame.module.daily.DailyTaskImpl;
import com.ssmGame.module.daily.DailyTaskType;
import com.ssmGame.module.dmgreward.DmgRewardImpl;
import com.ssmGame.module.monthcard.MonthcardImpl;
import com.ssmGame.module.player.PlayerImpl;
import com.ssmGame.module.task.TaskImpl;
import com.ssmGame.module.task.TaskType;
import com.ssmGame.util.AwardUtils;
import com.ssmGame.util.ItemCountPair;
import com.ssmShare.constants.E_PayType;

/**
 * 关卡系统
 * Created by WYM on 2016/11/2.
 */
@Service
@Scope("prototype")
public class PveImpl {

    @Autowired
    private PlayerLevelDB m_player_level_db;
    private PlayerLevelInfo m_player_level = null;

    @Autowired
    private PlayerRolesInfoDB m_player_roles_db;
    private PlayerRolesInfo m_player_roles = null;

    @Autowired
    private PlayerInfoDB m_player_info_db;
    private PlayerInfo m_player_info = null;
    
    @Autowired
    private PlayerBagDB m_bag_db;
    private PlayerBagInfo m_bag = null;

    private static final Logger log = LoggerFactory.getLogger(PveImpl.class);
    private static final int bossLimit = 1;

    // region 对象生命周期

    /**
     * 初始化
     */
    public PveImpl init(String uid){

        // 加载依赖的数据
        m_player_level = m_player_level_db.loadByUid(uid);
        m_player_roles = m_player_roles_db.load(uid);
        m_player_info = m_player_info_db.loadById(uid);
        m_bag = m_bag_db.loadByUid(uid);

        // 角色判空
        if(m_player_info == null){
            log.error("无法初始化PlayerImpl，找不到角色 " + uid);
            return null;
        }

        return this;
    }

    /**
     * 销毁
     */
    public void destroy()
    {
        m_player_level = null;
        m_player_level_db = null;

        m_player_roles = null;
        m_player_roles_db = null;

        m_player_info = null;
        m_player_info_db = null;
        
		m_bag_db = null;
		m_bag = null;
    }

    /**
     * 获取实例
     * @return PveImpl的实例
     */
    public static PveImpl getInstance(){
        return SpringContextUtil.getBean(PveImpl.class);
    }

    // endregion

    // region PVE战斗

    /**
     * 响应用户登录刷新
     */
    public void onPlayerLogin(){
        // 清空喜从天降奖励
        this.clearTreasure();
    }

    /**
     * 请求发起普通战斗
     */
    public PveBattleResultMsg reqPveBattle(){
    	
        if(m_player_level.cur_level <= 0) {
            this.getPveInfo();
        }

        //log.info("收到普通战斗请求");

        // 读取Level信息
        int cur_level = this.m_player_level.cur_level;
        //log.info("当前的关卡：" + cur_level);
        //Level level_info = ConfigConstant.tLevel.get(cur_level);

        // 生成双方阵容
        Map<Integer, RoleInfo> my_roles = this.genMyRoles();
        Map<Integer, RoleInfo> ene_roles = this.genEnemyRoles(cur_level);

        int bc_t = ConfigConstant.tConf.getBCTime();
        if (m_player_level.cur_level >= ConfigConstant.tConf.getBCTimelv()) {
        	bc_t = ConfigConstant.tConf.getBCTimelast();
        }
        // 模拟PVE战斗
        BattleSimulator sim = new BattleSimulator();
        sim.InitAllActorLogic(my_roles, ene_roles, ConfigConstant.tConf.getBattlelimit(), bc_t, BattleType.LEVEL_NORMAL
        		, null, null, null, null);
        BattlePack battle_result = sim.Exe();

        // 战斗胜利 记录奖励
        if(battle_result.m_result == BattleResult.LEFT_WIN){

            // 记录可领取的奖励，生成奖励hash
            this.m_player_level.reward_hash = RandomStringUtils.randomAlphabetic(16);
            this.m_player_level.reward_is_boss = false;

        } else {
            // 清空可领取奖励
            this.m_player_level.reward_hash = "";
            this.m_player_level.reward_is_boss = false;
        }

        // 记录最后战斗时间
        this.m_player_info.last_active_time = Calendar.getInstance().getTimeInMillis();

        // 保存进度
        this.m_player_info_db.save();
        this.m_player_level_db.save();

        // 返回结果
        PveBattleResultMsg result = new PveBattleResultMsg();
        result.is_legal = battle_result.m_result == BattleResult.LEFT_WIN;
        result.script = new BattleResultMsg();
        result.script.pack = battle_result;
        result.reward_hash = this.m_player_level.reward_hash;
        //log.info("战斗完成，胜利：{}，奖励哈希：{}", result.is_legal, result.reward_hash);

        return result;

    }

    /**
     * 请求挑战Boss
     */
    public PveBossResultMsg reqBossBattle(CommonMsg respond){

    	/*String ip = "192.168.1.252";
    	String port = "9992";
    	try {
    		CommonMsg send = new CommonMsg(1, "");
    		String result = HttpRequest.PostFunction("http://"+ip + ":" + port + I_DefMoudle.HUB_GS2HUB_HELLO, JsonTransfer.getJson(send));
    		CommonMsg res = JsonTransfer._In(result, CommonMsg.class);
    		System.out.println(res.body.playerLogin.icon_url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	
        // 判断是否有挑战BOSS的条件
        if(m_player_level.cur_level > ConfigConstant.tConf.getRushBoss() 
        		&& this.m_player_level.enemy_killed < PveImpl.bossLimit){
            // Log.print("本关未胜利2场以上，不能挑战BOSS");
            return null;
        }
        
        //日常任务
        Dailytask d_cfg = DailyTaskImpl.getCfg(DailyTaskType.PVE_BOSS);
        int add_result = DailyTaskImpl.AddDaily(d_cfg, 1, m_player_roles.player_info_id);
        if (d_cfg != null && add_result != 0)
        {
        	int id = ConfigConstant.tConf.getCharmItem();
        	int cnt = d_cfg.getTaskReward() * add_result;
        	m_bag.addItemCount(id, cnt);
        	m_bag_db.save();
        	respond.body.sync_bag = new SyncBagMsg();
        	respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
        }

        //log.info("收到BOSS战斗请求");

        // 读取Level信息
        int cur_level = this.m_player_level.cur_level;
        Level level_info = ConfigConstant.tLevel.get(cur_level);

        // 生成双方阵容
        Map<Integer, RoleInfo> my_roles = this.genMyRoles();
        Map<Integer, RoleInfo> ene_roles = this.genBossRoles(cur_level);

        int bc_t = ConfigConstant.tConf.getBCTime();
        if (m_player_level.cur_level >= ConfigConstant.tConf.getBCTimelv()) {
        	bc_t = ConfigConstant.tConf.getBCTimelast();
        }
        // 模拟PVE战斗
        BattleSimulator sim = new BattleSimulator();
        sim.InitAllActorLogic(my_roles, ene_roles, level_info.getTime(), bc_t, BattleType.LEVEL_BOSS
        		, null, null, null, null);
        BattlePack battle_result = sim.Exe();
        DmgRewardImpl.RefreshMax(m_player_info._id, battle_result);

        // 记录可领取的奖励，生成奖励hash
        if(battle_result.m_result == BattleResult.LEFT_WIN){
            this.m_player_level.reward_hash = RandomStringUtils.randomAlphabetic(16);
            this.m_player_level.reward_is_boss = true;
        } else {
            // 清空可领取奖励
            this.m_player_level.reward_hash = "";
            this.m_player_level.reward_is_boss = false;
        }

        // 记录最后战斗时间
        this.m_player_info.last_active_time = Calendar.getInstance().getTimeInMillis();

        // 保存进度
        this.m_player_info_db.save();
        this.m_player_level_db.save();

        // 返回结果
        PveBossResultMsg result = new PveBossResultMsg();
        result.is_legal = battle_result.m_result == BattleResult.LEFT_WIN;
        result.script = new BattleResultMsg();
        result.script.pack = battle_result;
        result.reward_hash = this.m_player_level.reward_hash;

        //log.info("BOSS战斗完成，胜利：{}，奖励哈希：{}", result.is_legal, result.reward_hash);

        return result;

    }

    /**
     * 请求领取战斗奖励
     */
    public CommonMsg reqReward(CommonMsg com_msg, String reward_hash){

        // 奖励hash为空 不允许领取
        if (reward_hash.trim().isEmpty() || this.m_player_level.reward_hash.trim().isEmpty()) {
            return com_msg;
        }

        // 检查奖励hash
        if(!this.m_player_level.reward_hash.trim().equals(reward_hash.trim())){
            // log.info("Pve reqReward hash incorrect, uid: {}, hash: {}", this.m_player_info._id, reward_hash.trim());
            //log.error("奖励hash不一致");
            //log.error("记录的hash：" + this.m_player_level.reward_hash);
            //log.error("传来的hash：" + reward_hash);
            return com_msg;
        }


        // 读取Level信息
        Level level_info = ConfigConstant.tLevel.get(this.m_player_level.cur_level);

        boolean r_is_boss = this.m_player_level.reward_is_boss;


        // 只在非BOSS挑战时检查时间合法性
        if(!r_is_boss) {
            int bc_t = ConfigConstant.tConf.getBCTime();
            if (m_player_level.cur_level >= ConfigConstant.tConf.getBCTimelv()) {
            	bc_t = ConfigConstant.tConf.getBCTimelast();
            }
            long last_battle_time = this.m_player_info.last_active_time;
            long threshold_time = ConfigConstant.tConf.getBattletime() + bc_t - 2000;
            long elapsed_time = Calendar.getInstance().getTimeInMillis() - last_battle_time;

            // System.out.print(((Calendar.getInstance().getTimeInMillis() - last_battle_time < threshold_time) ? "不" : "") + "合法：" + "{ elapsed: " + (Calendar.getInstance().getTimeInMillis() - last_battle_time) + " , min: " + threshold_time + " }\r\n");

            if(elapsed_time < threshold_time) {
                com_msg.header.rt_sub = MsgCode.PVE_ILLEGAL;
                return com_msg;
            }
        }

        // 发放金币和经验值奖励
        CardFactor cf = getCardFactor(false);
        int r_gold = r_is_boss ? level_info.getGold()[1] : level_info.getGold()[0];
        r_gold = (int)Math.floor(r_gold * ((1000.0 + cf.month_gold + cf.life_gold)/ 1000.0));
        m_player_info.addGold(r_gold);
        int r_exp = r_is_boss ? level_info.getExp()[1] : level_info.getExp()[0];
        r_exp = (int)Math.floor(r_exp * ((1000.0 + cf.month_exp + cf.life_exp)/ 1000.0));

        // 仅在未达最高等级限制时提升经验值
        boolean is_levelup = false;
        if (m_player_info.team_lv < ConfigConstant.tConf.getLevelCap()) {
            is_levelup = PlayerImpl.addExp(m_player_info, r_exp);
        } else {
            r_exp = 0;
        }

        // 生成物品奖励
        int r_reward_id = r_is_boss ? level_info.getAward() : 0;
        SyncBagItem[] r_bag_items = new SyncBagItem[0];

        SyncBagMsg sync_bag = new SyncBagMsg(false);

        if(r_reward_id > 0){

            List<ItemCountPair> r_bag = AwardUtils.GetAward(r_reward_id);
            r_bag_items = new SyncBagItem[r_bag.size()];

            for(int i = 0; i < r_bag.size(); i++){
            	if (m_bag.addItemCount(r_bag.get(i).m_item_id, r_bag.get(i).m_count))
            	{
                    SyncBagItem item = new SyncBagItem();
                    item.id = r_bag.get(i).m_item_id;
                    item.count = r_bag.get(i).m_count;

                    // 背包物品入库
                    r_bag_items[i] = item;
                    sync_bag.items.put(item.id, this.m_bag.getItemCount(item.id));
            	}
            }
            m_bag_db.save();
        }

        // BOSS战胜利，推进关卡进度
        if(r_is_boss){
            // 如果没打到最后一关，就推进关卡进度
            if(this.m_player_level.cur_level < ConfigConstant.tLevel.size()){
            	int pass_level = this.m_player_level.cur_level;
                this.m_player_level.cur_level += 1;
                this.m_player_level.time = Calendar.getInstance().getTimeInMillis();
                this.m_player_level.enemy_killed = 0;
                
                //主线任务
                TaskImpl.doTask(m_player_level.uid, TaskType.HAS_PVE, pass_level);
            }
        }else{
            // 普通战斗胜利，敌人击杀数+1
            this.m_player_level.enemy_killed += 1;
        }

        // 重置数据记录
        m_player_level.reward_hash = "";
        m_player_level.reward_is_boss = false;
        m_player_level_db.save();

        // 返回奖励信息
        PveRewardMsg result = this.getBaseRewardMsg();
        result.exp = r_exp;
        result.gold = r_gold;
        result.diamond = 0;
        result.reward_hash = reward_hash;
        result.fly_treasure = this.updateTreasure();
        result.bag_items = r_bag_items;

        com_msg.body.pveReward = result;
        com_msg.body.sync_bag = sync_bag;

        // 如果升级 返回升级消息
        if(is_levelup){
            com_msg.header.rt_msg = 10001;
            com_msg.body.sync_player_info = this.getSyncPlayerInfoMsg();
        }else{
            com_msg.body.sync_player_info = new SyncPlayerInfoMsg(false);
            com_msg.body.sync_player_info.exp = r_exp;
            com_msg.body.sync_player_info.gold = r_gold;
        }

        return com_msg;

    }

    /**
     * 获取基本的奖励信息
     * @return 基本奖励信息
     */
    public PveRewardMsg getBaseRewardMsg(){
        PveRewardMsg result = new PveRewardMsg();
        result.enemy_killed = this.m_player_level.enemy_killed;
        result.cur_level = this.m_player_level.cur_level;
        return result;
    }

    /**
     * 获取PVE基本信息
     * @return PVE基本信息
     */
    public PveInfoMsg getPveInfo(){

        if(m_player_level.cur_level <= 0){
            m_player_level.cur_level = ConfigConstant.tConf.getInitialLevel();
            m_player_level_db.save();
        }

        CardFactor cf = getCardFactor(false);

        PveInfoMsg result = new PveInfoMsg();
        Level level_info = ConfigConstant.tLevel.get(m_player_level.cur_level);
        result.cur_level = level_info.getID();
        result.gold_per_hour = 60 * 60 / ConfigConstant.tConf.getOffCTime() * (int)Math.floor(level_info.getGold()[0] * ((1000.0 + cf.month_gold + cf.life_gold)/ 1000.0));
        result.exp_per_hour = 60 * 60 / ConfigConstant.tConf.getOffCTime() * (int)Math.floor(level_info.getExp()[0] * ((1000.0 + cf.month_exp + cf.life_exp)/ 1000.0));
        result.enemy_killed = m_player_level.enemy_killed;
        result.all_quick_count = m_player_level.all_quick_count;
        result.today_quick_count = this.getQuickCount();
        result.today_quick_limit = this.getQuickLimit();
        result.auto_avaliable = this.authAuto();
        result.quick_next_price = this.getQuickNextPrice();

        return result;
    }

    // endregion

    // region 玩家信息

    public CommonMsg reqPlayerInfo(CommonMsg com_msg) {

        SyncPlayerInfoMsg s_player = new SyncPlayerInfoMsg(true);

        s_player.team_lv = m_player_info.team_lv;
        s_player.gold = m_player_info.gold;
        s_player.diamond = m_player_info.diamond;
        s_player.exp = m_player_info.team_exp;
        s_player.team_current_fighting = m_player_info.team_current_fighting;
        s_player.team_history_max_fighting = m_player_info.team_history_max_fighting;
        s_player.vip_level = m_player_info.vip_level;
        s_player.acc_rmb = m_player_info.acc_rmb;
        s_player.vip_award = m_player_info.vip_award;
        s_player.last_gold_time = m_player_info.last_gold_time;
        s_player.gold_buy_cnt = m_player_info.gold_buy_cnt;

        com_msg.body.sync_player_info = s_player;
        return com_msg;
    }

    // endregion

    // region 快速战斗

    /**
     * 请求发起快速战斗
     */
    public CommonMsg reqQuick(CommonMsg com_msg){

        // 获取权限信息
        int daily_quick_limit = this.getQuickLimit();
        int today_quick_count = this.getQuickCount();

        // 检查当日次数
        if(today_quick_count >= daily_quick_limit && daily_quick_limit >= 0){
            // 已经超出每日限制，做出提示
        	com_msg.header.rt_sub = 1101;
        	com_msg.body.pveQuick = new PveQuickMsg();
        	com_msg.body.pveQuick.success = false;
            return com_msg;
        }

        // 生成价格信息
        int quick_price = this.getQuickNextPrice();

        // 扣除价格
        PlayerImpl playerImpl = PlayerImpl.getInstance().init(m_player_info._id);

        // TODO 钻石不足
        if(!playerImpl.hasDiamond(quick_price)){
            //log.error("钻石不足");
            com_msg.header.rt_sub = 1005;
            com_msg.body.pveQuick = new PveQuickMsg();
            com_msg.body.pveQuick.success = false;
            return com_msg;
        }

        // 扣除钻石
        PlayerImpl.SubDiamond(m_player_info, quick_price);

        // 计算收益时间
        int quick_time = (ConfigConstant.tConf.getQCITime() + m_player_level.all_quick_count * 60);
        if(quick_time > ConfigConstant.tConf.getQCULTime()){
            quick_time = ConfigConstant.tConf.getQCULTime();
        }

        // 计算具体收益
        Level level_info = ConfigConstant.tLevel.get(m_player_level.cur_level);
        CardFactor cf = getCardFactor(false);
        int r_gold = quick_time / ConfigConstant.tConf.getOffCTime() * level_info.getGold()[0];
        r_gold = (int)Math.floor(r_gold * ((1000.0 + cf.month_gold + cf.life_gold)/ 1000.0));
        int r_exp = quick_time / ConfigConstant.tConf.getOffCTime() * level_info.getExp()[0];
        r_exp = (int)Math.floor(r_exp * ((1000.0 + cf.month_exp + cf.life_exp)/ 1000.0));
        // 发放具体收益
        m_player_info.addGold(r_gold);
        boolean is_levelup = PlayerImpl.addExp(m_player_info, r_exp);

        // 更新次数累计变化
        m_player_level.today_quick_count += 1;
        m_player_level.all_quick_count += 1;

        // 保存变更
        m_player_info_db.save();
        m_player_level_db.save();
        playerImpl.Destory();

        // 通知收益变化
        PveQuickMsg result = new PveQuickMsg();
        result.success = true;
        result.gold = r_gold;
        result.exp = r_exp;
        result.time = quick_time;
        result.today_quick_count = m_player_level.today_quick_count;
        result.all_quick_count = m_player_level.all_quick_count;
        result.quick_next_price = this.getQuickNextPrice();

        com_msg.body.pveQuick = result;

        // 如果升级 返回升级消息
        if(is_levelup){
            com_msg.header.rt_sub = 10001;
            com_msg.body.sync_player_info = this.getSyncPlayerInfoMsg();
        }else{
            com_msg.body.sync_player_info = new SyncPlayerInfoMsg(false);
            com_msg.body.sync_player_info.exp = r_exp;
            com_msg.body.sync_player_info.gold = r_gold;
            com_msg.body.sync_player_info.diamond = -quick_price;
        }
        
    	//主线
        TaskImpl.doTask(com_msg.header.uid, TaskType.QUICK_BATTLE_CNT, 1);
        
        //日常任务
        Dailytask d_cfg = DailyTaskImpl.getCfg(DailyTaskType.QUICK_PVE);
        int add_result = DailyTaskImpl.AddDaily(d_cfg, 1, m_player_roles.player_info_id);
        if (d_cfg != null && add_result != 0)
        {
        	int id = ConfigConstant.tConf.getCharmItem();
        	int cnt = d_cfg.getTaskReward() * add_result;
        	m_bag.addItemCount(id, cnt);
        	m_bag_db.save();
        	com_msg.body.sync_bag = new SyncBagMsg();
        	com_msg.body.sync_bag.items.put(id, m_bag.getItemCount(id));
        }

        return com_msg;

    }

    /**
     * 获取快速战斗次数（自动刷新）
     */
    private int getQuickCount(){

        // 生成今日和明日零点时间
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long todayZero = cal.getTimeInMillis();
        long offsetTime = ConfigConstant.tConf.getRTime() * 60 * 60 * 1000;

        long todayRefershTime = todayZero + offsetTime;

        // 过期则重置
        if(!(m_player_level.last_quick_time > todayRefershTime)){
            m_player_level.today_quick_count = 0;
            m_player_level.last_quick_time = Calendar.getInstance().getTimeInMillis();
            m_player_level_db.save();
        }

        return m_player_level.today_quick_count;
    }

    /**
     * 获取快速战斗每日购买次数限制
     * @return 快速战斗每日购买次数
     */
    private int getQuickLimit(){
        int my_vip_lv = m_player_info.vip_level;
        Vip vip_info = ConfigConstant.tVip.get(my_vip_lv);

        return vip_info.getQuickB();
    }

    /**
     * 获取下次快速战斗价格
     * @return 下次快速战斗价格
     */
    private int getQuickNextPrice(){
        int quick_price = ConfigConstant.tConf.getQCPrice();
        if(m_player_level.today_quick_count > 0){
            quick_price = quick_price + ConfigConstant.tConf.getPriceM() * (m_player_level.today_quick_count);
        }
        return quick_price;
    }

    // endregion

    // region 自动挑战

    /**
     * 检查是否拥有自动挑战权限
     * @return 是否有权限
     */
    private boolean authAuto(){

        //int req_vip_lv = ConfigConstant.tConf.getSMCVIPlv();
        //int req_cur_level = ConfigConstant.tConf.getSMCLevel();

        //int my_vip_lv = m_player_info.vip_level;
        //int my_cur_level = m_player_level.cur_level;

        return true;//(my_vip_lv >= req_vip_lv) || (my_cur_level > req_cur_level);

    }

    // endregion

    // region 喜从天降

    /**
     * 领取喜从天降奖励
     */
    public CommonMsg reqTreasure(CommonMsg rec_msg){

        // 如果未达到领取条件 返回失败
        if(!m_player_level.treasure_avaliable){
            rec_msg.body.pveTreasure = new PveTreasureMsg();
            return rec_msg;
        }

        // 重置喜从天降状态
        this.clearTreasure();

        // 生成奖励信息
        int r_gold = ConfigConstant.tLevel.get(m_player_level.cur_level).getGold()[0] * ConfigConstant.tConf.getPSGold();

        // 发放奖励
        this.m_player_info.addGold(r_gold);
        this.m_player_info_db.save();
        //PlayerImpl p = PlayerImpl.getInstance().init(m_player_info._id);
        //p.addGold(r_gold);

        // 通知收益
        PveTreasureMsg result = new PveTreasureMsg();
        result.gold = r_gold;

        // 写入主消息
        rec_msg.body.pveTreasure = result;

        // 通知金币变化
        rec_msg.body.sync_player_info = new SyncPlayerInfoMsg(false);
        rec_msg.body.sync_player_info.gold = r_gold;

        return rec_msg;

    }

    /**
     * 清空喜从天降状态
     */
    private void clearTreasure(){

        m_player_level.treasure_avaliable = false;
        m_player_level.treasure_count = 0;
        m_player_level_db.save();

    }

    /**
     * 刷新喜从天降状态
     * @return 当前喜从天降奖励是否可领取
     */
    private boolean updateTreasure(){
        // 如果已经可领取，则不处理
        if(m_player_level.treasure_avaliable){
            return true;
        }

        // 喜从天降计数器+1
        this.m_player_level.treasure_count += 1;

        // TODO 算法和策划给的不完全一致 可能引起非线性变化(概率向大数倾斜) 需要确认
        int target_count = randInt(ConfigConstant.tConf.getPSTime()[0], ConfigConstant.tConf.getPSTime()[1]);
        if(this.m_player_level.treasure_count >= target_count){
            this.m_player_level.treasure_avaliable = true;
        }

        // 保存喜从天降进度
        this.m_player_level_db.save();

        return m_player_level.treasure_avaliable;
    }

    // endregion

    // region 离线收益

    /**
     * 请求离线奖励
     */
    public PveOfflineMsg reqOfflineReward(){

        // 获取玩家信息
        //boolean is_vip = m_player_info.vip_level > 0;

        if(this.m_player_level.cur_level <= 0){
            return new PveOfflineMsg();
        }

        // 计算离线时长
        long last_battle_time = this.m_player_info.last_active_time;
        long cur_time = Calendar.getInstance().getTimeInMillis();
        long offline_time = (cur_time - last_battle_time) / 1000; // 离线时间（单位：秒）

        // 时长超出限制 取最大允许时长
        if(offline_time > ConfigConstant.tConf.getOffULTime()){
            offline_time = ConfigConstant.tConf.getOffULTime();
        }

        // 计算离线收益
        // 根据策划需求修改公式 modified by WYM 2017/02/17
        CardFactor cf = getCardFactor(true);
        Level level_info = ConfigConstant.tLevel.get(this.m_player_level.cur_level);
        int r_off_times = (int)offline_time / ConfigConstant.tConf.getOffCTime(); // 离线战斗场次

        // 基本收益
        int r_exp_ori = (int)(level_info.getExp()[0] * r_off_times * (ConfigConstant.tConf.getMultiple() / 1000.0));
        int r_gold_ori = (int)(level_info.getGold()[0] * r_off_times * (ConfigConstant.tConf.getMultiple() / 1000.0));

        // 月卡额外收益
        int r_exp_month = (int)((level_info.getExp()[0] * r_off_times * cf.month_exp) / 1000.0);
        int r_gold_month = (int)((level_info.getGold()[0] * r_off_times * cf.month_gold) / 1000.0);

        // 终生卡额外收益
        int r_exp_life = (int)(level_info.getExp()[0] * r_off_times * cf.life_exp / 1000.0);
        int r_gold_life = (int)(level_info.getGold()[0] * r_off_times * cf.life_gold / 1000.0);

        // 最终收益
        int r_exp_sp = r_exp_ori + r_exp_month + r_exp_life;
        int r_gold_sp = r_gold_ori + r_gold_month + r_gold_life;

        // 发放离线收益
        if (r_gold_sp >= 0) {
            m_player_info.gold += r_gold_sp;
        }
        PlayerImpl.addExp(m_player_info, r_exp_sp);
        
        //在线活动
        if (ActivityType.checkActivityOpen(ActivityType.Duration, cur_time, m_player_info)) {
    		PlayerDurationDB du_db = SpringContextUtil.getBean(PlayerDurationDB.class);
    		PlayerDurationInfo du_info = du_db.loadByUid(m_player_info._id);
    		if (du_info != null) {
    	    	Calendar now = Calendar.getInstance();
    	    	int now_day = now.get(Calendar.DAY_OF_YEAR);
    	    	Calendar last = Calendar.getInstance();
    	    	last.setTimeInMillis(du_info.last_t);
    	    	int last_day = last.get(Calendar.DAY_OF_YEAR);
    	    	if (last_day == now_day && du_info.last_t < m_player_info.last_active_time) {
    	    		du_info.duration += m_player_info.last_active_time - du_info.last_t;
    	    		du_info.last_t = cur_time;
    	    		du_db.save();
    	    	}
    		}
        }

        // 记录最后战斗时间
        this.m_player_info.last_active_time = cur_time;

        // 保存数据
        this.m_player_info_db.save();

        // 消息内容：离线时长、经验收益、金币收益、背包变动
        PveOfflineMsg result = new PveOfflineMsg();
        result.offline_time = offline_time;
        result.base_exp = r_exp_ori;
        result.base_gold = r_gold_ori;
        result.sp_exp = r_exp_sp;
        result.sp_gold = r_gold_sp;
        result.cur_level = m_player_level.cur_level;
        result.win_count = (int)offline_time / ConfigConstant.tConf.getOffCTime();
        result.lose_count = 0;
        result.sp_month_exp = r_exp_month;
        result.sp_month_gold = r_gold_month;
        result.sp_life_exp = r_exp_life;
        result.sp_life_gold = r_gold_life;
        return result;

    }

    /**
     * 获取离线收益加成倍数
     */
     //
     /*private int getOfflineMultiple(int type){

        // 获取用户信息
        PlayerImpl p = PlayerImpl.getInstance().init(this.m_player_info._id);

        // 终生卡
        if(p.hasLifetimeCard()){
            return ConfigConstant.tConf.getMultiple()[2] / 1000;
        }

        // 月卡
        if(p.hasMonthCard()){
            return ConfigConstant.tConf.getMultiple()[1] / 1000;
        }
    	 
    	int month = 0;
    	int life = 0;
    	
    	PlayerMonthcardDB db = SpringContextUtil.getBean(PlayerMonthcardDB.class);
    	PlayerMonthcardInfo i = db.loadByUid(this.m_player_info._id);
    	if (i != null)
    	{
    		MonthcardImpl.Refresh(i, Calendar.getInstance().getTimeInMillis());
    		if (i.hasType(MonthcardType.Month))
    		{
    			month = ConfigConstant.tMonthcard.get(MonthcardType.Month).getOffLine()[type];
    		}
    		if (i.hasType(MonthcardType.Life))
    		{
    			life = ConfigConstant.tMonthcard.get(MonthcardType.Life).getOffLine()[type];
    		}
    	}

        return (ConfigConstant.tConf.getMultiple()[0] + month + life) / 1000;

    }*/

    // endregion

    // region 辅助功能

    /**
     * 构造我方阵容
     * @return 角色阵容
     */
    private Map<Integer, RoleInfo> genMyRoles(){
        Map<Integer, Integer> pve_team_ids = m_player_roles.pve_team;
        Map<Integer, RoleInfo> pve_team = new HashMap<>();

        // 找到详细阵容
        for(Map.Entry<Integer, Integer> e : pve_team_ids.entrySet()){
            int role_id = e.getValue();
            m_player_roles.roles.stream().filter(info -> info.role_id == role_id).forEach(info -> {
                RoleInfo put = pve_team.put(e.getKey(), info);
            });
        }

        return pve_team;
    }

    /**
     * 构造敌方阵容
     * @param level 关卡id
     * @return 怪物阵容
     */
    private Map<Integer, RoleInfo> genEnemyRoles(int level){

        // 构造返回内容
        Map<Integer, RoleInfo> enemy_team = new HashMap<>();

        // 读取关卡信息
        Level level_info = ConfigConstant.tLevel.get(level);
        int ene_pool_size = level_info.getMon().length;

        // 计算怪物数量
        int min_count = level_info.getMonNum()[0];
        int max_count = level_info.getMonNum()[1];
        int ene_count = this.randInt(min_count, max_count);

        // 生成怪物
        for(int i = 0; i < ene_count; i++){

            int ene_pool_id = this.randInt(0, ene_pool_size - 1);
            int ene_role_id = level_info.getMon()[ene_pool_id];
            int ene_role_lv = level_info.getMonLv()[ene_pool_id];

            Role role_cfg = ConfigConstant.tRole.get(ene_role_id);
            if(role_cfg == null) {
                log.error("PveImpl genEnemyRoles() no role cfg, level: {}, role:{}", level, ene_role_id);
                continue;
            }

            RoleInfo role_info = new RoleInfo();

            role_info.InitByRoleConfigIdAndLv(ene_role_id, ene_role_lv);
            role_info.base_lv = 1; // 普攻等级
            role_info.skill_lv = ConfigConstant.tRole.get(ene_role_id).getSkillLv(); // 特殊技能等级

            enemy_team.put(i + 1, role_info);
            //Log.print(role_info.role_id);
        }

        return enemy_team;

    }

    /**
     * 构造BOSS阵容
     * @param level 关卡id
     * @return 怪物阵容
     */
    private Map<Integer, RoleInfo> genBossRoles(int level){

        // 构造返回内容
        Map<Integer, RoleInfo> enemy_team = new HashMap<>();

        // 读取关卡信息
        Level level_info = ConfigConstant.tLevel.get(level);

        for(int i = 0; i < 5; i++){
            RoleInfo role_info = new RoleInfo();
            int ene_role_id = level_info.getBoss()[i];

            // 该位置上没有怪物
            if(ene_role_id <= 0){
                //enemy_team.put(i, null); // 没有怪物则不填入数据
                continue;
            }

            int ene_role_lv = level_info.getBossLv()[i];
            role_info.InitByRoleConfigIdAndLv(ene_role_id, ene_role_lv);
            //role_info.lv = ene_role_lv; // 怪物等级
            role_info.base_lv = 1; // 普攻等级
            role_info.skill_lv = ConfigConstant.tRole.get(ene_role_id).getSkillLv(); // 特殊技能等级

            enemy_team.put(i + 1, role_info);
        }

        return enemy_team;

    }

    /**
     * 生成随机整数
     * @param min 最小值
     * @param max 最大值
     * @return 随机整数
     */
    private int randInt(int min, int max){
        return (int)Math.round(Math.random() * (max - min) + min);
    }

    /**
     * 生成玩家信息消息
     * @return 玩家信息同步消息
     */
    private SyncPlayerInfoMsg getSyncPlayerInfoMsg(){
        SyncPlayerInfoMsg sync_player_info = new SyncPlayerInfoMsg(true);
        sync_player_info.gold = m_player_info.gold;
        sync_player_info.diamond = m_player_info.diamond;
        sync_player_info.exp = m_player_info.team_exp;
        sync_player_info.team_lv = m_player_info.team_lv;
        sync_player_info.team_current_fighting = m_player_info.team_current_fighting;
        sync_player_info.vip_level = m_player_info.vip_level;
        sync_player_info.last_gold_time = m_player_info.last_gold_time;
        sync_player_info.gold_buy_cnt = m_player_info.gold_buy_cnt;

        return sync_player_info;
    }

    // endregion

    private CardFactor getCardFactor(boolean is_offline)
    {
    	CardFactor result = new CardFactor();
    	PlayerMonthcardDB db = SpringContextUtil.getBean(PlayerMonthcardDB.class);
    	PlayerMonthcardInfo i = db.loadByUid(this.m_player_info._id);
    	if (i != null)
    	{
    		MonthcardImpl.Refresh(i, Calendar.getInstance().getTimeInMillis());
    		if (i.hasType(E_PayType.MONTH.getCode()))
    		{
    			if (is_offline)
    			{
        			result.month_gold = ConfigConstant.tMonthcard.get(E_PayType.MONTH.getCode()).getOffLine()[0];
        			result.month_exp = ConfigConstant.tMonthcard.get(E_PayType.MONTH.getCode()).getOffLine()[1];
    			}
    			else
    			{
        			result.month_gold = ConfigConstant.tMonthcard.get(E_PayType.MONTH.getCode()).getOnLine()[0];
        			result.month_exp = ConfigConstant.tMonthcard.get(E_PayType.MONTH.getCode()).getOnLine()[1];
    			}
    		}
    		if (i.hasType(E_PayType.LIFEALL.getCode()))
    		{
    			if (is_offline)
    			{
        			result.life_gold = ConfigConstant.tMonthcard.get(E_PayType.LIFEALL.getCode()).getOffLine()[0];
        			result.life_exp = ConfigConstant.tMonthcard.get(E_PayType.LIFEALL.getCode()).getOffLine()[1];
    			}
    			else
    			{
        			result.life_gold = ConfigConstant.tMonthcard.get(E_PayType.LIFEALL.getCode()).getOnLine()[0];
        			result.life_exp = ConfigConstant.tMonthcard.get(E_PayType.LIFEALL.getCode()).getOnLine()[1];
    			}
    		}
    		db.save();
    		db = null;
    		i = null;
    	}
    	return result;
    }
    
}

class CardFactor
{
	public int month_gold = 0;
	public int life_gold = 0;
	public int month_exp = 0;
	public int life_exp = 0;
}
