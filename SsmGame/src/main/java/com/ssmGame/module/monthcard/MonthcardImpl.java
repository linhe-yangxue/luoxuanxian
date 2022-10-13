package com.ssmGame.module.monthcard;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.ssmData.config.entity.Lvgift;
import com.ssmData.config.entity.Monthcard;
import com.ssmData.config.entity.Spay;
import com.ssmData.config.entity.Vip;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerInvestActivityDB;
import com.ssmData.dbase.PlayerInvestActivityInfo;
import com.ssmData.dbase.PlayerMailDB;
import com.ssmData.dbase.PlayerMailInfo;
import com.ssmData.dbase.PlayerMonthcardDB;
import com.ssmData.dbase.PlayerMonthcardInfo;
import com.ssmData.dbase.PlayerVipBenefitDB;
import com.ssmData.dbase.PlayerVipBenefitInfo;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.defdata.msg.priv.PrivMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.activity.AccRmbImpl;
import com.ssmGame.module.activity.ActivityType;
import com.ssmGame.module.activity.DaypayImpl;
import com.ssmGame.module.broadcast.BroadcastImpl;
import com.ssmGame.module.mail.MailImpl;
import com.ssmGame.module.player.PlayerImpl;
import com.ssmShare.constants.E_PayType;
import com.ssmShare.order.GmItem;

@Service
@Scope("prototype")
public class MonthcardImpl {
	private static final Logger log = LoggerFactory.getLogger(MonthcardImpl.class);
	
	//首冲状态值
	public static final int NOT_FIRST_BILL = 0;  //尚未充过值
	public static final int NOT_GET_FRIST_AWARD = 1; //已经首充，尚未领取奖励
	public static final int AREALY_GET_AWARD = 2; //已经领过奖励
	
	public static final int[] VIPBENEFIT_MAILS = {0, 501, 502, 503, 504, 505, 506, 507
								, 508, 509, 510, 511, 512, 513, 514, 515, 516, 517, 518 };
	
	@Autowired
	PlayerMonthcardDB month_db;
	
	public final static MonthcardImpl getInstance(){
        return SpringContextUtil.getBean(MonthcardImpl.class);
	}
	
	public void destroy()
	{
		month_db = null;
	}
	
	//todo 对接
	public CommonMsg handleBuy(CommonMsg respond, GmItem msg)
	{
		respond.header.rt = MsgCode.DESIGN_ERR_BILLING;
		String uid = respond.header.uid;
		
		if (msg.getType() == null || msg.getDianmod() == null)
		{
			return respond;
		}
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null)
			return respond;
		
		PlayerMonthcardInfo month_info = month_db.loadByUid(uid);
		if (month_info == null){
			month_info = (PlayerMonthcardInfo)month_db.createDB(uid);
		}
		
		PlayerMailDB mail_db = SpringContextUtil.getBean(PlayerMailDB.class);
		PlayerMailInfo mail_info = mail_db.loadByUid(uid);
		if (mail_info == null)
			return respond;
		
		int card_id = msg.getType();
		int add_dmd = msg.getDianmod();
		
		if (msg.getFrist() != null && month_info.first_award_status == NOT_FIRST_BILL)
		{
			month_info.first_award_status = NOT_GET_FRIST_AWARD;
		}
		
		long last_bill_time = month_info.bill_t;
		Calendar now_cal = Calendar.getInstance();
		long now = now_cal.getTimeInMillis();
		month_info.bill_t = now;
		month_info.has_bill = true;
		month_info.r_dmd = add_dmd;
		
		if (p_info.t_c_dmd == null) {
			p_info.t_c_dmd = (double)add_dmd;
		} else {
			p_info.t_c_dmd += (double)add_dmd;
		}
		
		int it_id = msg.getItemId();
		
		if (msg.getItFrist() != null){
			if (month_info.finish_1st == null)
				month_info.finish_1st = new ArrayList<Integer>();
			if (!month_info.finish_1st.contains(it_id))
				month_info.finish_1st.add(it_id);
		}
		
