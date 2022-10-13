package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.activity.ExRoleGiftImpl;

public class ExroleGiftServlet extends AdvancedServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExroleGiftServlet() {
		super(ExroleGiftServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.ACT_EX_ROLE_INFO)
    public CommonMsg onInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	ExRoleGiftImpl m = ExRoleGiftImpl.getInstance();
        	respond = m.handleInfo(respond);
        	m.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_EXROLE_GIFT);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ACT_EX_ROLE_BUY)
    public CommonMsg onBuy(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	ExRoleGiftImpl m = ExRoleGiftImpl.getInstance();
        	respond = m.handleBuy(respond, receive);
        	m.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_EXROLE_GIFT);
        }
        return respond;
    }
}
