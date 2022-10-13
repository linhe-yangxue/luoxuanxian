package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.equip.EquipImpl;

public class JewelryServlet extends AdvancedServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JewelryServlet() {
		super(JewelryServlet.class);
	}

    @FunUrl(value = I_DefMoudle.JEWELRY_STR)
    public CommonMsg onStr(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.handleJewelyStr(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_JEWELRY);
        }

        return respond;
    }

    @FunUrl(value = I_DefMoudle.JEWELRY_STR_AUTO)
    public CommonMsg onStrAuto(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.handleJewelyStrAuto(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_JEWELRY);
        }

        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.JEWELRY_EVO)
    public CommonMsg onEvo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.handleJewelyEvo(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_JEWELRY);
        }

        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.JEWELRY_EVO_AUTO)
    public CommonMsg onEvoAuto(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
            EquipImpl eq = EquipImpl.getInstance().init(receive.header.uid);
            respond = eq.handleJewelyEvoAuto(respond, receive);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_JEWELRY);
        }

        return respond;
    }
}
