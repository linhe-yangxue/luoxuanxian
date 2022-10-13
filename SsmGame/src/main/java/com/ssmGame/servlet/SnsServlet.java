package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.sns.SnsImpl;

public class SnsServlet extends AdvancedServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SnsServlet() {
		super(SnsServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.SNS_FOLLOW_REWARD)
    public CommonMsg onSnsFollowReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	respond = SnsImpl.getInstance().handleFollowAward(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SNS);
        }
        
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SNS_INVITE_ONCE)
    public CommonMsg onSnsInviteOnce(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	respond = SnsImpl.getInstance().handleInviteOnce(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SNS);
        }
        
        return respond;
    }

	@FunUrl(value = I_DefMoudle.SNS_INVITE_REWARD)
    public CommonMsg onSnsInviteReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	respond = SnsImpl.getInstance().handleInviteReward(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SNS);
        }
        
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SNS_QQ_SHORTCUT_ACCOMPLISH)
    public CommonMsg onQQCutChg(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	respond = SnsImpl.getInstance().handleQQCutStatusChg(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SNS);
        }
        
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SNS_QQ_SHORTCUT_REWARD)
    public CommonMsg onQQCutReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	respond = SnsImpl.getInstance().handleQQCutReward(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SNS);
        }
        
        return respond;
    }
}
