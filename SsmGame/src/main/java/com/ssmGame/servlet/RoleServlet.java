package com.ssmGame.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.lineup.LineupImpl;
import com.ssmGame.module.role.RoleImpl;

/**
 * 斗士Servlet
 * Created by WYM on 2016/11/3.
 */
public class RoleServlet extends AdvancedServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Logger log = LoggerFactory.getLogger(RoleServlet.class);

    /**
     * 初始化
     */
    public RoleServlet() {
        super(RoleServlet.class);
    }

    /**
     * 更换主将
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.ROLE_HERO_CHANGE)
    public CommonMsg onReqInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);

        // 解析请求
        // TODO 客户端上传的消息在 CommonMsg.body.role_change_hero 内会附带当前要替换的pve_team位置和要上阵的role_id

        // 返回结果
        // TODO CommonMsg.body.sync_roles 内填写本次改变所影响的角色、阵容的同步信息，
        // TODO 同步信息中角色按角色为单位，阵容按队伍为单位，副将以位置为单位下发

        try {
            RoleImpl role = RoleImpl.getInstance();
            role.HandleHeroChange(respond, receive.body.role_change_hero.role_id, receive.body.role_change_hero.seat_id);
        }catch(Exception e){
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_ROLE);
        }

        // TODO 如果发生验证错误，在 CommonMsg.header.rt_msg 中返回响应的错误码，错误码需要策划配表

        return respond;
    }

    /**
     * 角色升级1次
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.ROLE_LEVELUP)
    public CommonMsg onReqLevelup(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);

        try {
            if (receive.body.role_result != null) {
                RoleImpl role = RoleImpl.getInstance();
                respond = role.HandleLevelUp(respond, receive.body.role_result.role_id, 1, true);
            }
        }catch(Exception e){
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_ROLE);
        }
        
        return respond;
    }


    /**
     * 角色升级5次
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.ROLE_LEVELUP_5)
    public CommonMsg onReqLevelup5(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);

        try {
            if (receive.body.role_result != null) {
                RoleImpl role = RoleImpl.getInstance();
                respond = role.HandleLevelUp(respond, receive.body.role_result.role_id, 5, true);
            }
        }catch(Exception e){
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_ROLE);
        }
        
        return respond;
    }


    /**
     * 角色突破
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.ROLE_BREACH)
    public CommonMsg onReqBreach(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);

        try{
            if (receive.body.role_result != null)
            {
                RoleImpl role = RoleImpl.getInstance();
                respond = role.HandleBreach(respond, receive.body.role_result.role_id, true);
            }
        }catch(Exception e){
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_ROLE);
        }

        return respond;
    }


    /**
     * 角色合成
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.ROLE_COMPOSE)
    public CommonMsg onReqCompose(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);

        try {
            if (receive.body.role_result != null) {
                RoleImpl role = RoleImpl.getInstance();
                respond = role.HandleAddNewRole(respond, receive.body.role_result.role_id, true);
            }
        }catch(Exception e){
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_ROLE);
        }
        
        return respond;
    }
    
    /**
     * 角色进阶
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.ROLE_TALENT)
    public CommonMsg onReqTalent(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);

        try{
            if (receive.body.role_result != null)
            {
                RoleImpl role = RoleImpl.getInstance();
                respond = role.HandleTalent(respond, receive.body.role_result.role_id, true);
            }
        }catch(Exception e){
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_ROLE);
        }
        
        return respond;
    }

    /**
     * 角色觉醒
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.ROLE_AWAKEN)
    public CommonMsg onReqAwaken(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);

        try {
            if (receive.body.role_result != null) {
                RoleImpl role = RoleImpl.getInstance();
                respond = role.HandleAwaken(respond, receive.body.role_result.role_id, true);
            }
        }catch(Exception e){
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_ROLE);
        }
        
        return respond;
    }
    
    /**
     * 副将上阵
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.ROLE_BACKUP)
    public CommonMsg onReqChangeBackup(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);

        try {
            if (receive.body.role_result != null) {
                RoleImpl role = RoleImpl.getInstance();
                respond = role.HandleChangeBackupPos(respond, receive.body.role_result.role_id
                        , receive.body.role_result.team_pos, receive.body.role_result.backup_pos);
            }
        }catch(Exception e){
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_ROLE);
        }
        
        return respond;
    }
    
    /**
     * 换布阵
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.ROLE_OPINION)
    public CommonMsg onReqChangePos(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);

        try {
            if (receive.body.role_result != null) {
                RoleImpl role = RoleImpl.getInstance();
                respond = role.HandleRefreshHeroPos(respond, receive.body.role_result.opinion);
            }
        }catch(Exception e){
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_ROLE);
        }
        
        return respond;
    }
    
    @FunUrl(value = I_DefMoudle.ROLE_LINEUP_INFO)
    public CommonMsg onReqLineupInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {	
        	LineupImpl a = LineupImpl.getInstance();
        	respond = a.handleInfo(respond);
        	a.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_LINEUP);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ROLE_LINEUP_REWARD)
    public CommonMsg onReqReward(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
        	LineupImpl a = LineupImpl.getInstance();
        	respond = a.handleReward(respond, receive.body.role_result.reward_id);
        	a.destroy();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_LINEUP);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.ROLE_AWAKEN_RESET)
    public CommonMsg onAwakenReset(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try {
            RoleImpl role = RoleImpl.getInstance();
            respond = role.handleAwakenReset(respond, receive);
            role.Destory();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_ROLE);
        }
        return respond;
    }
}