		if (card_id == E_PayType.NOME.getCode() || card_id == E_PayType.INVEST.getCode()
				|| card_id == E_PayType.LVGIFT.getCode())
		{
			if (card_id == E_PayType.INVEST.getCode()) {
				if (!ActivityType.checkActivityOpen(ActivityType.Invest, now, p_info)){
					log.warn("Invest Activity Not Open! player {}", uid);
					return respond;
				}
				PlayerInvestActivityDB act_db = SpringContextUtil.getBean(PlayerInvestActivityDB.class);
				PlayerInvestActivityInfo act = act_db.loadByUid(uid);
				if (act == null) {
					return respond;
				}
				if (act.has_invest) {
					log.warn("Invest Activity Already Bought! player {}", uid);
					return respond;
				}
				act.has_invest = true;
				act_db.save();
			}
			
			if (card_id == E_PayType.LVGIFT.getCode()) {
				Lvgift lv_cfg = ConfigConstant.tLvgift.get(it_id);
				if (lv_cfg == null) {
					log.warn("No Lvgift cfg ID player {} {}", uid, it_id);
					return respond;
				}
				boolean found = false;
				for (int i = 0; i < month_info.lvgift_id.size(); ++i) {
					if (month_info.lvgift_id.get(i) == it_id) {
						if (month_info.lvgift_st.get(i) == PlayerMonthcardInfo.LVGIFT_ONSALE) {
							/*if (Calendar.getInstance().getTimeInMillis() <= month_info.lvgift_t) {
								month_info.lvgift_st.set(i, PlayerMonthcardInfo.LVGIFT_HOLDING);
							}
							else {
								month_info.lvgift_st.set(i, PlayerMonthcardInfo.LVGIFT_OUT);
							}*/
							month_info.lvgift_st.set(i, PlayerMonthcardInfo.LVGIFT_HOLDING);
							found = true;
						} else {
							log.warn("No Lvgift ST ERROR player {} id{} st{}", uid, it_id, month_info.lvgift_st.get(i));
							return respond;
						}
					}
				}
				if (found == false) {
					log.warn("No Lvgift ID ERROR player {} id{}", uid, it_id);
					return respond;
				}
			}
			
			p_info.addDiamond(add_dmd);
			p_info.acc_rmb += msg.getMoney();
			AccRmbImpl.getInstance().bill(msg.getMoney(), p_info, last_bill_time);
			DaypayImpl.getInstance().bill(msg.getMoney(), p_info, last_bill_time, now);
			int v_lv = p_info.vip_level;
			Vip v_cfg = ConfigConstant.tVip.get(v_lv);
			int old_v_lv = v_lv;
			while (v_cfg != null)
			{
				if (v_cfg.getRmb() > 0 && v_cfg.getRmb() <= p_info.acc_rmb)
				{
					v_lv++;
				}
				else
					break;
				v_cfg = ConfigConstant.tVip.get(v_lv);
			}
			p_info.vip_level = v_lv;
			p_db.save();
			if (old_v_lv != v_lv) {
				PlayerVipBenefitDB vb_db = SpringContextUtil.getBean(PlayerVipBenefitDB.class);
				PlayerVipBenefitInfo vb_act = vb_db.loadByUid(uid);
				if (vb_act != null) {
					Calendar last_cal = Calendar.getInstance();
					last_cal.setTimeInMillis(vb_act.last_t);
					if (last_cal.get(Calendar.DAY_OF_YEAR) == now_cal.get(Calendar.DAY_OF_YEAR)) {
						for (int i = old_v_lv + 1; i <= v_lv && i < VIPBENEFIT_MAILS.length; ++i) {
							MailImpl.AddMail(mail_info, VIPBENEFIT_MAILS[i], null, 1, null);
						}
						mail_db.save();
					}
				}
				
				String context = ConfigConstant.tWordconfig.get(BroadcastImpl.VIP).getWord_cn();
	        	context = context.replace("$1", p_info.user_base.getNickname());
	        	context = context.replace("$2", "" + p_info.vip_level);
	        	BroadcastImpl bi = BroadcastImpl.getInstance();
	        	bi.SendBrocast(context, p_info.user_base.gid, p_info.user_base.zid);
			}
			
			if (month_info.spay_ids == null) {
				month_info.spay_ids = new ArrayList<Integer>();
				month_info.spay_r = new ArrayList<Integer>();
				month_info.spay_t = new ArrayList<Long>();
			}
			
			for (Entry<Integer, Spay> s_cfg : ConfigConstant.tSpay.entrySet()) {
				int id = s_cfg.getKey();
				if (month_info.spay_ids.contains(id)) {
					continue;
				}
				if (p_info.acc_rmb < s_cfg.getValue().getSpay()) {
					continue;
				}
				month_info.spay_ids.add(id);
				month_info.spay_t.add(now);
			}
			
			month_db.save();
			
			p_info = null;
			p_db = null;
			respond.header.rt = MsgCode.SUCCESS;
			return respond;
		}
		
