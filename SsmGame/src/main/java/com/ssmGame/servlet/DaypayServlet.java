package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.activity.DaypayImpl;

public class DaypayServlet extends AdvancedServlet {
	private static final long serialVersionUID = 1L;
	
	public DaypayServlet() {
		super(DaypayServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.ACT_DAY_PAY_INFO)
    public CommonMsg onReqInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	DaypayImpl a = DaypayImpl.getInstance();
        	respond = a.handleInfo(respond);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ACT_DAY_PAY_REWARD)
    public CommonMsg onReqReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	DaypayImpl a = DaypayImpl.getInstance();
        	respond = a.handleReward(respond, receive.body.activity.reward_id);
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
}
