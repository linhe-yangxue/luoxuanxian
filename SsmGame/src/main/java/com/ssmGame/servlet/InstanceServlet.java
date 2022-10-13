package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.instance.InstanceImpl;

public class InstanceServlet extends AdvancedServlet {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InstanceServlet() {
        super(InstanceServlet.class);
    }
    
    @FunUrl(value = I_DefMoudle.INSTANCE_BATTLE)
    public CommonMsg onReqBattle(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	InstanceImpl inst = InstanceImpl.getInstance().init(receive.header.uid);
        	respond = inst.challenge(respond, receive.body.instance.instance_id);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_INSTANCE);
        }
        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.INSTANCE_RESULT)
    public CommonMsg onReqResult(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	InstanceImpl inst = InstanceImpl.getInstance().init(receive.header.uid);
        	respond = inst.reqReward(respond, receive.body.instance.reward_hash);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_INSTANCE);
        }
        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.INSTANCE_RAID)
    public CommonMsg onReqRaid(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	InstanceImpl inst = InstanceImpl.getInstance().init(receive.header.uid);
        	respond = inst.raid(respond, receive.body.instance.instance_id);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_INSTANCE);
        }
        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.INSTANCE_INFO)
    public CommonMsg onReqInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	InstanceImpl inst = InstanceImpl.getInstance().init(receive.header.uid);
        	respond = inst.info(respond);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_INSTANCE);
        }
        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.INSTANCE_BUY_CHANCE)
    public CommonMsg onReqBuyChallenge(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	InstanceImpl inst = InstanceImpl.getInstance().init(receive.header.uid);
        	respond = inst.buyChallenge(respond, receive.body.instance.instance_id);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_INSTANCE);
        }
        return respond;
    }
}