		Monthcard m_cfg = ConfigConstant.tMonthcard.get(card_id);
		if (m_cfg == null)
		{
			return respond;
		}
		
		if (card_id == E_PayType.LIFEALL.getCode() && month_info.hasType(card_id))
		{
			return respond;
		}
		if (card_id == E_PayType.MONTH.getCode() && !month_info.hasType(E_PayType.MONTH.getCode())) {
				MailImpl.AddMail(mail_info, m_cfg.getMail(), null, 1, null);
				month_info.last_mail_t = now;
		}
		if (card_id == E_PayType.LIFEALL.getCode()) {
			MailImpl.AddMail(mail_info, m_cfg.getMail(), null, 1, null);
			MailImpl.AddMail(mail_info, m_cfg.getMailFirst(), null, 1, null);
			month_info.last_mail_t = now;
		}
		month_info.addCardId(card_id);
		mail_db.save();
		mail_db = null;
		mail_info = null;
		
		if (card_id == E_PayType.MONTH.getCode())
		{
			if (month_info.mc_end < now)
				month_info.mc_end = now + Long.parseLong(ConfigConstant.tConf.getMonth());
			else
				month_info.mc_end += Long.parseLong(ConfigConstant.tConf.getMonth());
		}
		
		p_info.addDiamond(add_dmd);
		p_info.acc_rmb += msg.getMoney();
		AccRmbImpl.getInstance().bill(msg.getMoney(), p_info, last_bill_time);
		DaypayImpl.getInstance().bill(msg.getMoney(), p_info, last_bill_time, now);
		int v_lv = p_info.vip_level;
		Vip v_cfg = ConfigConstant.tVip.get(v_lv);
		int old_v_lv = v_lv;
		while (v_cfg != null)
		{
			if (v_cfg.getRmb() > 0 && v_cfg.getRmb() <= p_info.acc_rmb)
				v_lv++;
			else
				break;      
			v_cfg = ConfigConstant.tVip.get(v_lv);
		}
		p_info.vip_level = v_lv;
		if (old_v_lv != v_lv) {		
			String context = ConfigConstant.tWordconfig.get(BroadcastImpl.VIP).getWord_cn();
        	context = context.replace("$1", p_info.user_base.getNickname());
        	context = context.replace("$2", "" + p_info.vip_level);
        	BroadcastImpl bi = BroadcastImpl.getInstance();
        	bi.SendBrocast(context, p_info.user_base.gid, p_info.user_base.zid);
		}
		
		if (month_info.spay_ids == null) {
			month_info.spay_ids = new ArrayList<Integer>();
			month_info.spay_r = new ArrayList<Integer>();
			month_info.spay_t = new ArrayList<Long>();
		}
		
		for (Entry<Integer, Spay> s_cfg : ConfigConstant.tSpay.entrySet()) {
			int id = s_cfg.getKey();
			if (month_info.spay_ids.contains(id)) {
				continue;
			}
			if (p_info.acc_rmb < s_cfg.getValue().getSpay()) {
				continue;
			}
			month_info.spay_ids.add(id);
			month_info.spay_t.add(now);
		}
		
		month_db.save();
		month_info = null;
		
