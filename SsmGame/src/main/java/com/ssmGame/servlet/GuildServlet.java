package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.guild.GuildImpl;

public class GuildServlet extends AdvancedServlet {
	private static final long serialVersionUID = 1L;
	
	public GuildServlet() {
		super(GuildServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.GUILD_CREATE)
    public CommonMsg onGuildCreate(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {	
        	GuildImpl g = GuildImpl.getInstance();
        	g.handleCreateGuild(respond, receive.body.guild.name);
        	g.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_INFO)
    public CommonMsg onMyGuildInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleMyGuildInfo(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_INFO_LIST_INT)
    public CommonMsg onGuildInfoListInt(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleApplyList(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_HR_APPLY)
    public CommonMsg onHrApply(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleApply(respond, receive.body.guild.gid);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_HR_REVOKE)
    public CommonMsg onHrRevoke(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleCancelApply(respond, receive.body.guild.gid);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_HR_LIST)
    public CommonMsg onGuildHrList(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleGuildWaitingList(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_HR_HANDLE)
    public CommonMsg onHandleHr(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handlePassApply(respond, receive.body.guild.uid);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_HR_REJECT)
    public CommonMsg onReJectHr(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleDenyApply(respond, receive.body.guild.uid);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_INFO_MEMBERS)
    public CommonMsg onInfoMembers(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleMembersInfo(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_DNT_DMD)
    public CommonMsg onDntDmd(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleDmdDonate(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_DNT_GOLD)
    public CommonMsg onDntGold(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleGoldDonate(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_HR_EXIT)
    public CommonMsg onHrExit(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleQuit(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_HR_RULER_SET)
    public CommonMsg onHrRulerSet(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleAppoint(respond, receive.body.guild.uid);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_HR_RULER_RELIEVE)
    public CommonMsg onHrRulerRelieve(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleDischarge(respond, receive.body.guild.uid);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_HR_DISMISS)
    public CommonMsg onDismiss(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleDismiss(respond, receive.body.guild.uid);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_DISSOLVE)
    public CommonMsg onDissolve(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleDissolve(respond, receive.body.guild.gid);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_HR_LEADER_SET)
    public CommonMsg onHrLeaderSet(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleTransfer(respond, receive.body.guild.uid);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_ANNOUNCE)
    public CommonMsg onAnnounce(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleChgWords(respond, receive.body.guild.words);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_SEARCH)
    public CommonMsg onGuildSearch(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleSearchGuild(respond, receive.body.guild.gid);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_HR_HANDLE_ALL)
    public CommonMsg onOneKeyPass(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleOneKeyPass(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_HR_REJECT_ALL)
    public CommonMsg onOneKeyReject(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleOneKeyDeny(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_WAR_INFO)
    public CommonMsg onWarInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleWarInfo(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_WAR_SCORE)
    public CommonMsg onWarScore(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleWarScore(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_WAR_LIST_ENEMY)
    public CommonMsg onWarListEnemy(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleWarListEnemy(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_WAR_LIST_MY)
    public CommonMsg onWarListMy(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleWarListMy(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_WAR_ATTACK)
    public CommonMsg onWarAttack(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleWarAttack(respond, receive.body.guild_war.uid);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_WAR_PLAYER)
    public CommonMsg onWarPlayer(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleWarPlayer(respond, receive.body.guild_war.uid, receive.body.guild_war.guild_id);;
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_WAR_LOG_PLAY)
    public CommonMsg onWarLogPlay(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleWarLogPlay(respond, receive.body.guild_war.log_id);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_WAR_MYSELF)
    public CommonMsg onWarMyself(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleWarMyself(respond);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_TECH_UPGRADE)
    public CommonMsg onGuildTechUpgrade(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleTechUpgrade(respond, receive);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.GUILD_TECH_LEVELUP)
    public CommonMsg onGuildTechLevelup(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	GuildImpl a = GuildImpl.getInstance();
        	respond = a.handleTechLevelup(respond, receive);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_GUILD);
        }
        return respond;
    }
}
