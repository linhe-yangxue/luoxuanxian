package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.task.TaskImpl;

public class TaskServlet extends AdvancedServlet {
	private static final long serialVersionUID = 1L;
	
	public TaskServlet() {
		super(TaskServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.TASK_INFO)
    public CommonMsg onReqInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	TaskImpl t = TaskImpl.getInstance();
        	t.handleInfo(respond);
        	t.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_TASK);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.TASK_REWARD)
    public CommonMsg onReqReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	TaskImpl t = TaskImpl.getInstance();
        	t.handleReward(respond, receive.body.task.cur_id);
        	t.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_TASK);
        }
        return respond;
    }
}
