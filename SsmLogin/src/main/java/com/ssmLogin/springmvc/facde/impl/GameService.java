package com.ssmLogin.springmvc.facde.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.ssmCore.memcached.MemAccess;
import com.ssmShare.constants.E_PlateInfo;
import com.ssmShare.platform.DataConf;
import com.ssmShare.platform.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.ReInfo;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.defdata.facde.I_GamePress;
import com.ssmLogin.defdata.impl.PlatformInfoImpl;
import com.ssmLogin.springmvc.facde.I_Game;
import com.ssmShare.entity.Docking;
import com.ssmShare.entity.ServerList;
import com.ssmShare.order.ShopItem;
import com.ssmShare.platform.MemDat;
import com.ssmShare.platform.PlatformInfo;

@Service
public class GameService implements I_Game {
	@Autowired
	I_GamePress service;

	@Override
	public ReInfo getGamels(int page) {
		List<PlatformInfo> ls = service.getGamels(page);
		if (ls == null) {
			return new ReInfo(I_Error_Login.ERROR);
		} else {
			return new ReInfo(ls);
		}
	}

	@Override
	public ReInfo getGamels() {
		List<PlatformInfo> ls = service.getGamels_all();
		if (ls == null) {
			return new ReInfo(I_Error_Login.ERROR);
		} else {
			return new ReInfo(ls);
		}
	}

	@Override
	public ReInfo getGameById(String id) {
		PlatformInfo info = service.getGameById(id);
		if (info == null) {
			return new ReInfo(I_Error_Login.ERROR_ADMIN_GAME_NOTEXIST);
		} else {
			return new ReInfo(info);
		}
	}

	@Override
	public ReInfo updateChatUrl(String gid, String url) {
		service.updateChatUrl(gid, url);
		MemDat.setChatUrl(gid, url);
		return new ReInfo(I_Error_Login.SUCCESS);
	}

	@Override
	public ReInfo getServiceByGameId(String id) {
		List<ServerList> info = Arrays.asList(service.getServiceByGameId(id));
		if (info == null) {
			return new ReInfo(I_Error_Login.ERROR_ADMIN_GAME_NOTEXIST);
		} else {
			return new ReInfo(info);
		}
	}

	@Override
	public ReInfo modifySerInfo(String gameId, ServerList ser) {
		service.modifySerInfo(gameId, ser);
		MemDat.setSvList(gameId, PlatformInfoImpl.serverMap(gameId,service.getServiceByGameId(gameId)));
		return new ReInfo(I_Error_Login.SUCCESS);
	}

	@Override
	public ReInfo addSerInfo(String gameId, ServerList ser) {
		List<ServerList> list = Arrays.asList(service.getServiceByGameId(gameId));
		;
		int zid = 1;
		if (list != null && list.size() > 0) {
			Collections.sort(list, new Comparator<ServerList>() {
				public int compare(ServerList o1, ServerList o2) {
					return o2.getZid() - o1.getZid();
				}
			});
			zid = list.get(0).getZid() + 1;
		}
		ser.setZid(zid);

		service.addSerInfo(gameId, ser);
		MemDat.setSvList(gameId, PlatformInfoImpl.serverMap(gameId,service.getServiceByGameId(gameId)));
		return new ReInfo(I_Error_Login.SUCCESS);
	}

	@Override
	public ReInfo delSerInfo(String gameId, int serId) {
		service.delServiceBySerId(gameId, serId);
		MemDat.setSvList(gameId, PlatformInfoImpl.serverMap(gameId,service.getServiceByGameId(gameId)));
		return new ReInfo(I_Error_Login.SUCCESS);
	}

	@Override
	public ReInfo addShopItem(String gameId, ShopItem item) {

		List<ShopItem> list = service.getShopItemls(gameId);
		int itemId = 1;
		if (list != null && list.size() > 0) {
			Collections.sort(list, new Comparator<ShopItem>() {
				public int compare(ShopItem o1, ShopItem o2) {
					return o2.getItemId() - o1.getItemId();
				}
			});
			itemId = list.get(0).getItemId() + 1;
		}
		item.setItemId(itemId);

		service.addShopItem(gameId, item);
		Map<Integer, ShopItem> map = MemDat.getShops(gameId);
		map.put(item.getItemId(), item);
		MemDat.setShops(gameId, map);
		return new ReInfo(I_Error_Login.SUCCESS);
	}

	@Override
	public ReInfo modifyShopItem(String gameId, ShopItem item) {
		service.modifyShopItem(gameId, item);
		Map<Integer, ShopItem> map = MemDat.getShops(gameId);
		map.put(item.getItemId(), item);
		MemDat.setShops(gameId, map);
		return new ReInfo(I_Error_Login.SUCCESS);
	}

	@Override
	public ReInfo delShopItem(String gameId, int itemId) {
		service.delShopItem(gameId, itemId);
		Map<Integer, ShopItem> map = MemDat.getShops(gameId);
		map.remove(itemId);
		MemDat.setShops(gameId, map);
		return new ReInfo(I_Error_Login.SUCCESS);
	}

	@Override
	public ReInfo getShopItemls(String gameId) {
		List<ShopItem> info = service.getShopItemls(gameId);
		if (info == null) {
			return new ReInfo(new ArrayList<>());
		} else {
			return new ReInfo(info);
		}
	}

	@Override
	public ReInfo getDockingByGameId(String gameId) {
		List<Docking> info = service.getDockingByGameId(gameId);
		if (info == null) {
			return new ReInfo(I_Error_Login.ERROR_ADMIN_GAME_NOTEXIST);
		} else {
			return new ReInfo(info);
		}
	}

	@Override
	public ReInfo modifyLoginNotice(String gameId, String pid, String content) {
		service.modifyLoginNotice(gameId, pid, content);
		Docking doc = MemDat.getDocking(gameId, pid);
		doc.setNotice(content);
		MemDat.setDocking(gameId, pid, doc);
		return new ReInfo(I_Error_Login.SUCCESS);
	}

	@Override
	public ReInfo getUserInfo(String gameId, int zid, String plat, String account, String nikeName, int pageNum) {
		return new ReInfo(service.getUserInfo(gameId, zid, plat, account, nikeName, pageNum, 10));
	}
	@Override
	public void repairToRollOff(String gameId,int zid) {
		List<UserInfo> userInfos = getGidZidAllUser(gameId,zid);
		DataConf dSource = DataConf.getInstance();
		for (UserInfo userInfo : userInfos) {
			MemAccess.Delete("MTK" + userInfo.getUserBase().getUid());
		}
	}

	@Override
	public List<UserInfo> getGidZidAllUser(String gameId, int zid) {
		return service.getGidZidAllUser(gameId, zid);
	}

	@Override
	public ReInfo getUserByIdls(List<Integer> idls) {
		return new ReInfo(service.getUserByIdls(idls));
	}
}
