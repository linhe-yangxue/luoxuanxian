package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.duel.DuelImpl;

public class DuelServlet extends AdvancedServlet {
	private static final long serialVersionUID = 1L;

	public DuelServlet() {
		super(DuelServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.DUEL_INFO)
    public CommonMsg onReqInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DuelImpl s = DuelImpl.getInstance();
        	respond = s.handleInfo(respond);
        	s.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DUEL);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DUEL_TEAM_CHANGE)
    public CommonMsg onReqTeamChange(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DuelImpl s = DuelImpl.getInstance();
        	respond = s.handleAddRole(respond, receive.body.duel.team_id
        			, receive.body.duel.team_pos, receive.body.duel.role_id);
        	s.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DUEL);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DUEL_TEAM_OPINION)
    public CommonMsg onReqTeamOpinion(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DuelImpl s = DuelImpl.getInstance();
        	respond = s.handleChangePos(respond, receive.body.duel.team_id
        			, receive.body.duel.cur_team);
        	s.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DUEL);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DUEL_TEAM_CHANGE_ALL)
    public CommonMsg onReqOneKey(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DuelImpl s = DuelImpl.getInstance();
        	respond = s.handleOneKeyPos(respond, receive.body.duel.my_teams);
        	s.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DUEL);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DUEL_CHALLENGE)
    public CommonMsg onReqChallenge(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DuelImpl s = DuelImpl.getInstance();
        	respond = s.handleDuel(respond);
        	s.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DUEL);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DUEL_SCORE_REWARD)
    public CommonMsg onReqScoreReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DuelImpl s = DuelImpl.getInstance();
        	respond = s.handleHistoryScoreAward(respond, receive.body.duel.score_id);
        	s.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DUEL);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DUEL_WINS_REWARD)
    public CommonMsg onReqWinsReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DuelImpl s = DuelImpl.getInstance();
        	respond = s.handleWinsCntAward(respond, receive.body.duel.wins_id);
        	s.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DUEL);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.DUEL_TEAM_DROP)
    public CommonMsg onTeamDrop(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	DuelImpl s = DuelImpl.getInstance();
        	respond = s.handleDropTeam(respond, receive);
        	s.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_DUEL);
        }
        return respond;
    }
}
