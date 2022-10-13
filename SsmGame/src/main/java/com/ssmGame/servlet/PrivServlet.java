package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.lvgift.LvgiftImpl;
import com.ssmGame.module.monthcard.MonthcardImpl;

public class PrivServlet extends AdvancedServlet {
	private static final long serialVersionUID = 1L;
	
	public PrivServlet() {
        super(PrivServlet.class);
    }
	
    @FunUrl(value = I_DefMoudle.PRIV_FPAY)
    public CommonMsg onReqFirstPay(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	MonthcardImpl s = MonthcardImpl.getInstance();
        	respond = s.handleFirstReward(respond);
        	s.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_PRIV);
        }
        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.PRIV_INFO)
    public CommonMsg onReqInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	MonthcardImpl s = MonthcardImpl.getInstance();
        	respond = s.handleInfo(respond);
        	s.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_PRIV);
        }
        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.PRIV_CARD_MAIL)
    public CommonMsg onReqMail(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	MonthcardImpl s = MonthcardImpl.getInstance();
        	respond = s.handleDailyReward(respond);
        	s.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_PRIV);
        }
        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.PRIV_PAYBACK)
    public CommonMsg onReqPayback(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	MonthcardImpl s = MonthcardImpl.getInstance();
        	respond = s.handlePayback(respond);
        	s.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_PRIV);
        }
        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.PRIV_VIP_GIFT)
    public CommonMsg onReqVipGift(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	MonthcardImpl s = MonthcardImpl.getInstance();
        	respond = s.handleVipAward(respond, receive.body.priv.vip_id);
        	s.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_PRIV);
        }
        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.PRIV_LVGIFT_INFO)
    public CommonMsg onLvgiftInfo(CommonMsg receive) {
    	// 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	LvgiftImpl s = LvgiftImpl.getInstance();
        	respond = s.handleInfo(respond);
        	s.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_PRIV);
        }
        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.PRIV_LVGIFT_REWARD)
    public CommonMsg onLvgiftReward(CommonMsg receive) {
    	// 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	LvgiftImpl s = LvgiftImpl.getInstance();
        	respond = s.handleReward(respond);
        	s.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_PRIV);
        }
        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.PRIV_SPAY_REWARD)
    public CommonMsg onSpayReward(CommonMsg receive) {
    	// 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	MonthcardImpl s = MonthcardImpl.getInstance();
        	respond = s.handleSpayReward(respond, receive.body.priv.reward_id);
        	s.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_PRIV);
        }
        return respond;
    }
}
