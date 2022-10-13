package com.ssmLogin.defdata.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.mongo.PageList;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmLogin.defdata.facde.I_GamePress;
import com.ssmShare.entity.Docking;
import com.ssmShare.entity.ServerList;
import com.ssmShare.entity.TypeServer;
import com.ssmShare.order.ShopItem;
import com.ssmShare.platform.PlatformInfo;
import com.ssmShare.platform.UserInfo;

@Service
public class GameImpl implements I_GamePress {
	private static final Logger log = LoggerFactory.getLogger(GameImpl.class);

	private BaseDaoImpl db = BaseDaoImpl.getInstance();

	@Override
	public List<PlatformInfo> getGamels(int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PlatformInfo> getGamels_all() {
		List<PlatformInfo> gamels = null;
		try {
			BasicDBObject fieldsObject = new BasicDBObject();
			fieldsObject.put("gid", 1);
			fieldsObject.put("gameNa", 1);
			Query queryinfo = new BasicQuery(new QueryBuilder().get(), fieldsObject);
			gamels = db.findAll(queryinfo, PlatformInfo.class);
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		}
		return gamels;
	}

	@Override
	public PlatformInfo getGameById(String gameId) {
		return db.find(new Query(Criteria.where("gid").is(gameId)), PlatformInfo.class);
	}

	@Override
	public boolean updateChatUrl(String gid, String url) {
		PlatformInfo game = null;
		try {
			game = db.find(new Query(Criteria.where("gid").is(gid)), PlatformInfo.class);
			if (game != null) {
				game.setChatUrl(url);
				db.saveOrUpdate(game);
			} else {
				return false;
			}
			return true;
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		}
		return false;
	}

	@Override
	public ServerList[] getServiceByGameId(String gameId) {
		List<ServerList> list = new ArrayList<ServerList>();
		ServerList[] svrs = db.find("platformInfo", "serverList",
				"'gid':'" + gameId + "'", ServerList[].class);
		if(svrs.length>0) {
			list.addAll(Arrays.asList(svrs));
		} else{
			PlatformInfo info = db.find(new Query(Criteria.where("gid").is(gameId)), PlatformInfo.class);
			for(Docking doc : info.getDocking()){
				TypeServer tsv = doc.getSvType();
				if(tsv!=null){
					if(tsv.getIso()!=null)
						list.addAll(Arrays.asList(tsv.getIso()));
					if(tsv.getAndriod()!=null)
						list.addAll(Arrays.asList(tsv.getAndriod()));
				}
			}
		}
		String json = JsonTransfer.getJson(list);
		return JsonTransfer._In(json, ServerList[].class);
	}

	@Override
	public boolean modifySerInfo(String gameId, ServerList ser) {
		try {
			Update update = new Update();
			update.set("serverList.$.name", ser.getName());
			update.set("serverList.$.dress", ser.getDress());
			update.set("serverList.$.status", ser.getStatus());
			update.set("serverList.$.isNew", ser.getIsNew());
			Criteria criteria = Criteria.where("gid").is(gameId).andOperator(Criteria.where("serverList").elemMatch(Criteria.where("zid").is(ser.getZid())));
			db.saveOrUpdate(new Query(criteria), update, PlatformInfo.class);
			return true;
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		}
		return false;
	}

	@Override
	public boolean addSerInfo(String gameId, ServerList ser) {
		try {
			Update update = new Update();
			update.addToSet("serverList", ser);
			db.insertOrUpdate(new Query(Criteria.where("gid").is(gameId)), update, PlatformInfo.class);
			return true;
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		}
		return false;
	}

	@Override
	public ServerList getServiceBySerId(String gameId, int serId) {
		return null;
	}

	@Override
	public boolean delServiceBySerId(String gameId, int serId) {
		try {
			Update update = new Update();
			ServerList list = new ServerList();
			list.setZid(serId);
			update.pull("serverList", list);
			db.saveOrUpdate(new Query(Criteria.where("gid").is(gameId)), update, PlatformInfo.class);
			return true;
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		}
		return false;
	}

	@Override
	public boolean addShopItem(String gameId, ShopItem item) {
		try {
			Update update = new Update();
			update.addToSet("rmbList", item);
			db.insertOrUpdate(new Query(Criteria.where("gid").is(gameId)), update, PlatformInfo.class);
			return true;
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		}
		return false;
	}

	@Override
	public boolean modifyShopItem(String gameId, ShopItem item) {
		try {
			Update update = new Update();
			update.set("rmbList.$.name", item.getName());
			update.set("rmbList.$.moneyType", item.getMoneyType());
			update.set("rmbList.$.type", item.getType());
			update.set("rmbList.$.monetaryAmount", item.getMonetaryAmount());
			update.set("rmbList.$.diamondsNum", item.getDiamondsNum());
			update.set("rmbList.$.award", item.getAward());
			update.set("rmbList.$.comment", item.getComment());
			update.set("rmbList.$.icon", item.getIcon());
			update.set("rmbList.$.firstId", item.getFirstId());
			Criteria criteria = Criteria.where("gid").is(gameId).andOperator(Criteria.where("rmbList").elemMatch(Criteria.where("itemId").is(item.getItemId())));
			db.saveOrUpdate(new Query(criteria), update, PlatformInfo.class);
			return true;
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		}
		return false;
	}

	@Override
	public boolean delShopItem(String gameId, int itemId) {
		try {
			Update update = new Update();
			ShopItem list = new ShopItem();
			list.setItemId(itemId);
			update.pull("rmbList", list);
			db.saveOrUpdate(new Query(Criteria.where("gid").is(gameId)), update, PlatformInfo.class);
			return true;
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		}
		return false;
	}

	@Override
	public List<ShopItem> getShopItemls(String gameId) {
		ShopItem[] ls = db.find("platformInfo", "rmbList", "'gid':'" + gameId + "'", ShopItem[].class);
		return Arrays.asList(ls);
	}

	@Override
	public List<Docking> getDockingByGameId(String gameId) {
		Docking[] ls = db.find("platformInfo", "docking", "'gid':'" + gameId + "'", Docking[].class);
		return Arrays.asList(ls);
	}

	@Override
	public boolean modifyLoginNotice(String gameId, String pid, String content) {
		try {
			Update update = new Update();
			update.set("docking.$.notice", content);
			Criteria criteria = Criteria.where("gid").is(gameId).andOperator(Criteria.where("docking").elemMatch(Criteria.where("pid").is(pid)));
			db.saveOrUpdate(new Query(criteria), update, PlatformInfo.class);
			return true;
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		}
		return false;
	}

	@Override
	public PageList getUserInfo(String gameId, int zid, String plat, String account, String nikeName, int pageNo, int pageSize) {
		Query query = new Query();
		query.addCriteria(Criteria.where("uaction.gTozid." + gameId + ".lgZid").in(zid));
		if (!plat.equals("0")) {
			query.addCriteria(Criteria.where("pid").is(plat));
		}
		if (!account.equals("")) {
			query.addCriteria(Criteria.where("account").is(account));
		}
		if (!nikeName.equals("")) {
			query.addCriteria(Criteria.where("userBase.nickname").regex(".*" + nikeName + ".*"));
		}
		return db.PaginationgetPage(pageNo, pageSize, query, UserInfo.class);
	}

	public List<UserInfo> getGidZidAllUser(String gameId, int zid) {
		Query query = new Query();
		query.addCriteria(Criteria.where("uaction.gTozid." + gameId + ".lastZid").is(zid));
		return db.findAll(query, UserInfo.class);
	}

	@Override
	public List<UserInfo> getUserByIdls(List<Integer> idls) {
		UserInfo[] ls = db.find("userInfo", "{'_id':{'$in':" + Arrays.toString(idls.toArray()) + "}}", UserInfo[].class);
		return Arrays.asList(ls);
	}

}
