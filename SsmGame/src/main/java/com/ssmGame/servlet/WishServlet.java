package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.wish.WishImpl;

public class WishServlet extends AdvancedServlet {
	private static final long serialVersionUID = 1L;
	
	public WishServlet() {
		super(WishServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.WISH_INFO)
    public CommonMsg onReqInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {	
        	WishImpl a = WishImpl.getInstance();
        	respond = a.handleInfo(respond);
        	a.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_WISH);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.WISH_EXEC)
    public CommonMsg onReqWish(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {	
        	WishImpl a = WishImpl.getInstance();
        	respond = a.handleWish(respond, receive.body.wish.role_id);
        	a.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_WISH);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.WISH_EXEC_ONEKEY)
    public CommonMsg onReqWishOneKey(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {	
        	WishImpl a = WishImpl.getInstance();
        	respond = a.handleOneKeyWish(respond, receive.body.wish.role_id);
        	a.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_WISH);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.WISH_SEL)
    public CommonMsg onReqWishRole(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {	
        	WishImpl a = WishImpl.getInstance();
        	respond = a.handleRole(respond, receive.body.wish.role_id);
        	a.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_WISH);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.WISH_LVUP_ITEM)
    public CommonMsg onReqWishItemLvUp(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {	
        	WishImpl a = WishImpl.getInstance();
        	respond = a.handleItemLvUp(respond);
        	a.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_WISH);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.WISH_LVUP_DIAMOND)
    public CommonMsg onReqWishDmdLvUp(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {	
        	WishImpl a = WishImpl.getInstance();
        	respond = a.handleDmdLvUp(respond);
        	a.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_WISH);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.WISH_LVUP_ONEKEY)
    public CommonMsg onReqWishOneLevelLvUp(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {	
        	WishImpl a = WishImpl.getInstance();
        	respond = a.handleOneLevelLvUp(respond);
        	a.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_WISH);
        }
        return respond;
    }
}
