package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.activity.DurationImpl;

public class DurationServlet extends AdvancedServlet {
	private static final long serialVersionUID = 1L;
	
	public DurationServlet() {
		super(DurationServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.ACT_DURATION_INFO)
    public CommonMsg onReqInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	DurationImpl a = DurationImpl.getInstance();
        	respond = a.handleInfo(respond);
        	a.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ACT_DURATION_REWARD)
    public CommonMsg onReqReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	DurationImpl a = DurationImpl.getInstance();
        	respond = a.handleReward(respond, receive.body.activity.reward_id);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
}
