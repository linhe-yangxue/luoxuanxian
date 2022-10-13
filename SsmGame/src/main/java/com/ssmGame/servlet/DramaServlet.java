package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.drama.DramaImpl;

public class DramaServlet extends AdvancedServlet {
	private static final long serialVersionUID = 1L;
	
	public DramaServlet() {
		super(DramaServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.DRAMA_BATTLE)
    public CommonMsg onReqBattle(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	DramaImpl a = DramaImpl.getInstance();
        	respond = a.handleBattle(respond);
        	a.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DRAMA);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DRAMA_FIN_BATTLE)
    public CommonMsg onReqBattleEnd(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	DramaImpl a = DramaImpl.getInstance();
        	respond = a.handleBattleEnd(respond);
        	a.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DRAMA);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DRAMA_FIN_DIALOG)
    public CommonMsg onReqDialogueEnd(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	DramaImpl a = DramaImpl.getInstance();
        	respond = a.handleDialogueEnd(respond);
        	a.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DRAMA);
        }
        return respond;
    }
}
