package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.draw.DrawCardImpl;

public class DrawCardServlet extends AdvancedServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DrawCardServlet() {
		super(DrawCardServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.DRAW_CARD_ITEM_ONE)
    public CommonMsg onReqDrawItemOne(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DrawCardImpl draw = DrawCardImpl.getInstance().init(receive.header.uid);
        	respond = draw.ItemDraw(respond, 1);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DRAW);
        }
        
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DRAW_CARD_ITEM_TEN)
    public CommonMsg onReqDrawItemTen(CommonMsg receive){

        // 构造返回消息
		CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DrawCardImpl draw = DrawCardImpl.getInstance().init(receive.header.uid);
        	respond = draw.ItemDraw(respond, 10);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DRAW);
        }
        
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DRAW_CARD_DIAMOND_ONE)
    public CommonMsg onReqDrawDmdOne(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DrawCardImpl draw = DrawCardImpl.getInstance().init(receive.header.uid);
        	respond = draw.DiamondDraw(respond, 1);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DRAW);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DRAW_CARD_DIAMOND_TEN)
    public CommonMsg onReqDrawDmdTen(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DrawCardImpl draw = DrawCardImpl.getInstance().init(receive.header.uid);
        	respond = draw.DiamondDraw(respond, 10);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DRAW);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DRAW_CARD_LIMIT_ONE)
    public CommonMsg onReqDrawLimitOne(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DrawCardImpl draw = DrawCardImpl.getInstance().init(receive.header.uid);
        	respond = draw.LimitDraw(respond, 1);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DRAW);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DRAW_CARD_LIMIT_TEN)
    public CommonMsg onReqDrawLimitTen(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DrawCardImpl draw = DrawCardImpl.getInstance().init(receive.header.uid);
        	respond = draw.LimitDraw(respond, 10);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DRAW);
        }
        return respond;
    }
}
