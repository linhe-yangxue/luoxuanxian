package com.ssmGame.module.mail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
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
import com.ssmData.config.entity.Mail;
import com.ssmData.dbase.MailInfo;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerMailDB;
import com.ssmData.dbase.PlayerMailInfo;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.mail.MailMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.util.TimeUtils;

@Service
@Scope("prototype")
public class MailImpl {
	private static final Logger log = LoggerFactory.getLogger(MailImpl.class);
	
	@Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
    @Autowired
    PlayerMailDB m_mail_db;
    PlayerMailInfo m_mail = null;
    
    public MailImpl init(String uid){

        // 加载依赖的数据
    	m_mail = m_mail_db.loadByUid(uid);
        m_player = m_player_db.loadById(uid);
        m_bag = m_bag_db.loadByUid(uid);

        if(m_mail == null){
            log.error("PveImpl init ERROR " + uid);
            return null;
        }

        return this;
    }
    
    public void destroy()
    {
    	m_mail = null;
        m_mail_db = null;

        m_player = null;
        m_player_db = null;
        
		m_bag_db = null;
		m_bag = null;
    }
    
	public final static MailImpl getInstance(){
        return SpringContextUtil.getBean(MailImpl.class);
	}
    
    public CommonMsg handleReqAllMail(CommonMsg respond)
    {
    	MailMsg msg = new MailMsg();
    	respond.body.mail = msg;
    	msg.success = false;
    	
    	long now = Calendar.getInstance().getTimeInMillis();
    	Refresh(now, m_mail);
    	//m_mail_db.save();
    	
    	msg.mails = new MailInfo[m_mail.mails.size()];
    	for (int i = 0; i < msg.mails.length; ++i)
    	{
    		msg.mails[i] = m_mail.mails.get(i);
    	}
    	msg.success = true;
    	//log.info("mail handleReqAllMail SUCCESS!");
    	return respond;
    }
    
    public CommonMsg handleReqRead(CommonMsg respond, int mail_id)
    {
    	MailMsg msg = new MailMsg();
    	respond.body.mail = msg;
    	msg.success = false;
    	
    	long now = Calendar.getInstance().getTimeInMillis();
    	Refresh(now, m_mail);
    	//m_mail_db.save();
    	
    	MailInfo found = null;
    	for (int i = 0; i < m_mail.mails.size(); ++i)
    	{
    		MailInfo m = m_mail.mails.get(i);
    		if (m.m_id != mail_id)
    			continue;
    		found = m;
    		break;
    	}
    	if (null == found)
    	{
    		log.info("mail handleReqRead NO MAIL! {}" + mail_id);
    		respond.header.rt_sub = 1134;
    		return respond;
    	}
    	
    	found.m_read = true;
    	m_mail_db.save();
    	msg.success = true;
    	//log.info("mail handleReqRead SUCCESS!");
    	return respond;
    }
    
    public CommonMsg handleReqGetReward(CommonMsg respond, int mail_id)
    {
    	MailMsg msg = new MailMsg();
    	respond.body.mail = msg;
    	msg.success = false;
    	
    	long now = Calendar.getInstance().getTimeInMillis();
    	Refresh(now, m_mail);
    	//m_mail_db.save();
    	
    	MailInfo found = null;
    	for (int i = 0; i < m_mail.mails.size(); ++i)
    	{
    		MailInfo m = m_mail.mails.get(i);
    		if (m.m_id != mail_id)
    			continue;
    		found = m;
    		break;
    	}
    	if (null == found)
    	{
    		log.info("mail handleReqGetReward NO MAIL! {}" + mail_id);
    		respond.header.rt_sub = 1134;
    		return respond;
    	}
    	
    	Mail m_cfg = ConfigConstant.tMail.get(found.m_mid);
    	if (null == m_cfg)
    	{
    		log.info("mail handleReqGetReward NO MAIL  CONFIG ! {}" + found.m_mid);
    		return respond;
    	}
    	
    	if (m_cfg.getMType() != MailType.GIFT && m_cfg.getMType() != MailType.CUSTOM_GIFT)
    	{
    		log.info("mail handleReqGetReward MAIL Not Gift ! {}" + m_cfg.getMType());
    		return respond;
    	}
    	
    	double dmd = m_cfg.getMDiamond();
    	double gold = m_cfg.getMMoney();
    	int[] ids = m_cfg.getMItem();
    	int[] cnts = m_cfg.getMNum(); 
    	if (m_cfg.getMType() == MailType.CUSTOM_GIFT) {
    		if (found.dmd != null)
    			dmd = found.dmd;
    		if (found.gold != null)
    			gold = found.gold;
    		if (found.ids != null && found.ids.size() > 0) {
    			ids = new int[found.ids.size()];
    			cnts = new int[found.ids.size()];
    			for (int i = 0; i < found.ids.size(); ++i) {
    				ids[i] = found.ids.get(i);
    				cnts[i] = found.cnt.get(i);
    			}
    		}
    	}
    	
    	if (cnts.length > 0)
    	{
        	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
        	{
        		respond.header.rt_sub = 1168;
        		return respond;
        	}
    	}
    	
    	m_mail.mails.remove(found);
    	m_mail_db.save();
    	
    	SyncPlayerInfoMsg s_player = null;
    	if (dmd > 0) {
    		m_player.addDiamond(dmd);
    		if (s_player == null)
    			s_player = new SyncPlayerInfoMsg();
    		s_player.diamond = dmd;
    		msg.r_diamond = dmd;
    		log.info("Mail Add Dmd {} uid {} mail {}", dmd, m_player._id, mail_id);
    	}
    	if (gold > 0) {
    		m_player.addGold(gold);
    		if (s_player == null)
    			s_player = new SyncPlayerInfoMsg();
    		s_player.gold = gold;
    		msg.r_gold = gold;
    	}
    	if (s_player != null)
    	{
    		m_player_db.save();
    		respond.body.sync_player_info = s_player;
    	}
    	
    	Map<Integer, Integer> sync_bags = null;
    	List<SyncBagItem> r_items = null;
    	for (int i = 0; i < ids.length; ++i)
    	{
    		int id = ids[i];
    		int cnt = cnts[i];
    		if (id == 0 || cnt == 0)
    			continue;
    		m_bag.addItemCount(id, cnt);
    		if (sync_bags == null)
    			sync_bags = new HashMap<Integer, Integer>();
    		sync_bags.put(id, m_bag.getItemCount(id));
    		
    		if (r_items == null)
    			r_items = new ArrayList<SyncBagItem>();
    		SyncBagItem add_item = new SyncBagItem();
    		add_item.id = id;
    		add_item.count = cnt;
    		r_items.add(add_item);
    	}
    	if (sync_bags != null)
    	{
    		m_bag_db.save();
    		respond.body.sync_bag = new SyncBagMsg();
    		respond.body.sync_bag.items = sync_bags;
    	}
    	if (r_items != null)
    	{
    		msg.r_items = r_items;
    	}
    	
    	msg.success = true;
    	//log.info("mail handleReqGetReward SUCCESS!");
    	return respond;
    }
    
