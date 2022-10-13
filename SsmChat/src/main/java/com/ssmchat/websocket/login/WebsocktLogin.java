package com.ssmchat.websocket.login;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.jetty.EventSocket;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.HttpRequest;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmShare.constants.E_TYPE;
import com.ssmShare.entity.ChartUser;
import com.ssmShare.entity.ChatEntity;
import com.ssmShare.platform.UserInfo;
import com.ssmchat.constanst.Defconstants;

@Service
public class WebsocktLogin implements I_Login {

	private @Value("${LOGIN_SERVER}") String loginPath;
	private @Value("${CHART_DISPLAY}") Integer dis;

	public static WebsocktLogin getInstance() {
		return SpringContextUtil.getBean(WebsocktLogin.class);
	}

	@Override
	public void client(EventSocket event) {
		try {
			if(event.getObj()!=null){
				event.setUnconnet(WebsocketClose.getInstance());
				Defconstants.addChannle(event);
				chatMsgLoad(event);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		event.onWebSocketClose(1000, "非法用户");
	}

	/**
	 * 个人游戏信息发布
	 * 
	 * @param chat
	 */
	@Override
	public void playerPush(ChatEntity chat) {
		try {
			String guid = chat.getGuid().toString();
			if (guid != null) {
				String user = HttpRequest.GetFunction(loginPath + "?guid=" + guid + "&uaction=1");
				UserInfo ubase = JsonTransfer._In(user, UserInfo.class);
				if (user != null) {
					chat.setGid(ubase.getUaction().getLastGid()); // 最后登录的游戏
					chat.setZid(ubase.getUaction().getLastZid(ubase.getUaction().getLastGid())); // 最后登录的区
				}
			}
		} catch (Exception e) {

		}
	}

	/** 信息发送 */
	private void chatMsgLoad(EventSocket event) {
		ChartUser cUser = (ChartUser) event.getObj();
		BaseDaoImpl db = BaseDaoImpl.getInstance();
		
		ChartList lst = new ChartList();
		Query query = new Query(Criteria.where("channel").is(0)
					.and("sender.gid").is(cUser.getGid()));
		query.with(new Sort(new Order(Direction.DESC, "sendTime")));
		query.limit(20);
		lst.c_word = db.findAll(query, ChatEntity.class);

		if(cUser.getGuildId()!= null){//工会聊天信息
			Query union = new Query(Criteria.where("channel").is(2)
					.and("sender.gid").is(cUser.getGid())
					.and("sender.guildId").is(cUser.getGuildId()));
			union.with(new Sort(new Order(Direction.DESC, "sendTime")));
			union.limit(20);
			lst.c_union = db.findAll(union, ChatEntity.class);
			if(lst.c_union!=null && lst.c_union.size()>0)
				lst.c_word.addAll(lst.c_union);
		}
		
		if ((lst.c_word != null && lst.c_word.size() > 0) 
				|| (lst.c_union != null && lst.c_union.size() > 0)) {
			event.sendMsg(JsonTransfer.getJson(new ReInfo(E_TYPE.MSGLIST.getCode(),lst.c_word)));
		}
	}


	class ChartList{
		public List<ChatEntity> c_word;  //世界频道
		public List<ChatEntity> c_union; //工会频道
	}
	
}
