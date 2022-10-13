package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.equip.EquipImpl;

public class EquipServlet extends AdvancedServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EquipServlet() {
		super(EquipServlet.class);
	}

    /**
     * 装备合成
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.EQUIP_COMPOSE)
    public CommonMsg onReqCompose(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.HandleAddNewItem(respond, receive.body.role_equip.equip_id);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }

    /**
     * 装备穿戴
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.EQUIP_CHANGE)
    public CommonMsg onReqChange(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.HandleChange(respond, receive.body.role_equip.pos
            		, receive.body.role_equip.equip_id, receive.body.role_equip.role_id);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }

    /**
     * 一键装备
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.EQUIP_CHANGE_AUTO)
    public CommonMsg onReqChangeAuto(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.HandleOneKeyEquip(respond, receive.body.role_equip.role_id);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }

    /**
     * 卸下装备
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.EQUIP_UNLOAD)
    public CommonMsg onReqUnload(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.HandleTakeOff(respond, receive.body.role_equip.pos
            		, receive.body.role_equip.role_id);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }

    /**
     * 装备强化
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.EQUIP_STRENGTH)
    public CommonMsg onReqStrength(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.HandleStrength(respond, receive.body.role_equip.pos
            		, receive.body.role_equip.role_id);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }

    /**
     * 一键强化
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.EQUIP_STRENGTH_AUTO)
    public CommonMsg onReqStrengthAuto(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.HandleOneKeyStrength(respond, receive.body.role_equip.role_id);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }

    /**
     * 装备精炼
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.EQUIP_REFINE)
    public CommonMsg onReqRefine(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.HandleRefine(respond, receive.body.role_equip.pos
            		, receive.body.role_equip.role_id);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }

    /**
     * 装备分解
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.EQUIP_RESOLVE)
    public CommonMsg onReqResolve(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.HandleResolve(respond, receive.body.role_equip.equips);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }
    
    /**
     * 装备附魔升级
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.EQUIP_ENCHANT_LV)
    public CommonMsg onReqEnchantLv(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.HandleEnchantLv(respond, receive.body.role_equip.pos, receive.body.role_equip.role_id);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }
    
    /**
     * 装备附魔突破
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.EQUIP_ENCHANT_BREACH)
    public CommonMsg onReqEnchantBreach(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.HandleEnchantBreach(respond, receive.body.role_equip.pos, receive.body.role_equip.role_id);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.EQUIP_ENCHANT_BREACH_DMD)
    public CommonMsg onReqEnchantBreachDmd(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.HandleEnchantBreachDmd(respond, receive.body.role_equip.pos, receive.body.role_equip.role_id);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }
    
    /**
     * 装备附魔洗练属性
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.EQUIP_ENCHANT_WASH)
    public CommonMsg onReqEnchantWash(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.HandleEnchantWash(respond, receive.body.role_equip.pos, receive.body.role_equip.role_id);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }
    
    /**
     * 装备附魔换属性
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.EQUIP_ENCHANT_CHANGE)
    public CommonMsg onReqEnchantChange(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.HandleEnchantChange(respond, receive.body.role_equip.pos, receive.body.role_equip.role_id);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }
    
    /**
     * 装备一键附魔
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.EQUIP_ENCHANT_ONE_KEY)
    public CommonMsg onReqEnchantOneKey(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.HandleEnchantOneKey(respond, receive.body.role_equip.pos, receive.body.role_equip.role_id);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.EQUIP_BAG_INFO)
    public CommonMsg onReqBagInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.HandleBagInfo(respond);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.EQUIP_BAG_GIFT_USE_SWITCH)
    public CommonMsg onReqSelectPack(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.handleSelectPack(respond, receive.body.role_equip.item_id, receive.body.role_equip.sel_id);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.EQUIP_BAG_GIFT_USE_NORMAL)
    public CommonMsg onReqNormalPack(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.handleItemPack(respond, receive.body.role_equip.item_id, receive.body.role_equip.count);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.EQUIP_BAG_GIFT_USE_RANDOM)
    public CommonMsg onReqRandomPack(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.handleRandomPack(respond, receive.body.role_equip.item_id, receive.body.role_equip.count);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.EQUIP_ENCHANT_CHANGE_CONST)
    public CommonMsg onReqChangeConst(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.handleChangeConst(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.EQUIP_EXCHANGE_LV)
    public CommonMsg onReqExchange(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.handleExchange(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.EQUIP_BAG_ITEM_COMPOSE)
    public CommonMsg onReqItemCompose(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.handleItemCompose(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_EQUIP);
        }

        return respond;
    }
}
