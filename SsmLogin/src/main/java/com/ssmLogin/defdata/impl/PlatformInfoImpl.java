package com.ssmLogin.defdata.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.colligate.PinYinUtil;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.defdata.facde.I_DataLoad;
import com.ssmShare.entity.Docking;
import com.ssmShare.entity.ServerList;
import com.ssmShare.order.ShopItem;
import com.ssmShare.platform.MemDat;
import com.ssmShare.platform.PlatformInfo;

@Service("platforminfo")
public class PlatformInfoImpl implements I_DataLoad {

	public static PlatformInfoImpl getInstance() {
		return SpringContextUtil.getBean(PlatformInfoImpl.class);
	}

	/**
	 * 数据初始化
	 */
	public void initDB(String gid) {
		loadDBInit(gid); // 平台信息初始化话
	}

	/**
	 * 数据初始化
	 */
	public void initDB() {
		loadDBInit(); // 平台信息初始化话
	}

	/**
	 * 游戏文件更新
	 */
	public void upLoad(String json) {
		try {
			PlatformInfo[] infos = JsonTransfer._In(json, PlatformInfo[].class);
			BaseDaoImpl db = BaseDaoImpl.getInstance();
			if (db.isExits(PlatformInfo.class)) {
				for (PlatformInfo info : infos) {
					String gid = PinYinUtil.getFirstSpell(info.getGameNa());
					info.setGid(gid);
					Query query = new Query(Criteria.where("gid").is(gid));
					PlatformInfo pinfo = db.find(query, PlatformInfo.class);
					if (pinfo == null) {
						db.add(info);
						continue;
					}
					if (info.getDocking() != null) { // 更新平台信息
						upArrayData(db, info.getGid(), info.getDocking());
						initDock(info.getGid(),info.getDocking()); // 初始化对接文档
					}
					if (info.getRmbList() != null) { // 更新商品信息
						upArrayData(db, info.getGid(), info.getRmbList());
						MemDat.setShops(info.getGid(), shopItemToMap(info.getRmbList()));
					}
					if (info.getServerList() != null) { // 更新服务器列表信息
						upArrayData(db, info.getGid(), info.getServerList());
						MemDat.setSvList(info.getGid(),serverMap(info.getGid(),info.getServerList()));
					}
				}
			} else {
				for (PlatformInfo info : infos)
					info.setGid(PinYinUtil.getFirstSpell(info.getGameNa()));
				db.add(Arrays.asList(infos));
			}
			db = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadDBInit() {
		List<PlatformInfo> ls = BaseDaoImpl.getInstance().findAll(PlatformInfo.class);
		if (ls != null && ls.size() > 0) {
			for (PlatformInfo info : ls) {
				if (info.getLoginURl() != null) // 登录服连接地址
					MemDat.setLoginUrl(info.getGid(), info.getLoginURl());

				if (info.getGameUrl() != null) // 游戏连接地址
					MemDat.setGameUrl(info.getGid(), info.getGameUrl());

				if (info.getChatUrl() != null) // 聊天地址
					MemDat.setChatUrl(info.getGid(), info.getChatUrl());

				if (info.getGameInerface() != null) // 订单访问地址
					MemDat.setOrderUrl(info.getGid(), info.getGameInerface());

				if (info.getDocking() != null) // 初始化对接文档
					initDock(info.getGid(),info.getDocking());

				if (info.getRmbList() != null) // 商品列表
					MemDat.setShops(info.getGid(), shopItemToMap(info.getRmbList()));

				if (info.getServerList() != null) // 服务器列表
					MemDat.setSvList(info.getGid(), serverMap(info.getGid(),info.getServerList()));
			}
		}
	}

	private void loadDBInit(String gid) {
		PlatformInfo info = BaseDaoImpl.getInstance().find(new Query(Criteria.where("gid").is(gid)),PlatformInfo.class);
		if (info != null) {
			if (info.getLoginURl() != null) // 登录服连接地址
				MemDat.setLoginUrl(info.getGid(), info.getLoginURl());

			if (info.getGameUrl() != null) // 游戏连接地址
				MemDat.setGameUrl(info.getGid(), info.getGameUrl());

			if (info.getChatUrl() != null) // 聊天地址
				MemDat.setChatUrl(info.getGid(), info.getChatUrl());

			if (info.getGameInerface() != null) // 订单访问地址
				MemDat.setOrderUrl(info.getGid(), info.getGameInerface());

			if (info.getDocking() != null) // 初始化对接文档
				initDock(info.getGid(),info.getDocking());

			if (info.getRmbList() != null) // 商品列表
				MemDat.setShops(info.getGid(), shopItemToMap(info.getRmbList()));

			if (info.getServerList() != null) // 服务器列表
				MemDat.setSvList(info.getGid(), serverMap(info.getGid(),info.getServerList()));
		}
	}

	private void initDock(String gid,Docking... docs) {
		if (docs != null)
			for (Docking doc : docs) {
				MemDat.setDocking(gid, doc.getPid(), doc);
			}
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	private <T> void upArrayData(BaseDaoImpl db, String gid, T... msgs) {
		for (Object msg : msgs) {
			Update update = new Update();
			Criteria critera = new Criteria();
			try {
				critera.where("gid").is(gid);
				if (msg instanceof Docking)
					upDocking(update, db, (Docking) msg, critera);

				else if (msg instanceof ServerList)
					upServerList(update, db, (ServerList) msg, critera);

				else if (msg instanceof ShopItem)
					upItemsList(update, db, (ShopItem) msg, critera);

				db.insertOrUpdate(new Query(critera), update, PlatformInfo.class);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				update = null;
				critera = null;
			}
		}
	}

	private void upServerList(Update update, BaseDaoImpl db, ServerList msg, Criteria critera) {
		ServerList[] ls = db.find("platformInfo", "serverList", "'serverList.zid':" + msg.getZid(), ServerList[].class);
		if (ls.length > 0) {
			update.set("serverList.$.name", msg.getName());
			update.set("serverList.$.dress", msg.getDress());
			update.set("serverList.$.status", msg.getStatus());
			update.set("serverList.$.isNew", msg.getIsNew());
			critera.andOperator(Criteria.where("serverList").elemMatch(Criteria.where("zid").is(msg.getZid())));
		} else {
			update.addToSet("serverList", msg);
		}
	}

	private void upDocking(Update update, BaseDaoImpl db, Docking msg, Criteria critera) {
		Docking[] ls = db.find("platformInfo", "docking", "'docking.pid':'" + msg.getPid() + "'", Docking[].class);
		if (ls.length > 0) {

			if (msg.getPname() != null && msg.getPname().isEmpty())
				update.set("docking.$.pname", msg.getPname());

			if (msg.getRate() != null)
				update.set("docking.$.rate", msg.getRate());

			if (msg.getLoginKey() != null && msg.getLoginKey().isEmpty())
				update.set("docking.$.loginKey", msg.getLoginKey());

			if (msg.getPayKey() != null && msg.getPayKey().isEmpty())
				update.set("docking.$.payKey", msg.getPayKey());

			if (msg.getNotice() != null && msg.getNotice().isEmpty())
				update.set("docking.$.context", msg.getNotice());

			update.set("docking.$.isWx", msg.getIsWx());

			critera.andOperator(Criteria.where("docking").elemMatch(Criteria.where("pid").is(msg.getPid())));
		} else {
			update.addToSet("docking", msg);
		}
	}

	private void upItemsList(Update update, BaseDaoImpl db, ShopItem msg, Criteria critera) {
		ShopItem[] ls = db.find("platformInfo", "rmbList", "'rmbList._id':" + msg.getItemId(), ShopItem[].class);
		if (ls.length > 0) {
			if (msg.getItemId() > 0)
				update.set("rmbList.$.id", msg.getItemId());
			if (msg.getName() != null && !msg.getName().isEmpty())
				update.set("rmbList.$.name", msg.getName());
			if (msg.getMoneyType() > 0)
				update.set("rmbList.$.moneyType", msg.getMoneyType());
			if (msg.getMonetaryAmount() > 0)
				update.set("rmbList.$.monetaryAmount", msg.getMonetaryAmount());
			if (msg.getDiamondsNum() > 0)
				update.set("rmbList.$.diamondsNum", msg.getDiamondsNum());
			if (msg.getAward() > 0)
				update.set("rmbList.$.award", msg.getAward());
			if (msg.getComment() != null && !msg.getComment().isEmpty())
				update.set("rmbList.$.comment", msg.getComment());
			if (msg.getIcon() != null && !msg.getIcon().isEmpty())
				update.set("rmbList.$.icon", msg.getIcon());

			critera.andOperator(Criteria.where("rmbList").elemMatch(Criteria.where("id").is(msg.getItemId())));
		} else {
			update.addToSet("rmbList", msg);
		}
	}

	public Map<Integer, ShopItem> shopItemToMap(ShopItem... items) {
		Map<Integer, ShopItem> map = new HashMap<Integer, ShopItem>();
		for (ShopItem item : items) {
			map.put(item.getItemId(), item);
		}
		return map;
	}
	
	public static Map<Integer,ServerList> serverMap(String gid, ServerList...svs){
		Map<Integer,ServerList> map = new TreeMap<Integer,ServerList>();
		for(ServerList sv : svs){
			map.put(sv.getZid(),sv);
			MemDat.setSvMap(gid,sv.getZid(),sv.getDress());
		}
		return map;
	}
}
