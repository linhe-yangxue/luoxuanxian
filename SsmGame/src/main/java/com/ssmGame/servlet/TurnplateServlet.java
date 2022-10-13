package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.activity.TurnplateImpl;

public class TurnplateServlet extends AdvancedServlet {
	private static final long serialVersionUID = 1L;
	
	public TurnplateServlet() {
		super(TurnplateServlet.class);
	}
	
	/*@FunUrl(value = I_DefMoudle.ACT_LIMIT_GIFT_INFO)
    public CommonMsg onReqInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {	
        	LimitGiftActivityImpl a = LimitGiftActivityImpl.getInstance();
        	respond = a.handleInfo(respond);
        	a.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }*/
	
	@FunUrl(value = I_DefMoudle.ACT_TURN_PLATE_EXEC)
    public CommonMsg onReqReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	TurnplateImpl a = TurnplateImpl.getInstance();
        	respond = a.handleRoll(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
}
