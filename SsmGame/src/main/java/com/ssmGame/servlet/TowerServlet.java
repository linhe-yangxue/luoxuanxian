package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.tower.TowerImpl;

public class TowerServlet extends AdvancedServlet {
	private static final long serialVersionUID = 1L;
	
	public TowerServlet() {
		super(TowerServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.TOWER_INFO)
    public CommonMsg onReqInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	TowerImpl t = TowerImpl.getInstance();
        	t.handleInfo(respond);
        	t.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_TOWER);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.TOWER_BAT)
    public CommonMsg onReqEnemy(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	TowerImpl t = TowerImpl.getInstance();
        	t.handleEnemy(respond);
        	t.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_TOWER);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.TOWER_BAT_BOSS)
    public CommonMsg onReqBoss(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	TowerImpl t = TowerImpl.getInstance();
        	t.handleBoss(respond);
        	t.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_TOWER);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.TOWER_UPSTAIRS)
    public CommonMsg onReqBox(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	TowerImpl t = TowerImpl.getInstance();
        	t.handleBox(respond);
        	t.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_TOWER);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.TOWER_RESET)
    public CommonMsg onReqReset(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	TowerImpl t = TowerImpl.getInstance();
        	t.handleReset(respond);
        	t.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_TOWER);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.TOWER_RAID)
    public CommonMsg onReqRaid(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	TowerImpl t = TowerImpl.getInstance();
        	t.handleRaid(respond);
        	t.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_TOWER);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.TOWER_REWARD)
    public CommonMsg onReqFirst(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	TowerImpl t = TowerImpl.getInstance();
        	t.handleReward(respond, receive.body.tower.reward_id);
        	t.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_TOWER);
        }
        return respond;
    }
}
