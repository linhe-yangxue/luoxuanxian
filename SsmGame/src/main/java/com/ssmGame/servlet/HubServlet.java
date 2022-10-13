package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.DefConstant;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.guild.GuildImpl;
import com.ssmGame.module.send.SendImpl;

public class HubServlet extends AdvancedServlet {
	private static final long serialVersionUID = 1L;
	public HubServlet() {
		super(HubServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.HUB_H2G_REQ_MATCH_INFO)
    public CommonMsg onReqMatchInfo(CommonMsg receive){
        try {
        	CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, DefConstant.HUB_ID);
        	GuildImpl g = GuildImpl.getInstance();
        	g.handleHubReqMatchInfo(respond);
        	g.destroy();
        	return respond;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
	@FunUrl(value = I_DefMoudle.HUB_H2G_MATCH_RESULT)
    public CommonMsg onMatchResult(CommonMsg receive){
        try {
        	CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, DefConstant.HUB_ID);
        	GuildImpl g = GuildImpl.getInstance();
        	g.handleMatchResult(respond, receive);
        	g.destroy();
        	return respond;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
	@FunUrl(value = I_DefMoudle.HUB_G2H_WAR_SCORE)
    public CommonMsg onWarScore(CommonMsg receive){
        try {
        	CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, DefConstant.HUB_ID);
        	GuildImpl g = GuildImpl.getInstance();
        	g.handleGsQueryWarScore(respond, receive.header.uid);
        	g.destroy();
        	return respond;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
	@FunUrl(value = I_DefMoudle.HUB_G2H_ENEMY_LIST)
    public CommonMsg onEnemyList(CommonMsg receive){
        try {
        	CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, DefConstant.HUB_ID);
        	GuildImpl g = GuildImpl.getInstance();
        	g.handleGsQueryEnemyList(respond, receive.header.uid);
        	g.destroy();
        	return respond;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
	@FunUrl(value = I_DefMoudle.HUB_H2G_FW_ENEMY_LIST)
    public CommonMsg onFwEnemyList(CommonMsg receive){
        try {
        	CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, DefConstant.HUB_ID);
        	GuildImpl g = GuildImpl.getInstance();
        	g.handleFwEnemyList(respond, receive.header.uid);
        	g.destroy();
        	return respond;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
	@FunUrl(value = I_DefMoudle.HUB_G2H_MY_LIST)
    public CommonMsg onMyList(CommonMsg receive){
        try {
        	CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, DefConstant.HUB_ID);
        	GuildImpl g = GuildImpl.getInstance();
        	g.handleGsQueryMyList(respond, receive.header.uid);
        	g.destroy();
        	return respond;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
	@FunUrl(value = I_DefMoudle.HUB_G2H_WAR_PLAYER)
    public CommonMsg onWarPlayer(CommonMsg receive){
        try {
        	CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, DefConstant.HUB_ID);
        	GuildImpl g = GuildImpl.getInstance();
        	g.handleG2HWarPlayer(respond, receive);
        	g.destroy();
        	return respond;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
	@FunUrl(value = I_DefMoudle.HUB_H2G_FW_WAR_PLAYER)
    public CommonMsg onFwWarPlayer(CommonMsg receive){
        try {
        	CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, DefConstant.HUB_ID);
        	GuildImpl g = GuildImpl.getInstance();
        	g.handleFwWarPlayer(respond, receive);
        	g.destroy();
        	return respond;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
	@FunUrl(value = I_DefMoudle.HUB_G2H_GWAR_ATTACK)
    public CommonMsg onG2HGWarAttack(CommonMsg receive){
        try {
        	CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, DefConstant.HUB_ID);
        	GuildImpl g = GuildImpl.getInstance();
        	g.handleG2HGWarAttack(respond, receive);
        	g.destroy();
        	return respond;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
	@FunUrl(value = I_DefMoudle.HUB_H2G_FW_GW_ATTACK)
    public CommonMsg onH2GFWGWAttack(CommonMsg receive){
        try {
        	CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, DefConstant.HUB_ID);
        	GuildImpl g = GuildImpl.getInstance();
        	g.handleH2GFWGWAttack(respond, receive);
        	g.destroy();
        	return respond;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
	@FunUrl(value = I_DefMoudle.HUB_G2H_LOGPLAY)
    public CommonMsg onG2HLogPlay(CommonMsg receive){
        try {
        	CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, DefConstant.HUB_ID);
        	GuildImpl g = GuildImpl.getInstance();
        	g.handleG2HLogPlay(respond, receive);
        	g.destroy();
        	return respond;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
	@FunUrl(value = I_DefMoudle.HUB_G2H_WARRANK)
    public CommonMsg onG2HWarRank(CommonMsg receive){
        try {
        	CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, DefConstant.HUB_ID);
        	GuildImpl g = GuildImpl.getInstance();
        	g.handleG2HWarRank(respond, receive);
        	g.destroy();
        	return respond;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
	@FunUrl(value = I_DefMoudle.HUB_H2G_GW_DAILY)
    public CommonMsg onH2GGwDaily(CommonMsg receive){
        try {
        	CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, DefConstant.HUB_ID);
        	GuildImpl g = GuildImpl.getInstance();
        	g.handleDaily(respond, receive.body.hub_gw_msg);
        	g.destroy();
        	return respond;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
	@FunUrl(value = I_DefMoudle.HUB_H2G_GW_WEEKLY)
    public CommonMsg onH2GGwWeekly(CommonMsg receive){
        try {
        	CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, DefConstant.HUB_ID);
        	GuildImpl g = GuildImpl.getInstance();
        	g.handleWeekly(respond, receive.body.hub_gw_msg);
        	g.destroy();
        	return respond;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
	
	////斗士外派相关
	@FunUrl(value = I_DefMoudle.HUB_SEND_CHECKIN)
    public CommonMsg onRobTeams(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleHubCheckin(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.HUB_SEND_REFRESH)
    public CommonMsg onRobRefresh(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleHubRefresh(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.HUB_SEND_FIND_ENEMY)
    public CommonMsg onFindEnemy(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleHubFindEnemy(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.HUB_G2G_SEND_QUEST_ROLES)
    public CommonMsg onSendQuestAndRoles(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleHubGGQuestAndRoles(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.HUB_G2G_SEND_BATTLE_RET)
    public CommonMsg onSendBattleRet(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleHubGGBattleRet(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.HUB_G2G_SEND_AVATAR)
    public CommonMsg onSendAvatar(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	SendImpl s = SendImpl.getInstance();
        	respond = s.handleHubGGAvatar(respond, receive);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SEND);
        }
        return respond;
    }
}
