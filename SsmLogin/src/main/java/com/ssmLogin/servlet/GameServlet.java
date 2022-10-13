package com.ssmLogin.servlet;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.jetty.BaseServlet;
import com.ssmCore.jetty.FunUrl;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.CommUtil;
import com.ssmCore.tool.colligate.DateUtil;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.constant.I_ModuleServlet;
import com.ssmLogin.defdata.impl.StatisticsPool;
import com.ssmLogin.defdata.impl.UserMsgPress;
import com.ssmShare.entity.Docking;
import com.ssmShare.entity.InviteEny;
import com.ssmShare.entity.ServerList;
import com.ssmShare.entity.Uaction;
import com.ssmShare.entity.UserBase;
import com.ssmShare.entity.statistics.LogCreateStats;
import com.ssmShare.entity.statistics.LogLoginStats;
import com.ssmShare.order.ShopItem;
import com.ssmShare.platform.MemDat;
import com.ssmShare.platform.ReturnMag;
import com.ssmShare.platform.UserInfo;

public class GameServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(UserMsgPress.class);

	public GameServlet() {
		super(GameServlet.class, new HttpParamsPress());
	}

	/**
	 * 返回服务器列表及公告信息
	 * 
	 * @param param
	 * @return
	 */
	@FunUrl(value = I_ModuleServlet.GET_INFO_SERVERLIST)
	public ReInfo getServerInfo(Map<String, Object> param) {
		String token = (String) param.get("mkey");
		ReturnMag obj = MemDat.getServerMsg(token);
		try {
			if (obj != null) {
				Map<Integer, ShopItem> shops = MemDat.getShops(obj.getGid());
				if (shops != null) {
					String json = JsonTransfer.getJson(shops.values().toArray());
					obj.setShops(JsonTransfer._In(json, ShopItem[].class));
				}

				Integer diverType = (Integer) param.get("phone");
				Docking doc = MemDat.getDocking(obj.getGid(), obj.getPid());
				ServerList[] svlist = null;
				obj.setChatUrl(MemDat.getChatUrl(obj.getGid())); // 设置聊天服

				if (doc != null && doc.getSvType() != null) {
					svlist = doc.getSvType().getSvList(diverType);
					if (svlist != null) // 设置访问的服务器列表
						obj.setSvList(svlist);
					if (doc.getSvType().getChatUrl(diverType) != null)
						obj.setChatUrl(doc.getSvType().getChatUrl(diverType));
				} else {
					Map<Integer, ServerList> servers = MemDat.getSvList(obj.getGid());
					if (servers != null && servers.size() > 0) {
						String json = JsonTransfer.getJson(servers.values().toArray());
						obj.setSvList(JsonTransfer._In(json, ServerList[].class));
					}
				}

				UserInfo info = null; // 记录游戏登录状态---------
				if (obj.getIsWx().intValue() == 1) {
					info = (UserInfo) BaseDaoImpl.getInstance()
							.find(new Query(Criteria.where("account").is(obj.getInfo().getAccount())), UserInfo.class);
				} else
					info = (UserInfo) BaseDaoImpl.getInstance().find(new Query(
							Criteria.where("account").is(obj.getInfo().getAccount()).and("pid").is(obj.getPid())),
							UserInfo.class);

				if (info != null) {
					if ((info.getUaction() != null) && (info.getUaction().getLastZid(obj.getGid()) != null)
							&& (info.getUaction().getlgZid(obj.getGid()) != null)) {
						obj.setLastZid(info.getUaction().getLastZid(obj.getGid()));
						obj.setLogZid(info.getUaction().getlgZid(obj.getGid()));
					} else {
						obj.setLastZid(Integer.valueOf(obj.getSvList()[(obj.getSvList().length - 1)].getZid()));
					}
					obj.setGuid(Long.valueOf(info.get_id()));
				}
				obj.setInfo(null);
				return new ReInfo(obj);
			} else {
				Object platid = param.get("plat_id");
				Object gameid = param.get("game_id");
				if (platid != null && gameid != null) {
					Docking docking = MemDat.getDocking(gameid.toString(), platid.toString());
					if (docking != null) {
						return new ReInfo(I_Error_Login.ERRO_SERVERLIST, docking.getGameUrl_login());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			obj = null;
		}
		return new ReInfo(I_Error_Login.ERRO_SERVERLIST);
	}

	/**
	 * 返回用户基本信息 --用户登录
	 * 
	 * @param param
	 * @return
	 */
	@FunUrl(value = I_ModuleServlet.GET_USER_INFO)
	public Object getUserGuid(Map<String, Object> param) {
		try {
			String token = (String) param.get("mkey");
			ReturnMag obj = MemDat.getServerMsg(token);
			Integer zid = Integer.parseInt((String) param.get("zid"));
			if (token != null && zid != null) {
				if (obj != null) {
					BaseDaoImpl db = BaseDaoImpl.getInstance();
					UserInfo info = null;

					if ((obj.getIsWx() != null) && (obj.getIsWx().intValue() == 1)) {
						info = (UserInfo) db.find(new Query(Criteria.where("account").is(obj.getInfo().getAccount())),
								UserInfo.class);
					} else {
						info = (UserInfo) db.find(new Query(
								Criteria.where("account").is(obj.getInfo().getAccount()).and("pid").is(obj.getPid())),
								UserInfo.class);
					}

					if (info != null) {
						Uaction action = info.getUaction();
						if (action == null)
							action = new Uaction();
						action.setLastZid(zid, obj.getGid());
						action.setLastLogin(new Date());

						Update update = new Update().set("uaction", action);
						db.saveOrUpdate(new Query(
								Criteria.where("account").is(obj.getInfo().getAccount()).and("pid").is(obj.getPid())),
								update, UserInfo.class);

						info.getUserBase().setGuid(Long.valueOf(info.get_id()),
								action.getGameIndex(obj.getGid()).intValue(), zid.intValue());
						info.setUaction(action);
						StatisticsPool.dDate.put(info);
						UserBase ubase = (UserBase) JsonTransfer.conleObject(info.getUserBase());
						ubase.gid = obj.getGid();
						ubase.zid = zid;

						if (ubase.getDisShare().intValue() == 1) {
							List<InviteEny> ls = db
									.findAll(
											new Query(Criteria.where("inviterId").is(obj.getInfo().getAccount())
													.and("invitee_login").is(DateUtil.getCurrDate("-"))),
									InviteEny.class);
							if ((ls != null) && (ls.size() > 0)) {
								ubase.setCurrDay(Integer.valueOf(ls.size()));
							}
						}
						MemDat.delServerMsg(token);
						return ubase;
					}
				}
			}
		} catch (Exception e) {
			log.warn(I_Error_Login.ERRO_USER_GET + "获取用户信息错误！", e);
		}
		return new ReInfo(I_Error_Login.ERRO_USER_GET);
	}

	/**
	 * 修改用户头像昵称
	 * 
	 * @param param
	 * @return
	 */
	@FunUrl(value = I_ModuleServlet.MODFIY_USER_INFO)
	public Object modyUserMsg(Map<String, Object> param) {
		BaseDaoImpl db = BaseDaoImpl.getInstance();
		try {
			if (param.get("guid") != null) {
				String objguid = String.valueOf(param.get("guid"));
				if (CommUtil.isNumeric(objguid)) {
					Long guid = Long.parseLong(objguid);
					Query query = new Query(Criteria.where("_id").is(guid));
					UserInfo info = db.find(query, UserInfo.class);
					if (info != null) {
						int type = Integer.parseInt(String.valueOf(param.get("type")));
						String uuid = String.valueOf(param.get("uuid"));
						String gid = String.valueOf(param.get("g_id"));
						String nickname = String.valueOf(param.get("nickname"));
						int rolelevel = Integer.parseInt(String.valueOf(param.get("rolelevel")));
						String zid = String.valueOf(param.get("zid"));
						if (type == 0) {
							// 创建角色
							LogCreateStats createStats = db.find(new Query(Criteria.where("uid").is(uuid)),
									LogCreateStats.class);
							if (createStats == null) {
								createStats = new LogCreateStats();
								createStats.setGid(gid);
								createStats.setPid(info.getPid());
								createStats.setAccount(info.getAccount());
								createStats.setUid(uuid);
								createStats.setCreateTime(new Date());
								createStats.setRoleName(nickname);
								createStats.setRoleLevel(rolelevel);
								createStats.setServerId(zid);
								db.add(createStats);
							} else {
								log.warn("重复调用创建角色！ uuid" + uuid);
							}
						} else if (type == 1) {
							/////////////////////// 登陆日志//////////////////////
							LogLoginStats loginStats = null;
							Date start = DateUtil.getDate(System.currentTimeMillis(), DateUtil.DEFAULT_DATA_PATTERN);
							Calendar ca = Calendar.getInstance();
							ca.add(Calendar.DATE, 1);
							Date end = ca.getTime();

							Query querylog = new Query();
							querylog.addCriteria(Criteria.where("account").is(info.getAccount()));
							querylog.addCriteria(Criteria.where("createTime").gte(start).lt(end));
							loginStats = db.find(querylog, LogLoginStats.class);

							if (loginStats == null) {
								loginStats = new LogLoginStats();
								loginStats.setGid(gid);
								loginStats.setPid(info.getPid());
								loginStats.setAccount(info.getAccount());
								loginStats.setCount(1);
								loginStats.setUid(uuid);
								loginStats.setRoleName(nickname);
								loginStats.setServerId(zid);
								loginStats.setRoleLevel(rolelevel);
								loginStats.setCreateTime(new Date());
								db.add(loginStats);
							} else {
								Update update = new Update();
								update.set("uid", uuid);
								update.set("gid", gid);
								update.set("pid", info.getPid());
								update.set("roleName", nickname);
								update.set("roleLevel", rolelevel);
								update.set("serverId", zid);
								update.set("createTime", new Date());
								update.set("count", loginStats.getCount() + 1);
								db.saveOrUpdate(new Query(Criteria.where("account").is(info.getAccount())), update,
										LogLoginStats.class);
							}
						} else {
							log.warn(I_ModuleServlet.MODFIY_USER_INFO + " type:" + type + "  非法请求！");
						}
						return "success";
					}
				}
			}
		} catch (Exception e) {
			log.warn(I_Error_Login.ERRO_USER_MODFIY + "回传用户信息异常！", e);
			e.printStackTrace();
		} finally {
			db = null;
		}
		return "fail";
	}

	/**
	 * 聊天服务器获取用户信息
	 * 
	 * @param param
	 * @return
	 */
	@FunUrl(value = I_ModuleServlet.CHAT_USER_INFO)
	public Object GetChatUser(Map<String, Object> param) {
		BaseDaoImpl db = BaseDaoImpl.getInstance();
		try {
			String guid = (String) param.get("guid");
			Query query = new Query(Criteria.where("_id").is(Long.parseLong(guid)));
			UserInfo info = db.find(query, UserInfo.class);
			if (info != null) {
				return info.getUserBase();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db = null;
		}
		return null;
	}

}
