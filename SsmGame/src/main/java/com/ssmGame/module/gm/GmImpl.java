package com.ssmGame.module.gm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.CommUtil;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Item;
import com.ssmData.dbase.MailInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerMailDB;
import com.ssmData.dbase.PlayerMailInfo;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.mail.MailImpl;
import com.ssmShare.order.GiftItem;

@Service
@Scope("prototype")
public class GmImpl {
	private static final Logger log = LoggerFactory.getLogger(GmImpl.class);
	
	public final static GmImpl getInstance(){
        return SpringContextUtil.getBean(GmImpl.class);
    }
	
	private static final String WORD_ERR = "标题或者内容不能为空";
	
	public ReInfo handleGift(CommonMsg respond, GiftItem msg)
	{
		Lock lock = new ReentrantLock();  
		lock.lock();
		ReInfo result = new ReInfo(MsgCode.DESIGN_ERR_GIFT);
		try
		{
			boolean all_plat = msg.getPid().length() <= 0;
			MailInfo custom = new MailInfo();
			custom.cnt = msg.getCnt();
			custom.dmd = msg.getDmd();
			custom.gold = msg.getGold();
			custom.ids = msg.getIds();
			custom.title = msg.getTitle();
			custom.word = msg.getWord();
			if (custom.title.length() <= 0 || custom.word.length() <= 0) {
				result.msg = WORD_ERR;
				return result;
			}
			if (custom.ids != null) {
				for (int i = 0; i < custom.ids.size(); ++i) {
					Item i_cfg = ConfigConstant.tItem.get(custom.ids.get(i));
					if (i_cfg == null || custom.cnt.get(i) <= 0) {
						result.msg = new String("道具不存在或者数量错误  id " + custom.ids.get(i));
						return result;
					}
				}
			}
			
			List<String> id_list = null;
			if (msg.getMailType() == 0) {
				BaseDaoImpl db = BaseDaoImpl.getInstance();
				List<PlayerMailInfo> ml = db.findAll(PlayerMailInfo.class);
				int s = ml.size();
				id_list = new ArrayList<String>(s);
				for (int i = 0; i < s; ++i) {
					if (!CommUtil.isNumeric(ml.get(i).uid)) {
						continue;
					}
					id_list.add(ml.get(i).uid);
				}
			}
			else {
				id_list = msg.getUid();
			}
			if (id_list == null) {
				log.info("Uid == null {}", JsonTransfer.getJson(msg));
				return result;
			}
			
			int size = id_list.size();
			for (int i = 0; i < size; ++i){
				String uid = id_list.get(i);
				
				//全局发送且不是全平台的时候，需要检测平台是否相同
				if (msg.getMailType() == 0 && all_plat == false) { 
					PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
					PlayerInfo p_info = p_db.loadById(uid);
					if (p_info == null || p_info.user_base == null || p_info.user_base.pid == null) {
						log.error("uid {}", uid);
						continue;
					}
					if (msg == null || msg.getPid() == null) {
						log.error("{}", JsonTransfer.getJson(msg));
						continue;
					}
					if (!p_info.user_base.pid.equals(msg.getPid()))
					{
						continue;
					}
				}
							
				PlayerMailDB mail_db = SpringContextUtil.getBean(PlayerMailDB.class);
				PlayerMailInfo mail_info = mail_db.loadByUid(uid);
				if (mail_info == null) {
					log.warn("GM handleGift no mail_db uid {}", uid);
					result.msg = new String("用户邮件数据错误 " + uid);
					return result;
				}
				
				int mail_id = 0;
				if (msg.getType() == 0) {
					mail_id = 1; //自定义物品邮件
				}
				else if (msg.getType() == 1) {
					mail_id = 2;//自定义通知
				}
				if (mail_id == 0) {
					continue;
				}
				
				MailImpl.AddMail(mail_info, mail_id, null, 1, custom);
				mail_db.save();
			}
			result.rt = MsgCode.SUCCESS;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result.msg = e.getMessage();
		}
		finally{
			lock.unlock();
		}
		return result;
	}
}
