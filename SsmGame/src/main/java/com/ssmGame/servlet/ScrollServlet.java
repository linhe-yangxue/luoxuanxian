package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.scroll.ScrollImpl;

public class ScrollServlet extends AdvancedServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ScrollServlet() {
		super(ScrollServlet.class);
		// TODO Auto-generated constructor stub
	}

	@FunUrl(value = I_DefMoudle.SCROLL_BUY)
    public CommonMsg onReqBuy(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	ScrollImpl s = ScrollImpl.getInstance().init(receive.header.uid);
        	respond = s.buyScroll(respond, receive.body.scroll.scroll_id);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SCROLL);
        }
        return respond;
    }
}
