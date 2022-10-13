package com.ssmGame.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.defdata.msg.pve.PveBattleResultMsg;
import com.ssmGame.defdata.msg.pve.PveBossResultMsg;
import com.ssmGame.module.pve.PveImpl;

/**
 * 关卡Servlet
 * Created by WYM on 2016/11/3.
 */
public class PveServlet extends AdvancedServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Logger log = LoggerFactory.getLogger(PveServlet.class);

    /**
     * 初始化
     */
    public PveServlet() {
        super(PveServlet.class);
    }

    /**
     * 获取PVE基本信息
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.PVE_INFO)
    public CommonMsg onReqInfo(CommonMsg receive){

        // 初始化PveImpl
        PveImpl p = PveImpl.getInstance().init(receive.header.uid);

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);

        try {
            // 读取pve信息
            respond.body.pveInfo = p.getPveInfo();
        }catch(Exception e){
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_PVE);
        }

        // 回收PveImpl
        p.destroy();

        return respond;
    }

    /**
     * 请求进行战斗
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.PVE_REQ_BATTLE)
    public CommonMsg onReqBattle(CommonMsg receive){

        // 初始化PveImpl
        PveImpl p = PveImpl.getInstance().init(receive.header.uid);

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);

        try {
            // 结算上次奖励
            if (receive.body.pveBattleResult.reward_hash != null) {
                respond = p.reqReward(respond, receive.body.pveBattleResult.reward_hash);
            } else {
                respond.body.pveReward = p.getBaseRewardMsg();
            }

            // 执行PVE战斗
            PveBattleResultMsg result = p.reqPveBattle();
            respond.body.pveBattleResult = result;
        }catch(Exception e){
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_PVE);
        }

        // 回收PveImpl
        p.destroy();

        return respond;
    }

    /**
     * 请求挑战BOSS
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.PVE_REQ_BOSS)
    public CommonMsg onReqBoss(CommonMsg receive){

        // 初始化PveImpl
        PveImpl p = PveImpl.getInstance().init(receive.header.uid);

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);

        try {
            // 设定关卡基本信息
            respond.body.pveReward = p.getBaseRewardMsg();

            // 执行BOSS战斗
            PveBossResultMsg result = p.reqBossBattle(respond);
            respond.body.pveBossResult = result;
        }catch(Exception e){
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_PVE);
        }

        // 回收PveImpl
        p.destroy();

        return respond;
    }


    /**
     * 请求快速战斗
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.PVE_REQ_QUICK)
    public CommonMsg onReqQuick(CommonMsg receive){

        // 初始化PveImpl
        PveImpl p = PveImpl.getInstance().init(receive.header.uid);

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);

        try {
            respond = p.reqQuick(respond);
        }catch(Exception e){
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_PVE);
        }

        // 回收PveImpl
        p.destroy();

        return respond;
    }

    /**
     * 结算喜从天降奖励
     * @param receive
     * @return
     */
    @FunUrl(value = I_DefMoudle.PVE_TREASURE)
    public CommonMsg onReqTreasure(CommonMsg receive){

        // 初始化PveImpl
        PveImpl p = PveImpl.getInstance().init(receive.header.uid);

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);

        try {
            // 读取pve信息
            respond = p.reqTreasure(respond);
        }catch(Exception e){
        	e.printStackTrace();
            respond = CommonMsg.err(MsgCode.DESIGN_ERR_PVE);
        }

        // 回收PveImpl
        p.destroy();

        return respond;
    }

}
