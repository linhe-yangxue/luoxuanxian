package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.dmgreward.DmgRewardImpl;

public class DmgRewardServlet extends AdvancedServlet {
	private static final long serialVersionUID = 1L;
	
	public DmgRewardServlet() {
		super(DmgRewardServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.DMG_INFO)
    public CommonMsg onReqInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try { 	
        	DmgRewardImpl a = DmgRewardImpl.getInstance();
        	respond = a.handleInfo(respond);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DMG);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DMG_REWARD)
    public CommonMsg onReqReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	DmgRewardImpl a = DmgRewardImpl.getInstance();
        	respond = a.handleReward(respond, receive.body.dmg.reward_id);
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DMG);
        }
        return respond;
    }
}
