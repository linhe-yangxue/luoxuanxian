package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.activity.EnchantInvestImpl;
import com.ssmGame.module.activity.InvestImpl;
import com.ssmGame.module.activity.JewelryInvestImpl;
import com.ssmGame.module.activity.RoleInvestImpl;

public class InvestActivityServlet extends AdvancedServlet {
	private static final long serialVersionUID = 1L;
	
	public InvestActivityServlet() {
		super(InvestActivityServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.ACT_INVEST_INFO)
    public CommonMsg onReqInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	InvestImpl a = InvestImpl.getInstance();
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
	
	@FunUrl(value = I_DefMoudle.ACT_INVEST_REWARD)
    public CommonMsg onReqReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	InvestImpl a = InvestImpl.getInstance();
        	respond = a.handleReward(respond, receive.body.activity.reward_id);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ACT_INVEST_ROLE_INFO)
    public CommonMsg onRoleInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	RoleInvestImpl a = RoleInvestImpl.getInstance();
        	respond = a.handleInfo(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ACT_INVEST_ROLE_BUY)
    public CommonMsg onRoleBuy(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	RoleInvestImpl a = RoleInvestImpl.getInstance();
        	respond = a.handleBuy(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ACT_INVEST_ROLE_REWARD)
    public CommonMsg onRoleReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	RoleInvestImpl a = RoleInvestImpl.getInstance();
        	respond = a.handleReward(respond, receive);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ACT_INVEST_ENCHANT_INFO)
    public CommonMsg onEnchantInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	EnchantInvestImpl a = EnchantInvestImpl.getInstance();
        	respond = a.handleInfo(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ACT_INVEST_ENCHANT_BUY)
    public CommonMsg onEnchantBuy(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	EnchantInvestImpl a = EnchantInvestImpl.getInstance();
        	respond = a.handleBuy(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ACT_INVEST_ENCHANT_REWARD)
    public CommonMsg onEnchantReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	EnchantInvestImpl a = EnchantInvestImpl.getInstance();
        	respond = a.handleReward(respond, receive);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ACT_INVEST_JEWELRY_INFO)
    public CommonMsg onJewelryInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	JewelryInvestImpl a = JewelryInvestImpl.getInstance();
        	respond = a.handleInfo(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ACT_INVEST_JEWELRY_BUY)
    public CommonMsg onJewelryBuy(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	JewelryInvestImpl a = JewelryInvestImpl.getInstance();
        	respond = a.handleBuy(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ACT_INVEST_JEWELRY_REWARD)
    public CommonMsg onJewelryReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	JewelryInvestImpl a = JewelryInvestImpl.getInstance();
        	respond = a.handleReward(respond, receive);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ACTIVITY);
        }
        return respond;
    }
}
