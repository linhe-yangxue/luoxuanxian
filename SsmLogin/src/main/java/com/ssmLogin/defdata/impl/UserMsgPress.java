package com.ssmLogin.defdata.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.defdata.facde.I_MsgPress;
import com.ssmLogin.springmvc.facde.impl.PlatDockService;
import com.ssmShare.entity.InviteEny;
import com.ssmShare.entity.UserBase;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;
import com.ssmShare.platform.MemDat;
import com.ssmShare.platform.UserInfo;

@Service
public class UserMsgPress implements I_MsgPress {

	private static final Logger log = LoggerFactory.getLogger(UserMsgPress.class);
	private @Value("${MEM_KEY_EXPIRE}") Integer mkeyTime;
	private @Value("${GID_PATH}") Boolean isopen;
	@Autowired
	BaseDaoImpl db;

	@Override
	public String pressUser(DataConf dSource, I_Platform platform) {
		try {
			String mkey = UUID.randomUUID().toString() + dSource.gid;
			mkey = Encryption.Encode(mkey, Encryption.MD5);

			String skipUrl = dSource.doc.getGameUrl_res(); // 平台独立游戏地址
			if (skipUrl == null)
				skipUrl = MemDat.getGameUrl(dSource.gid); // 获取默认游戏地址
			skipUrl = skipUrl += isopen ? "/" + dSource.gid : "";
			skipUrl += "?mkey=" + mkey + "|" + MemDat.getLoginUrl(dSource.gid);
			skipUrl += "&plat_id=" + dSource.rtn.getPid();
			skipUrl += "&game_id=" + dSource.rtn.getGid();

			String lgurl = MemDat.getLoginUrl(dSource.gid);
			if (lgurl != null) {
				if (dSource.doc.getIsWx() == 1) {
					String wxlgurl = PlatDockService.getWxUrl(dSource.gid, dSource.doc.getPid(), dSource.wx, lgurl);
					dSource.rtn.setGameUrl_login(wxlgurl);
				} else {
					dSource.rtn.setGameUrl_login(dSource.doc.getGameUrl_login()); // 设置回调登录
					dSource.rtn.setPayUrl(dSource.doc.getPayUrl()); // 设置第三方支付跳转地址
				}
				// 游戏订单创建地址
				dSource.rtn.setGameUrl_pay(lgurl + "/" + dSource.doc.getPid() + "/" + dSource.gid + "/order");
			} else {
				log.warn(dSource.gid + "未设置登录服务器地址！");
			}
			dSource.saveMsg(mkey, mkeyTime);// 存储用户信息等待用户访问
			dSource.rtn.getInfo().setPid(dSource.rtn.getPid());
			UserDataPool.uDate.put(JsonTransfer.conleObject(dSource));
			return skipUrl; // 返回游戏跳转连接

		} catch (Exception e) {
			log.warn(I_Error_Login.ERRO_DB_RW + "", e);
		}
		return null;
	}

	@Override
	public String pressUrl(Map<String, Object> param, DataConf dSource) {
		try {
			String mkey = UUID.randomUUID().toString() + dSource.gid;
			mkey = Encryption.Encode(mkey, Encryption.MD5);

			String skipUrl = dSource.doc.getGameUrl_res(); // 平台独立游戏地址
			if (skipUrl == null)
				skipUrl = MemDat.getGameUrl(dSource.gid); // 获取默认游戏地址

			skipUrl = skipUrl += isopen ? "/" + dSource.gid : "";
			skipUrl += "?mkey=" + mkey + "|" + MemDat.getLoginUrl(dSource.gid);
			skipUrl += "&plat_id=" + dSource.rtn.getPid();
			skipUrl += "&game_id=" + dSource.rtn.getGid();
			param.remove("gid");
			param.remove("pid");
			param.remove("zid");
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				skipUrl += "&" + entry.getKey() + "=" + entry.getValue();
			}
			String lgurl = MemDat.getLoginUrl(dSource.gid);
			if (lgurl != null) {
				if (dSource.doc.getIsWx() == 1) {
					String wxlgurl = PlatDockService.getWxUrl(dSource.gid, dSource.doc.getPid(), dSource.wx, lgurl);
					dSource.rtn.setGameUrl_login(wxlgurl);
				} else {
					dSource.rtn.setGameUrl_login(dSource.doc.getGameUrl_login()); // 设置回调登录
					dSource.rtn.setPayUrl(dSource.doc.getPayUrl()); // 设置第三方支付跳转地址
				}
				// 游戏订单创建地址
				dSource.rtn.setGameUrl_pay(lgurl + "/" + dSource.doc.getPid() + "/" + dSource.gid + "/order");
			} else {
				log.warn(dSource.gid + "未设置登录服务器地址！");
			}
			dSource.rtn.setUsebase(new UserBase());
			dSource.saveMsg(mkey, mkeyTime);// 存储用户信息等待用户访问
			dSource.rtn.getInfo().setPid(dSource.rtn.getPid());
			return skipUrl; // 返回游戏跳转连接

		} catch (Exception e) {
			log.warn(I_Error_Login.ERRO_DB_RW + "", e);
		}
		return null;
	}

	@Override
	public void pressStoreUser(Map<String, Object> param, DataConf dSource) {

		BaseDaoImpl db = BaseDaoImpl.getInstance();
		String phone = String.valueOf(param.get("phone"));
		Criteria criteria = Criteria.where("account")
				.is(dSource.rtn.getInfo().getAccount()).and("pid").is(dSource.rtn.getPid());

		UserInfo info = db.find(new Query(criteria), UserInfo.class);
		Update update = new Update();
		boolean isNew = false;
		if (phone != null)
			dSource.rtn.getInfo().getUserBase().setDevice(Integer.parseInt(phone));
		if (info != null) {
			update.set("userBase", dSource.rtn.getInfo().getUserBase());
			db.saveOrUpdate(new Query(criteria), update, UserInfo.class);
		} else {
			db.add(dSource.rtn.getInfo());
			if (dSource.rtn.getInfo().getUserBase().getInvitID() != null) {
				Invitation(dSource.rtn.getInfo().getUserBase().getInvitID() // 邀请人帐号
						, dSource.rtn.getInfo().getAccount() // 被邀请人帐号
						, dSource.rtn.getInfo().getPid());
			}
			info = db.find(new Query(criteria), UserInfo.class);
			isNew = true;
		}
		dSource.rtn.setInfo(info);
		String mkey = (String) param.get("mkey");
		dSource.saveMsg(mkey, mkeyTime);// 存储用户信息等待用户访问
		if (!isNew) {
			if (info.getUaction() != null) {
				dSource.rtn.setLastZid(info.getUaction().getLastZid((String) param.get("gid")));
				dSource.rtn.setLogZid(info.getUaction().getlgZid((String) param.get("gid")));
			} else {
				dSource.rtn.setLogZid(new HashSet<Integer>());
			}
		}
	}

	/**
	 * 用户邀请记录存储
	 * 
	 * @param dSoucse
	 */
	private void Invitation(String id, String account, String pid) {
		if (id != null && account != null && pid != null) {
			InviteEny inv = new InviteEny(id, account, pid);
			BaseDaoImpl.getInstance().add(inv);
		}
	}

}
