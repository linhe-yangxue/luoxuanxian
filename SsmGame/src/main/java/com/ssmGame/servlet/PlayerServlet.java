package com.ssmGame.servlet;


import java.util.Calendar;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.defdata.msg.player.PlayerLoginMsg;
import com.ssmGame.manager.ArenaManager;
import com.ssmGame.module.draw.DrawCardImpl;
import com.ssmGame.module.instance.InstanceImpl;
import com.ssmGame.module.player.PlayerImpl;
import com.ssmGame.module.pve.PveImpl;
import com.ssmGame.module.scroll.ScrollImpl;


public class PlayerServlet extends AdvancedServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlayerServlet() {
		super(PlayerServlet.class);
	}

	/*@FunUrl(value = I_DefMoudle.PLAYER_LOGIN)
    public CommonMsg onPlayerLogin(CommonMsg receive){
		
		if (receive.body.playerLogin == null)
		{
			return CommonMsg.err(MsgCode.MSG_INCORRECT_PARAM);
		}
		
		PlayerLoginMsg respond_body = PlayerImpl.getInstance().login(receive.body.playerLogin.username);
		CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
		respond.body.playerLogin = respond_body;
		
		DrawCardImpl draw = DrawCardImpl.getInstance().initInLogin(respond_body._id);
		respond = draw.handleLogin(respond);
		draw.destroy();
		
		InstanceImpl inst = InstanceImpl.getInstance().init(respond_body._id);
		respond = inst.handleLogin(respond);
		inst.destroy();
		
		if (respond_body.is_new)
		{
			ScrollImpl scroll = ScrollImpl.getInstance().initInLogin(respond_body._id);
			respond = scroll.handleNewPlayer(respond);
			scroll.destroy();
		}
		else
		{
			ScrollImpl scroll = ScrollImpl.getInstance().initInLogin(respond_body._id);
			respond = scroll.handleLogin(respond);
			scroll.destroy();
		}
		
		respond_body.server_now = Calendar.getInstance().getTime().getTime();
		return respond;
	}*/
	
	@FunUrl(value = I_DefMoudle.PLAYER_CREATE)
    public CommonMsg onPlayerCreate(CommonMsg receive){
		if (receive.body.playerLogin == null)
		{
			return CommonMsg.err(MsgCode.MSG_INCORRECT_PARAM);
		}
		
		int code = PlayerImpl.getInstance().create(receive.header.uid, receive.body.playerLogin.nickname
				, receive.body.playerLogin.icon_url) ? MsgCode.SUCCESS : MsgCode.MSG_INCORRECT_PARAM;
		
		CommonMsg respond = new CommonMsg(code, receive.header.uid);
		return respond;
	}
	
	@FunUrl(value = I_DefMoudle.PLAYER_PLATFORM)
	public CommonMsg platformLogin(CommonMsg receive){
		PlayerLoginMsg respond_body = null;
		if (receive.header.mkey == null && receive.header.zid == null){
			return CommonMsg.err(MsgCode.MSG_INCORRECT_PARAM);
		}
		respond_body = PlayerImpl.getInstance()
				.platLogin(receive.header);
		if (respond_body == null)
		{
			return null;
		}
		
		CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
		respond.body.playerLogin = respond_body;
		
		DrawCardImpl draw = DrawCardImpl.getInstance().initInLogin(respond_body._id);
		respond = draw.handleLogin(respond);
		draw.destroy();
		
		InstanceImpl inst = InstanceImpl.getInstance().init(respond_body._id);
		respond = inst.handleLogin(respond);
		inst.destroy();
		
		if (respond_body.is_new)
		{
			ScrollImpl scroll = ScrollImpl.getInstance().initInLogin(respond_body._id);
			respond = scroll.handleNewPlayer(respond);
			scroll.destroy();
			
			/*ArenaImpl a_i = ArenaImpl.getInstance().init(respond_body._id);
			a_i.handleNewPlayer(respond_body);
			a_i.destroy();*/
		}
		else
		{
			ScrollImpl scroll = ScrollImpl.getInstance().initInLogin(respond_body._id);
			respond = scroll.handleLogin(respond);
			scroll.destroy();
		}
		
		respond_body.server_now = Calendar.getInstance().getTime().getTime();
		respond_body.server_start = ArenaManager.ServerStartDate().getTimeInMillis();
		return respond;
	}

	/**
	 * 请求玩家个人信息
	 * @param receive
	 * @return
	 */
	@FunUrl(value = I_DefMoudle.PLAYER_INFO)
	public CommonMsg onReqQuick(CommonMsg receive){

		// 初始化PveImpl
		PveImpl p = PveImpl.getInstance().init(receive.header.uid);

		// 构造返回消息
		CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);

		try {
			respond = p.reqPlayerInfo(respond);
		}catch(Exception e){
			e.printStackTrace();
			respond = CommonMsg.err(MsgCode.DESIGN_ERR_PVE);
		}

		// 回收PveImpl
		p.destroy();

		return respond;
	}
	
	
}
