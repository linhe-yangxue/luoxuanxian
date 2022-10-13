package com.ssmchat.servlet;

import java.util.Iterator;
import java.util.Set;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.jetty.BaseServlet;
import com.ssmCore.jetty.EventSocket;
import com.ssmCore.jetty.FunUrl;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmShare.constants.E_TYPE;
import com.ssmShare.entity.ChatEntity;
import com.ssmchat.constanst.Defconstants;
import com.ssmchat.constanst.I_DefMoudle;
import com.ssmchat.entity.HttpParamString;

public class SysPushServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	public SysPushServlet() {
		super(SysPushServlet.class, new HttpParamString());
	}

	@FunUrl(value = I_DefMoudle.MSG_PUSH)
	public Object pushMessage(ChatEntity chat) {
		Set<String> guid = null;
		if(chat.getChannel()==null)
			new ReInfo(-1,"频道不存在！");
//		if (chat.getGid() == null || chat.getZid() == null) {
//			WebsocktLogin.getInstance().playerPush(chat);
//		}
		if (chat.getChannel() == 0 && chat.getGid() != null) {
			guid = Defconstants.gidToGuid.get(chat.getGid());
		} else if (chat.getChannel() == 1 && chat.getGid() != null && chat.getZid() != null) {
			guid = Defconstants.zidToGuid.get(chat.getGid() + "_" + chat.getZid());
		}
		if (guid != null) {
			Iterator<String> it = guid.iterator();
			while (it.hasNext()) {
				EventSocket socket = Defconstants.online.get(chat.getGid()+ "_" + it.next());
				if (socket != null)
					socket.sendMsg(JsonTransfer.getJson(new ReInfo(E_TYPE.NOTICE.getCode(), chat)));
			}
			return new ReInfo("消息发送完成！");
		}
		return new ReInfo(-1,"消息发送错误！");
	}
	/**
	 * 支付成功消息发送
	 * @param chat
	 * @return
	 */
	@FunUrl(value = I_DefMoudle.PAY_SUCCESS)
	public Object PaySuccessMsg(ChatEntity chat){
		System.out.println("pay success jieou");
		if (chat.getMsgType() == E_TYPE.PAY_SUCESS_MSG.getCode()){
			EventSocket socket = Defconstants.online.get(chat.getGid()+ "_" + chat.getUid());
			System.out.println(socket);
			if (socket != null){
				socket.sendMsg(JsonTransfer.getJson(new ReInfo(E_TYPE.PAY_SUCESS_MSG.getCode(), chat)));
				System.out.println("用户充值完成");
				chat.setContext("用户充值已完成！");
				return new ReInfo(chat.getContext()+ "|"+ chat.getUid() );
			}else{
				System.out.println("该用户不在线");
				return new ReInfo(-2,"该用户不在线！");
			}
		}
        System.out.println("该消息类型不正确 消息不合法");
		return new ReInfo(-3,"该消息类型不正确！消息不合法。");
	}
}
