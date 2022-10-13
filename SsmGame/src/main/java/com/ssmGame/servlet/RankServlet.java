package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.guild.GuildImpl;
import com.ssmGame.module.rank.RankImpl;

public class RankServlet extends AdvancedServlet  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RankServlet() {
		super(RankServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.RANK_LIST_ARENA)
	public CommonMsg onReqArenaList(CommonMsg receive)
    {
        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
    		respond = RankImpl.getInstance().handelReqArenaList(respond);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_RANK);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.RANK_LIST_PVE)
	public CommonMsg onReqPveList(CommonMsg receive)
	{
		// 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
    		respond = RankImpl.getInstance().handelReqPveList(respond);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_RANK);
        }
        return respond;
	}
	
	@FunUrl(value = I_DefMoudle.RANK_LIST_TOWER)
	public CommonMsg onReqTowerList(CommonMsg receive)
	{
		// 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
    		respond = RankImpl.getInstance().handleTowerList(respond);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_RANK);
        }
        return respond;
	}
	
	@FunUrl(value = I_DefMoudle.RANK_LIST_TEAM_LV)
	public CommonMsg onReqTeamLvList(CommonMsg receive)
	{
		// 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
    		respond = RankImpl.getInstance().handleTeamLvList(respond);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_RANK);
        }
        return respond;
	}
	
	/*@FunUrl(value = I_DefMoudle.RANK_ME)
	public CommonMsg onReqMyInfo(CommonMsg receive)
	{
		CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
    		respond = RankImpl.getInstance().handelReqMy(respond, receive.body.rank.rank_type, receive.header.uid);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_RANK);
        }
        return respond;
	}*/
	
	@FunUrl(value = I_DefMoudle.RANK_INFO)
	public CommonMsg onReqDetail(CommonMsg receive)
	{
		CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
    		respond = RankImpl.getInstance().handelReqDetail(respond, receive.body.rank.uid);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_RANK);
        }
        return respond;
	}
	
	@FunUrl(value = I_DefMoudle.RANK_LIST_GUILD)
	public CommonMsg onReqGuildList(CommonMsg receive)
	{
		// 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
    		respond = RankImpl.getInstance().handleGuildList(respond);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_RANK);
        }
        return respond;
	}
	
	@FunUrl(value = I_DefMoudle.RANK_LIST_GUILD_WAR)
    public CommonMsg onWarRank(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleWarRank(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    } 
}
