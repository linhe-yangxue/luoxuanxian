package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.activity.VipGiftImpl;

public class VipGiftServlet extends AdvancedServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VipGiftServlet() {
		super(VipGiftServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.ACT_VIP_WEEKLY_INFO)
    public CommonMsg onVipGiftInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	VipGiftImpl m = VipGiftImpl.getInstance();
        	respond = m.handleInfo(respond);
        	m.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_VIPGIFT);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ACT_VIP_WEEKLY_BUY)
    public CommonMsg onVipGiftBuy(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	VipGiftImpl m = VipGiftImpl.getInstance();
        	respond = m.handleBuy(respond, receive.body.activity.gift_id);
        	m.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_VIPGIFT);
        }
        return respond;
    }
}
