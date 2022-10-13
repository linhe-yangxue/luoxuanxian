package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.daily.DailyTaskImpl;

public class DailytaskServlet extends AdvancedServlet {
	
	private static final long serialVersionUID = 1L;
	
	public DailytaskServlet() {
		super(DailytaskServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.DAILY_TASK_INFO)
    public CommonMsg onReqInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DailyTaskImpl d = DailyTaskImpl.getInstance();
        	respond = d.handleInfo(respond);
        	d.Destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DAILY_TASK);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DAILY_TASK_UPGRADE)
    public CommonMsg onReqUpgrade(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DailyTaskImpl d = DailyTaskImpl.getInstance();
        	respond = d.handleLevelup(respond);
        	d.Destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DAILY_TASK);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DAILY_TASK_REWARD)
    public CommonMsg onReqReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DailyTaskImpl d = DailyTaskImpl.getInstance();
        	respond = d.handleGetReward(respond, receive.body.daily_task.reward_id);
        	d.Destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DAILY_TASK);
        }
        return respond;
    }
}
