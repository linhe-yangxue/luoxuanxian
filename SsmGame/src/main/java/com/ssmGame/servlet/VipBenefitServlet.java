package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.activity.VipBenefitImpl;

public class VipBenefitServlet extends AdvancedServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VipBenefitServlet() {
		super(VipBenefitServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.ACT_VIP_BENEFIT_INFO)
    public CommonMsg onVipBenefitInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	VipBenefitImpl m = VipBenefitImpl.getInstance();
        	respond = m.handleInfo(respond, receive);
        	m.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_VIPBENEFIT);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ACT_VIP_BENEFIT_EXEC)
    public CommonMsg onVipBenefitExec(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	VipBenefitImpl m = VipBenefitImpl.getInstance();
        	respond = m.handleExec(respond, receive);
        	m.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_VIPBENEFIT);
        }
        return respond;
    }
}
