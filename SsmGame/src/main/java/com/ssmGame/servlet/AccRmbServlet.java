package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.activity.AccRmbImpl;

public class AccRmbServlet extends AdvancedServlet {
	private static final long serialVersionUID = 1L;
	
	public AccRmbServlet() {
		super(AccRmbServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.ACT_ACC_RMB_INFO)
    public CommonMsg onReqInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	AccRmbImpl a = AccRmbImpl.getInstance();
        	respond = a.handleInfo(respond);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ACT_ACC_RMB_REWARD)
    public CommonMsg onReqReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	AccRmbImpl a = AccRmbImpl.getInstance();
        	respond = a.handleReward(respond, receive.body.activity.reward_id);
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
}
