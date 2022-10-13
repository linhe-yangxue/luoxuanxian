package com.ssmGame.module.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Hdrankstage;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerMailDB;
import com.ssmData.dbase.PlayerMailInfo;
import com.ssmData.dbase.PlayerTaskInfo;
import com.ssmGame.manager.RankManager;
import com.ssmGame.module.mail.MailImpl;

public class PveLevelActivityImpl {
	private static final Logger log = LoggerFactory.getLogger(PveLevelActivityImpl.class);
	public static void handleAward(List<PlayerTaskInfo> all, int dif_day) {
		int[] difs = ConfigConstant.tConf.getRanktime();
		int dif_index = -1;
		for (int i = 0; i < difs.length; ++i) {
			if (difs[i] == dif_day) {
				dif_index = i;
				break;
			}
		}
		if (dif_index < 0)
			return;
		log.info("PveLevelActivityImpl.handleAward() dif day {}", dif_day);
		long now = Calendar.getInstance().getTimeInMillis();
		Map<Integer, String> rank = RankManager.level_rank;
		Hdrankstage other_player_cfg = null;
		List<String> tops = new ArrayList<String>();
		for (Entry<Integer, Hdrankstage> a_cfg : ConfigConstant.tHdrankstage.entrySet()) {
			if (a_cfg.getKey() == 0) {
				other_player_cfg = a_cfg.getValue();
				continue;
			}
			Hdrankstage config = a_cfg.getValue();
			int begin = config.getRank()[0];
			if (begin < 1)
				continue;
			int end = config.getRank()[1];
			if (end <= 0 || end > rank.size())
				continue;
			for (int i = begin; i <= end; ++i)
			{
				String uid = rank.get(i);
				PlayerMailDB mail_db = SpringContextUtil.getBean(PlayerMailDB.class);
				PlayerMailInfo mail_info = mail_db.loadByUid(uid);
				if (mail_info == null)
					continue;
				PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
				PlayerInfo p_info = p_db.loadById(uid);
				if (p_info == null)
					continue;
				if (!ActivityType.checkActivityOpen(ActivityType.PveRank, now, p_info))
					continue;
				MailImpl.AddMail(mail_info, config.getMail()[dif_index], null, 1, null);
				mail_db.save();
				tops.add(uid);
			}
		}
		if (other_player_cfg != null) {
			for (int i = 0; i < all.size(); ++i) {
				PlayerTaskInfo m = all.get(i);
				if (tops.contains(m.uid))
					continue;
				PlayerMailDB mail_db = SpringContextUtil.getBean(PlayerMailDB.class);
				PlayerMailInfo mail_info = mail_db.loadByUid(m.uid);
				if (mail_info == null)
					continue;
				PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
				PlayerInfo p_info = p_db.loadById(m.uid);
				if (p_info == null)
					continue;
				if (!ActivityType.checkActivityOpen(ActivityType.PveRank, now, p_info))
					continue;
				MailImpl.AddMail(mail_info, other_player_cfg.getMail()[dif_index], null, 1, null);
				mail_db.save();
			}
		}
	}
}
