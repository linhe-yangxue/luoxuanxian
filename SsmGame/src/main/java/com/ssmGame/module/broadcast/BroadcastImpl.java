package com.ssmGame.module.broadcast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.colligate.HttpRequest;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmShare.constants.E_ChatChannel;
import com.ssmShare.constants.E_TYPE;
import com.ssmShare.entity.ChatEntity;

@Service
@Scope("prototype")
public class BroadcastImpl {
	
	public static final String ROLE_BREACH = "notice1";
	public static final String ROLE_TALENT = "notice2";
	public static final String ROLE_AWAKEN = "notice3";
	public static final String GET_ROLE_4_5_STAR = "notice4";
	public static final String ARENA_CHAP = "notice5";
	public static final String VIP = "notice6";
	public static final String INSTANCE_PASS = "notice7";
	public static final String FULLBOSS_KILL = "notice9";
	public static final String TOWERBOSS_KILL = "notice8";
	
	private static final Logger log = LoggerFactory.getLogger(BroadcastImpl.class);
	
	private @Value("${CHAT_RUL}") String chat_url; /*聊天地址*/
	
	public final static BroadcastImpl getInstance(){
        return SpringContextUtil.getBean(BroadcastImpl.class);
	}
	
	public void SendBrocast(String context, String gid, int zid)
	{
		try
		{
			ChatEntity entity = new ChatEntity();
			entity.setContext(context);
			entity.setMsgType(E_TYPE.NOTICE.getCode());
			entity.setGid(gid);
			entity.setChannel(E_ChatChannel.AREEA.getCode());
			entity.setZid(zid);
			String result = HttpRequest.PostFunction(chat_url + "/push", JsonTransfer.getJson(entity));
			if (null == result)
			{
				log.warn("BroadcastImpl.SendBrocast ERROR!");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
