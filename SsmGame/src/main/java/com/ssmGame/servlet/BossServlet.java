package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.boss.BossImpl;

public class BossServlet extends AdvancedServlet{
	//private static final Logger log = LoggerFactory.getLogger(BossServlet.class);
	private static final long serialVersionUID = 1L;

	public BossServlet() {
		super(BossServlet.class);
	}


	/**
	 * 响应请求战斗
	 * @param msg
	 * @return
	 */
	@FunUrl(value = I_DefMoudle.BOSS_SB_BATTLE)
	public CommonMsg onSingleBattle(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {	
        	respond = BossImpl.handleSingleFight(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_BOSS);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.BOSS_SB_REWARD)
	public CommonMsg onSingleReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {	
        	respond = BossImpl.handleSingleReward(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_BOSS);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.BOSS_FB_INFO)
	public CommonMsg onFBInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {	
        	respond = BossImpl.handleFbInfo(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_BOSS);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.BOSS_FB_BATTLE)
	public CommonMsg onFBBattle(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {	
        	respond = BossImpl.handleFullBossFight(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_BOSS);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.BOSS_FB_KILLER)
	public CommonMsg onFBKiller(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {	
        	respond = BossImpl.handleFBKiller(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_BOSS);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.BOSS_FB_DAMAGE)
	public CommonMsg onFBDmg(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {	
        	respond = BossImpl.handleFBDamage(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_BOSS);
        }
        return respond;
    }
}
