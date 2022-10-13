package com.ssmGame.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.battle.BattleResultMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.battle.BattleImpl;
import com.ssmGame.module.battle.BattlePack;

public class BattleServlet extends AdvancedServlet{
	private static final Logger log = LoggerFactory.getLogger(BattleServlet.class);
	private static final long serialVersionUID = 1L;

	public BattleServlet() {
		super(BattleServlet.class);
	}


	/**
	 * 响应请求战斗
	 * @param msg
	 * @return
	 */
	@FunUrl(value = I_DefMoudle.BATTLE_REQ)
	public CommonMsg onReqBatte(CommonMsg msg){
		
		//log.info("onReqBatte id{}" + msg.header.uid);
		// TODO 具体战斗模拟逻辑
		BattlePack bp = BattleImpl.getInstance().simulateTest(msg.header.uid);
		if (null == bp)
		{
			log.info("onReqBatte ERROR id{}" + msg.header.uid);
			return CommonMsg.err(MsgCode.MSG_INCORRECT_PARAM);
		}
			

		// 构造战斗结果消息体
		BattleResultMsg battleResultMsg = new BattleResultMsg();
		battleResultMsg.pack = bp;

		// 构造返回消息
		CommonMsg resMsg = new CommonMsg(MsgCode.SUCCESS, msg.header.uid);
		resMsg.body.battleResult = battleResultMsg;
		
		System.out.println("Send " + resMsg.body.battleResult.pack.m_actors_script.size());
		
		// 返回消息
		return resMsg;
	}
	
}