		p_db.save();
		p_info = null;
		p_db = null;
		respond.header.rt = MsgCode.SUCCESS;
		return respond;
	}
	
	public CommonMsg handleInfo(CommonMsg respond)
	{
		String uid = respond.header.uid;
		PrivMsg msg = new PrivMsg();
		msg.success = false;
		respond.body.priv = msg;
		
		PlayerMonthcardInfo month_info = month_db.loadByUid(uid);
		if (month_info == null)
		{
			month_info = (PlayerMonthcardInfo) month_db.createDB(uid);
		}
		
		if (month_info.spay_ids == null) {
			month_info.spay_ids = new ArrayList<Integer>();
			month_info.spay_r = new ArrayList<Integer>();
			month_info.spay_t = new ArrayList<Long>();
			
			PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
			PlayerInfo p_info = p_db.loadById(uid);
			if (p_info == null)
			{
				p_db = null;
				return respond;
			}
			
			for (Entry<Integer, Spay> s_cfg : ConfigConstant.tSpay.entrySet()) {
				int id = s_cfg.getKey();
				if (month_info.spay_ids.contains(id)) {
					continue;
				}
				if (p_info.acc_rmb < s_cfg.getValue().getSpay()) {
					continue;
				}
				month_info.spay_ids.add(id);
				month_info.spay_t.add(0L);
			}			
			month_db.save();
		}
		
		msg.card = month_info;
		msg.success = true;

		return respond;
	}
	
	public CommonMsg handleDailyReward(CommonMsg respond)
	{
		String uid = respond.header.uid;
		PrivMsg msg = new PrivMsg();
		msg.success = false;
		respond.body.priv = msg;
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null)
			return respond;
		int v_lv = 0;
		Vip v_cfg = ConfigConstant.tVip.get(v_lv);
		while (v_cfg != null)
		{
			if (v_cfg.getRmb() > 0 && v_cfg.getRmb() <= p_info.acc_rmb)
			{
				v_lv++;
			}
			else
				break;
			v_cfg = ConfigConstant.tVip.get(v_lv);
		}
		p_info.vip_level = v_lv;
		p_db.save();
		
		PlayerMonthcardInfo month_info = month_db.loadByUid(uid);
		if (month_info == null)
		{
			return respond;
		}
		msg.success = true;
		
		Calendar last_mail_day = Calendar.getInstance();
		last_mail_day.setTimeInMillis(month_info.last_mail_t);
		Calendar today = Calendar.getInstance();
		int day_diff = DateDiff(last_mail_day, today);
		if (day_diff <= 0)
		{
			return respond;
		}
		if (day_diff > 365)
		{
			log.warn("MonthcardImpl handleDailyReward diff too big {} day {}", uid, day_diff);
			return respond;
		}
		
		PlayerMailDB mail_db = SpringContextUtil.getBean(PlayerMailDB.class);
		PlayerMailInfo mail_info = mail_db.loadByUid(uid);
		if (mail_info == null)
		{
			mail_db = null;
			return respond;
		}		
		if (month_info.hasType(E_PayType.MONTH.getCode()))
		{
			int before = 0;
			if (mail_info.mails != null) {
				before = mail_info.mails.size();
			}
			Monthcard m_cfg = ConfigConstant.tMonthcard.get(E_PayType.MONTH.getCode());
			if (MailImpl.AddMail(mail_info, m_cfg.getMail(), null, day_diff, null)) {
				log.info("Month card mail player {} id {} before {}, after {}", uid, m_cfg.getMail(), before, mail_info.mails.size());
			}
		}
		if (month_info.hasType(E_PayType.LIFEALL.getCode()))
		{
			int before = 0;
			if (mail_info.mails != null) {
				before = mail_info.mails.size();
			}
			Monthcard m_cfg = ConfigConstant.tMonthcard.get(E_PayType.LIFEALL.getCode());
			if (MailImpl.AddMail(mail_info, m_cfg.getMail(), null, day_diff, null)) {
				log.info("Life card mail player {} id {} before {}, after {}", uid, m_cfg.getMail(), before, mail_info.mails.size());
			}
		}
		mail_db.save();
		
		month_info.last_mail_t = Calendar.getInstance().getTimeInMillis();
		Refresh(month_info, Calendar.getInstance().getTimeInMillis());
		month_db.save();
			
		month_info = null;
		mail_db = null;
		return respond;
	}
	
	public CommonMsg handleFirstReward(CommonMsg respond)
	{
		String uid = respond.header.uid;
		PrivMsg msg = new PrivMsg();
		msg.success = false;
		respond.body.priv = msg;
		
		if (ConfigConstant.tConf.getFPaySwitch() == 0) {
			return respond;
		}
		
		PlayerMonthcardInfo month_info = month_db.loadByUid(uid);
		if (month_info == null)
		{
			return respond;
		}
		if (month_info.first_award_status != NOT_GET_FRIST_AWARD)
		{
			return respond;
		}
		
		PlayerBagDB bag_db = SpringContextUtil.getBean(PlayerBagDB.class);
		PlayerBagInfo bag = bag_db.loadByUid(uid);
    	if (bag.getEquipBagCapacity() - bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		bag_db = null;
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	
    	month_info.first_award_status = AREALY_GET_AWARD;
    	month_db.save();
    	
    	respond.body.sync_bag = new SyncBagMsg();
    	respond.body.sync_bag.items = new HashMap<Integer, Integer>();
    	msg.r_items = new ArrayList<SyncBagItem>();
    	for (int i = 0; i < ConfigConstant.tConf.getFPay().length; ++i)
    	{
    		int id = ConfigConstant.tConf.getFPay()[i];
    		int cnt = ConfigConstant.tConf.getFPayQ()[i];
    		bag.addItemCount(id, cnt);
    		respond.body.sync_bag.items.put(id, bag.getItemCount(id));
    		
    		SyncBagItem s = new SyncBagItem();
    		s.id = id;
    		s.count = cnt;
    		msg.r_items.add(s);
    	}
    	bag_db.save();
    	bag = null;
    	bag_db = null;
    	
    	msg.card = month_info;
    	msg.success = true;
		return respond;
	}
	
	public CommonMsg handlePayback(CommonMsg respond)
	{
		String uid = respond.header.uid;
		respond.body.sync_player_info = new SyncPlayerInfoMsg();
		PrivMsg msg = new PrivMsg();
		msg.success = false;
		respond.body.priv = msg;
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		p_db = null;
		if (p_info == null)
		{
			return respond;
		}
		
		PlayerMonthcardInfo month_info = month_db.loadByUid(uid);
		if (month_info == null) {
			return respond;
		}
		if (!month_info.has_bill) {
			return respond;
		}
		msg.off_diamond = month_info.r_dmd;
		month_info.has_bill = false;
		month_info.r_dmd = 0;
		month_db.save();
		
		respond.body.sync_player_info.vip_level = p_info.vip_level;
		respond.body.sync_player_info.acc_rmb = p_info.acc_rmb;
		msg.p_diamond = p_info.diamond;
		
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleVipAward(CommonMsg respond, int vip_lv)
	{
		String uid = respond.header.uid;
		PrivMsg msg = new PrivMsg();
		msg.success = false;
		respond.body.priv = msg;
		
		Vip v_cfg = ConfigConstant.tVip.get(vip_lv);
		if (v_cfg == null)
		{
			return respond;
		}
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null)
		{
			p_db = null;
			return respond;
		}
		
		if (p_info.vip_level < vip_lv)
		{
			return respond;
		}
		
		if (p_info.vip_award.contains(vip_lv))
		{
			return respond;
		}
		
		if (!p_info.hasDiamond(v_cfg.getCost()))
		{
			respond.header.rt_sub = 1005;
			return respond;
		}
		
		PlayerBagDB bag_db = SpringContextUtil.getBean(PlayerBagDB.class);
		PlayerBagInfo bag = bag_db.loadByUid(uid);
    	if (bag.getEquipBagCapacity() - bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		bag_db = null;
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
		
    	double old_dmd = p_info.diamond;
    	log.info("Vip buy vip pack player {} cost {}", uid, v_cfg.getCost());
    	PlayerImpl.SubDiamond(p_info, v_cfg.getCost());
		p_info.vip_award.add(vip_lv);
		
		SyncPlayerInfoMsg s_player = new SyncPlayerInfoMsg();
		respond.body.sync_player_info = s_player;
		
    	if (v_cfg.getDiamonds() > 0)
    	{
    		p_info.addDiamond(v_cfg.getDiamonds());
    		msg.r_diamond = v_cfg.getDiamonds();
    		
    	}
    	s_player.diamond = p_info.diamond - old_dmd;
    	if (v_cfg.getMoney() > 0)
    	{
    		p_info.addGold(v_cfg.getMoney());
    		msg.r_gold = v_cfg.getMoney();
    		s_player.gold = v_cfg.getMoney();
    	}
    	p_db.save();
    	p_db = null;
    	
    	Map<Integer, Integer> sync_bags = null;
    	List<SyncBagItem> r_items = null;
    	for (int i = 0; i < v_cfg.getItemId().length; ++i)
    	{
    		int id = v_cfg.getItemId()[i];
    		int cnt = v_cfg.getItemNum()[i];
    		if (id == 0 || cnt == 0)
    			continue;
    		bag.addItemCount(id, cnt);
    		if (sync_bags == null)
    			sync_bags = new HashMap<Integer, Integer>();
    		sync_bags.put(id, bag.getItemCount(id));
    		
    		if (r_items == null)
    		{
    			r_items = new ArrayList<SyncBagItem>();
    			msg.r_items = r_items;
    		}
    			
    		SyncBagItem add_item = new SyncBagItem();
    		add_item.id = id;
    		add_item.count = cnt;
    		r_items.add(add_item);
    	}
    	if (sync_bags != null)
    	{
    		bag_db.save();
    		bag_db = null;
    		respond.body.sync_bag = new SyncBagMsg();
    		respond.body.sync_bag.items = sync_bags;
    	}
		
    	msg.success = true;
		return respond;
	}

	public CommonMsg handleSpayReward(CommonMsg respond, int spay_id) {
		String uid = respond.header.uid;
		PrivMsg msg = new PrivMsg();
		msg.success = false;
		respond.body.priv = msg;
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null)
		{
			p_db = null;
			return respond;
		}
		
		PlayerBagDB bag_db = SpringContextUtil.getBean(PlayerBagDB.class);
		PlayerBagInfo bag = bag_db.loadByUid(uid);
    	if (bag.getEquipBagCapacity() - bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		bag_db = null;
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	
    	PlayerMonthcardInfo month_info = month_db.loadByUid(uid);
		if (month_info == null)
		{
			return respond;
		}	
		if (month_info.spay_ids == null) {
			return respond;
		}
		
		Spay s_cfg = ConfigConstant.tSpay.get(spay_id);
		if (s_cfg == null) {
			return respond;
		} 
		
		int s_id = s_cfg.getID();
		if (!month_info.spay_ids.contains(s_id)) {
			return respond;
		}
		if (month_info.spay_r.contains(s_id)) {
			return respond;
		}
		month_info.spay_r.add(spay_id);
		month_db.save();
		
		
		long pay_t = 0;
		for (int i = 0; i < month_info.spay_ids.size(); ++i) {
			if (month_info.spay_ids.get(i) == s_id) {
				pay_t = month_info.spay_t.get(i);
				break;
			}
		}
		boolean factor = pay_t > 0 && pay_t > p_info.creat_t && pay_t - p_info.creat_t <= s_cfg.getLtime();
		
		SyncPlayerInfoMsg s_player = new SyncPlayerInfoMsg();
		respond.body.sync_player_info = s_player;
		int add_gold = s_cfg.getSpayGold() * (factor ? s_cfg.getGoldDouble() : 1);
    	if (add_gold > 0)
    	{
    		p_info.addGold(add_gold);
    		msg.r_gold = add_gold;
    		s_player.gold = add_gold;
    	}
    	p_db.save();
    	p_db = null;
    	
    	Map<Integer, Integer> sync_bags = null;
    	List<SyncBagItem> r_items = null;
    	for (int i = 0; i < s_cfg.getItem().length; ++i)
    	{
    		int id = s_cfg.getItem()[i];
    		int cnt = s_cfg.getCounts()[i] * (factor ? s_cfg.getDouble()[i] : 1);
    		if (id == 0 || cnt == 0)
    			continue;
    		bag.addItemCount(id, cnt);
    		if (sync_bags == null)
    			sync_bags = new HashMap<Integer, Integer>();
    		sync_bags.put(id, bag.getItemCount(id));
    		
    		if (r_items == null)
    		{
    			r_items = new ArrayList<SyncBagItem>();
    			msg.r_items = r_items;
    		}
    			
    		SyncBagItem add_item = new SyncBagItem();
    		add_item.id = id;
    		add_item.count = cnt;
    		r_items.add(add_item);
    	}
    	if (sync_bags != null)
    	{
    		bag_db.save();
    		bag_db = null;
    		respond.body.sync_bag = new SyncBagMsg();
    		respond.body.sync_bag.items = sync_bags;
    	}
    	
    	log.info("Get Spay player {}, spay id {}", uid, spay_id);
    	
    	msg.success = true;
		return respond;
	}
	
	public static void Refresh(PlayerMonthcardInfo i, long now)
	{
		if (i.hasType(E_PayType.MONTH.getCode()) && now > i.mc_end)
		{
			i.removeId(E_PayType.MONTH.getCode());
		}
	}
	
	public static int DateDiff(Calendar before, Calendar after)
	{
		int result = 0;
		int bf = before.get(Calendar.DAY_OF_YEAR);
		int af = after.get(Calendar.DAY_OF_YEAR);
		int by = before.get(Calendar.YEAR);
		int ay = after.get(Calendar.YEAR);
		int dis = 0;
		if (by != ay)
		{
			for (int i = by; i < ay; ++i)
			{
				if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)
				{
					dis += 366;
				}
				else
				{
					dis += 365;
				}
			}
		}
		result = af - bf + dis;
		return result;
	}

}