    public CommonMsg handleReqOnekeyReward(CommonMsg respond)
    {
    	MailMsg msg = new MailMsg();
    	respond.body.mail = msg;
    	msg.success = false;
    	
    	long now = Calendar.getInstance().getTimeInMillis();
    	Refresh(now, m_mail);
    	//m_mail_db.save();
    	
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	
    	List<MailInfo> old = m_mail.mails;
    	m_mail.mails = new ArrayList<MailInfo>();
    	SyncPlayerInfoMsg s_player = null;
    	Map<Integer, Integer> sync_bags = null;
    	Map<Integer, Integer> temp_add_cnt = null;
    	for (int i = 0; i < old.size(); ++i)
    	{
    		MailInfo m_i = old.get(i);
    		Mail m_cfg = ConfigConstant.tMail.get(m_i.m_mid);
        	if (null == m_cfg)
        	{
        		continue;
        	}
        	
        	if (m_cfg.getMType() != MailType.GIFT && m_cfg.getMType() != MailType.CUSTOM_GIFT)
        	{
        		m_mail.mails.add(m_i);
        		continue;
        	}
        	
        	double dmd = m_cfg.getMDiamond();
        	double gold = m_cfg.getMMoney();
        	int[] ids = m_cfg.getMItem();
        	int[] cnts = m_cfg.getMNum(); 
        	if (m_cfg.getMType() == MailType.CUSTOM_GIFT) {
        		if (m_i.dmd != null)
        			dmd = m_i.dmd;
        		if (m_i.gold != null)
        			gold = m_i.gold;
        		if (m_i.ids != null && m_i.ids.size() > 0) {
        			ids = new int[m_i.ids.size()];
        			cnts = new int[m_i.ids.size()];
        			for (int ix = 0; ix < m_i.ids.size(); ++ix) {
        				ids[ix] = m_i.ids.get(ix);
        				cnts[ix] = m_i.cnt.get(ix);
        			}
        		}
        	}
      
        	if (dmd > 0)
        	{
        		double before = m_player.diamond;
        		m_player.addDiamond(dmd);
        		if (s_player == null)
        		{
        			s_player = new SyncPlayerInfoMsg();
        			s_player.diamond = 0;
        		}
        		msg.r_diamond += dmd;	
        		s_player.diamond += dmd;
        		log.info("Mail Add Dmd {} uid {} mail {} before {} after {}", dmd, m_player._id, m_cfg.getMID(), before, m_player.diamond);
        	}
        	if (gold > 0)
        	{
        		m_player.addGold(gold);
        		if (s_player == null)
        		{
        			s_player = new SyncPlayerInfoMsg();
        			s_player.gold += 0;
        		}
        		s_player.gold += gold;
        		msg.r_gold += gold;
        	}
        	
        	for (int j = 0; j < ids.length; ++j)
        	{
        		int id = ids[j];
        		int cnt = cnts[j];
        		if (id == 0 || cnt == 0)
        			continue;
        		m_bag.addItemCount(id, cnt);
        		if (sync_bags == null)
        			sync_bags = new HashMap<Integer, Integer>();
        		sync_bags.put(id, m_bag.getItemCount(id));
       
        		if (temp_add_cnt == null)
        			temp_add_cnt = new HashMap<Integer, Integer>();
        		if (!temp_add_cnt.containsKey(id))
        			temp_add_cnt.put(id, cnt);
        		else
        			temp_add_cnt.put(id, temp_add_cnt.get(id) + cnt);
        	}       	
    	}
    	m_mail_db.save();
    	if (s_player != null)
    	{
    		msg.success = true;
    		m_player_db.save();
    		respond.body.sync_player_info = s_player;
    	}
    	if (sync_bags != null)
    	{
    		msg.success = true;
    		m_bag_db.save();
    		respond.body.sync_bag = new SyncBagMsg();
    		respond.body.sync_bag.items = sync_bags;
    	}
    	if (temp_add_cnt != null)
    	{
    		msg.success = true;
    		msg.r_items = new ArrayList<SyncBagItem>();
    		for (Entry<Integer, Integer> i : temp_add_cnt.entrySet())
    		{
    			SyncBagItem n = new SyncBagItem();
    			msg.r_items.add(n);
    			n.id = i.getKey();
    			n.count = i.getValue();
    		}
    	}
    	//log.info("mail handleReqOnekeyReward SUCCESS!");
    	return respond;
    }
    
