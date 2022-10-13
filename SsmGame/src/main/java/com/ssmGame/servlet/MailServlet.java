package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.mail.MailImpl;

public class MailServlet extends AdvancedServlet {
	private static final long serialVersionUID = 1L;

	public MailServlet() {
        super(MailServlet.class);
    }

    /**
    @FunUrl(value = "/mail/add")
    public CommonMsg onReqAddMail(CommonMsg receive){

        if(receive.body.mail.r_gold != 9302){
            return CommonMsg.err(MsgCode.DESIGN_ERR_MAIL);
        }

        // 构造返回消息
        MailImpl m = MailImpl.getInstance().init(receive.header.uid);
        m.AddMyMail(receive.body.mail.mail_id, receive.body.pveReward.reward_hash);
        return CommonMsg.err(0);
    }
     */

	@FunUrl(value = I_DefMoudle.MAIL_CHECK)
    public CommonMsg onReqCheck(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	MailImpl m = MailImpl.getInstance().init(receive.header.uid);
        	respond = m.handleReqNotRead(respond);
        	m.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_MAIL);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.MAIL_LIST)
    public CommonMsg onReqList(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	MailImpl m = MailImpl.getInstance().init(receive.header.uid);
        	respond = m.handleReqAllMail(respond);
        	m.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_MAIL);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.MAIL_READ)
    public CommonMsg onReqRead(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	MailImpl m = MailImpl.getInstance().init(receive.header.uid);
        	respond = m.handleReqRead(respond, receive.body.mail.mail_id);
        	m.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_MAIL);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.MAIL_REWARD)
    public CommonMsg onReqReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	MailImpl m = MailImpl.getInstance().init(receive.header.uid);
        	respond = m.handleReqGetReward(respond, receive.body.mail.mail_id);
        	m.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_MAIL);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.MAIL_REWARD_ONEKEY)
    public CommonMsg onReqOnekeyReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	MailImpl m = MailImpl.getInstance().init(receive.header.uid);
        	respond = m.handleReqOnekeyReward(respond);
        	m.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_MAIL);
        }
        return respond;
    }
}
