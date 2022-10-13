package com.ssmGame.servlet;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.arena.ArenaImpl;

public class ArenaServlet extends AdvancedServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArenaServlet() {
		super(ArenaServlet.class);
	}
		// TODO Auto-generated constructor stub
		
	@FunUrl(value = I_DefMoudle.ARENA_ENEMY)
	public CommonMsg onReqChallengeList(CommonMsg receive)
    {
		CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	
    		ArenaImpl a_i = ArenaImpl.getInstance().init(receive.header.uid);
    		respond = a_i.handelReqChallengeList(respond);
    		a_i.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ARENA);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ARENA_RANK)
	public CommonMsg onReqMyRank(CommonMsg receive)
	{
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
    		ArenaImpl a_i = ArenaImpl.getInstance().init(receive.header.uid);
    		respond = a_i.handleGetMyRank(respond);
    		a_i.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ARENA);
        }
        return respond;
	}
	
	@FunUrl(value = I_DefMoudle.ARENA_INIT)
	public CommonMsg onReqInit(CommonMsg receive)
    {

		Lock lock = new ReentrantLock();  
		lock.lock();
		CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	
    		ArenaImpl a_i = ArenaImpl.getInstance().init(receive.header.uid);
    		respond = a_i.handleNewPlayer(respond);
    		a_i.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ARENA);
        }
        finally
        {
        	lock.unlock();	
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ARENA_CHALLENGE)
	public CommonMsg onReqChallenge(CommonMsg receive)
    {

		Lock lock = new ReentrantLock();  
		lock.lock();
		CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	
    		ArenaImpl a_i = ArenaImpl.getInstance().init(receive.header.uid);
    		respond = a_i.handleReqChallenge(respond, receive.body.arena.enemy_rank_id);
    		a_i.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ARENA);
        }
        finally
        {
        	lock.unlock();	
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ARENA_RAID)
	public CommonMsg onReqRaid(CommonMsg receive)
	{
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
    		ArenaImpl a_i = ArenaImpl.getInstance().init(receive.header.uid);
    		respond = a_i.handleReqRaid(respond);
    		a_i.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ARENA);
        }
        return respond;
	}
	
	@FunUrl(value = I_DefMoudle.ARENA_RANK_REWARD)
	public CommonMsg onReqRankReward(CommonMsg receive)
	{
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
    		ArenaImpl a_i = ArenaImpl.getInstance().init(receive.header.uid);
    		respond = a_i.handleReqRankReward(respond, receive.body.arena.reward_id);
    		a_i.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ARENA);
        }
        return respond;
	}
	
	@FunUrl(value = I_DefMoudle.ARENA_REWARD_INFO)
	public CommonMsg onReqRankRewardInfo(CommonMsg receive)
	{
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
    		ArenaImpl a_i = ArenaImpl.getInstance().init(receive.header.uid);
    		respond = a_i.handleReqRankRewardInfo(respond);
    		a_i.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ARENA);
        }
        return respond;
	}
}
