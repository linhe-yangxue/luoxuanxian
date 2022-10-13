package com.ssmchat.websocket;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.jetty.EventSocket;
import com.ssmCore.jetty.I_SocketPress;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmShare.constants.E_ChatChannel;
import com.ssmShare.constants.E_TYPE;
import com.ssmShare.entity.ChartUser;
import com.ssmShare.entity.ChatEntity;
import com.ssmchat.constanst.Defconstants;
import com.ssmchat.websocket.login.ChartPool;
import com.ssmchat.websocket.login.I_Login;

@Service
public class ChatTalk implements I_SocketPress{

	@Autowired I_Login login;
	@Override
	public void recive(EventSocket event, String json) {
		try{
			ChatEntity chat = JsonTransfer._In(json, ChatEntity.class);
			if(chat.getMsgType().intValue()== E_TYPE.LOGIN.getCode()){
				ChartUser cUser = JsonTransfer._In(json, ChartUser.class);
				event.setObj(cUser);
				login.client(event);
				
			}else if(chat.getMsgType().intValue()== E_TYPE.MESSAGE.getCode()){
				ChartUser ubase = (ChartUser) event.getObj();
				chat.setSender(ubase);
				chat.setSendTime(System.currentTimeMillis());//消息时间
				sendmsg(event,chat);
				ChartPool.uDate.add(chat);    //聊天记录存储
			}else if(chat.getMsgType() == E_TYPE.NOTICE.getCode()){
				/**发送聊天信息**/
				sendmsg(event,new ReInfo(E_TYPE.NOTICE.getCode(),chat));
				
			}else if(chat.getMsgType().intValue()==E_TYPE.GUILD_CHANGE.getCode()){
				//**修改自己工会信息**/
				chat.setSender((ChartUser)event.getObj());
				unitonChange(chat);
			}else if(chat.getMsgType().intValue()==E_TYPE.GUILD_CHANGE_MSG.getCode()){
				//**工会变更通知**/
				chat.setSender((ChartUser)event.getObj());
				unitonChangeNotice(chat);
			}else if(chat.getMsgType().intValue()==E_TYPE.HEART.getCode()){
				/** 心跳包  **/
			}
		}catch(Exception e){
			e.printStackTrace();
			event.onWebSocketClose(1001,null);
		}
	}


	@Override
	/*发送消息
	 * (non-Javadoc)
	 * @see com.ssmCore.jetty.I_SocketPress#sendmsg(com.ssmCore.jetty.EventSocket, java.lang.Object)
	 */
	public void sendmsg(EventSocket event, Object obj) {
		ChatEntity chat = (ChatEntity) obj;
		String gid = chat.getSender().getGid();
		Set<String> guids = null;
		if (chat.getChannel() == E_ChatChannel.ALL.getCode()) {
			guids = Defconstants.gidToGuid.get(chat.getSender().getGid());
		} else if (chat.getChannel() == E_ChatChannel.AREEA.getCode()) {
			guids = Defconstants.zidToGuid.get(chat.getGid()+chat.getZid());
		} else if(chat.getChannel() == E_ChatChannel.GUILD.getCode()){
			guids = Defconstants.untion.get(gid + "_" + chat.getSender().getGuildId());
		}
		if (guids != null && guids.size()>0) {
			Iterator<String> it = guids.iterator();
			while (it.hasNext()) {
				EventSocket socket = Defconstants.online.get(gid + "_" + it.next());
				if (socket != null)
					socket.sendMsg(JsonTransfer.getJson(new ReInfo(chat.getMsgType(),chat)));
			}
		}
	}
	
	/**
	 * 更新工会id
	 * @param chat
	 */
	public void unitonChange(ChatEntity chat){
		BaseDaoImpl db = BaseDaoImpl.getInstance();
		ChartUser cUser = chat.getSender();
		Update update = new Update();
		if(chat.getGuildId()==null || chat.getGuildId().trim().isEmpty()){
			update.unset("sender.guildId");
		}else{
			update.set("sender.guildId", chat.getGuildId());
		}
		db.saveOrUpdate(new Query(Criteria.where("sender.uid").is(cUser.getGuid()))
				,update, ChatEntity.class);
	}
	/**
	 * 工会变更通知
	 * @param chat
	 */
	public void unitonChangeNotice(ChatEntity chat){
		if(chat.getUids()!=null && chat.getUids().size()>0){
			String gid = chat.getSender().getGid();
			List<String> uids = JsonTransfer.conleObject(chat.getUids());
			chat.setUids(null);
			for(String uid : uids){
				EventSocket event = Defconstants.online.get(gid + "_" + uid);
				if(event!=null)
					sendmsg(event,chat);
			}
		}
	}
}
