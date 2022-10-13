package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.send.SendImpl;

public class SendServlet extends AdvancedServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SendServlet() {
		super(SendServlet.class);
		// TODO Auto-generated constructor stub
	}

	@FunUrl(value = I_DefMoudle.SEND_QUEST_INFO)
    public CommonMsg onReqBuy(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleReqQuestInfo(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SEND_QUEST_REFRESH)
    public CommonMsg onReqQuestRefresh(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleRefreshQuest(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SEND_QUEST_EXEC)
    public CommonMsg onReqQuestExec(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleQuestExc(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SEND_QUEST_OPINION)
    public CommonMsg onQuestOp(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleQuestOp(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SEND_QUEST_REWARD)
    public CommonMsg onQuestReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleQuestReward(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SEND_QUEST_QUICK)
    public CommonMsg onQuestQuick(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleQuestQuick(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SEND_ROB_TEAMS)
    public CommonMsg onRobTeams(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleRobTeams(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SEND_ROB_REFRESH)
    public CommonMsg onRobRefresh(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleRobRefresh(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SEND_ROB_REVENGERS)
    public CommonMsg onRobRevengers(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleRobRevengers(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SEND_ROB_EXEC)
    public CommonMsg onRobExec(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleRobExec(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SEND_LOG_LIST)
    public CommonMsg onRobLogList(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleLogList(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SEND_LOG_REPLAY)
    public CommonMsg onLogReplay(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleLogReplay(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SEND_UTIL_AVATAR)
    public CommonMsg onAvatar(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleAvatar(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
}