    public CommonMsg handleReqNotRead(CommonMsg respond)
    {
    	MailMsg msg = new MailMsg();
    	respond.body.mail = msg;
    	msg.success = false;
    	
    	long now = Calendar.getInstance().getTimeInMillis();
    	Refresh(now, m_mail);
    	//m_mail_db.save();
    	
    	boolean gift = false;
    	boolean notify = false;
    	for (int i = 0; i < m_mail.mails.size(); ++i)
    	{
    		if (gift && notify)
    			break;
    		MailInfo info = m_mail.mails.get(i);
    		Mail m_cfg = ConfigConstant.tMail.get(info.m_mid);
    		if ( (m_cfg.getMType() == MailType.GIFT || m_cfg.getMType() == MailType.CUSTOM_GIFT)&& info.m_read == false)
    		{
    			gift = true;
    		}
    		else if ((m_cfg.getMType() == MailType.NOTIFY || m_cfg.getMType() == MailType.CUSTOM_NOTIFY)&& info.m_read == false)
    		{
    			notify = true;
    		}
    	}
    	msg.unread_gift = gift;
    	msg.unread_notify = notify;
    	msg.success = true;
    	//log.info("mail handleReqNotRead SUCCESS!");
    	return respond;
    }
    
    public static void Refresh(long now, PlayerMailInfo mail_info)
    {
    	List<MailInfo> old = mail_info.mails;
    	mail_info.mails = new ArrayList<MailInfo>();
    	for (int i = 0; i < old.size(); ++i)
    	{
    		MailInfo m = old.get(i);
    		if (now >= m.m_last)
    		{
    			continue;
    		}
    		mail_info.mails.add(m);
    	}
    }
    
    public static boolean AddMail(PlayerMailInfo mail_info, int mid, List<String> args,
								  int count, MailInfo custom)
    {
    	boolean ret = false;
    	if (mail_info.mails == null || count <= 0)
    		return ret;
    	long now = Calendar.getInstance().getTimeInMillis();
    	Refresh(now, mail_info);
    	if (mail_info.mails.size() > 1000)
    		return ret;
    	
    	int max_id = -1;
    	for (int i = 0; i < mail_info.mails.size(); ++i)
    	{
    		int id = mail_info.mails.get(i).m_id;
    		if (max_id < id)
    			max_id = id;
    	}
    	for (int i = 0; i < count; ++i)
    	{
        	max_id++;
        	MailInfo new_mail = new MailInfo();
        	new_mail.m_id = max_id;
        	new_mail.m_mid = mid;
        	new_mail.m_last = now + TimeUtils.SEVEN_DAY_TIME;
        	new_mail.m_read = false;
        	new_mail.m_args = args;
        	if (custom != null){
        		new_mail.word = custom.word;
        		new_mail.cnt = custom.cnt;
        		new_mail.dmd = custom.dmd;
        		new_mail.gold = custom.gold;
        		new_mail.ids = custom.ids;
        		new_mail.title = custom.title;
        	}
        	mail_info.mails.add(new_mail);
        	ret = true;
    	}
    	return ret;
    }

    public void AddMyMail(int mid, String args_raw)
    {
		List<String> args = new LinkedList<>();
		if(args_raw != null){
			String[] args_arr = args_raw.split(",");
			for(int i = 0; i < args_arr.length; i++){
				args.add(args_arr[i]);
			}
		}

        AddMail(this.m_mail, mid, args, 1, null);
        this.m_mail_db.save();
    }
}
