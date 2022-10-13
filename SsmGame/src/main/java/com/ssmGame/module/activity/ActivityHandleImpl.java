package com.ssmGame.module.activity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.dbase.PlayerAccSpendDB;
import com.ssmData.dbase.PlayerAccSpendInfo;
import com.ssmData.dbase.PlayerActivityDB;
import com.ssmData.dbase.PlayerActivityInfo;
import com.ssmData.dbase.PlayerCheckinActivityDB;
import com.ssmData.dbase.PlayerCheckinActivityInfo;
import com.ssmData.dbase.PlayerDaypayDB;
import com.ssmData.dbase.PlayerDaypayInfo;
import com.ssmData.dbase.PlayerDmdplateDB;
import com.ssmData.dbase.PlayerDmdplateInfo;
import com.ssmData.dbase.PlayerDurationDB;
import com.ssmData.dbase.PlayerDurationInfo;
import com.ssmData.dbase.PlayerEnchantInvestDB;
import com.ssmData.dbase.PlayerEnchantInvestInfo;
import com.ssmData.dbase.PlayerExRoleGiftDB;
import com.ssmData.dbase.PlayerExRoleGiftInfo;
import com.ssmData.dbase.PlayerGiftActivityDB;
import com.ssmData.dbase.PlayerGiftActivityInfo;
import com.ssmData.dbase.PlayerGkgrowActivityDB;
import com.ssmData.dbase.PlayerGkgrowActivityInfo;
import com.ssmData.dbase.PlayerInvestActivityDB;
import com.ssmData.dbase.PlayerInvestActivityInfo;
import com.ssmData.dbase.PlayerJewelryInvestDB;
import com.ssmData.dbase.PlayerJewelryInvestInfo;
import com.ssmData.dbase.PlayerLimitGiftDB;
import com.ssmData.dbase.PlayerLimitGiftInfo;
import com.ssmData.dbase.PlayerLimitSevenActivityDB;
import com.ssmData.dbase.PlayerLimitSevenActivityInfo;
import com.ssmData.dbase.PlayerLvgrowActivityDB;
import com.ssmData.dbase.PlayerLvgrowActivityInfo;
import com.ssmData.dbase.PlayerRoleInvestDB;
import com.ssmData.dbase.PlayerRoleInvestInfo;
import com.ssmData.dbase.PlayerSendDB;
import com.ssmData.dbase.PlayerSendInfo;
import com.ssmData.dbase.PlayerSendRcdDB;
import com.ssmData.dbase.PlayerSendRcdInfo;
import com.ssmData.dbase.PlayerSevenActivityDB;
import com.ssmData.dbase.PlayerSevenActivityInfo;
import com.ssmData.dbase.PlayerTurnplateDB;
import com.ssmData.dbase.PlayerTurnplateInfo;
import com.ssmData.dbase.PlayerVipBenefitDB;
import com.ssmData.dbase.PlayerVipBenefitInfo;
import com.ssmData.dbase.PlayerVipGiftDB;
import com.ssmData.dbase.PlayerVipGiftInfo;

@Service
@Scope("prototype")
public class ActivityHandleImpl {
    
    public static void InitDB(String uid)
    {
		PlayerActivityDB act_db = SpringContextUtil.getBean(PlayerActivityDB.class);
		PlayerActivityInfo act = act_db.loadByUid(uid);
		if (act == null){
			act_db.createDB(uid);
		}
		
		PlayerSevenActivityDB s_db = SpringContextUtil.getBean(PlayerSevenActivityDB.class);
		PlayerSevenActivityInfo s_act = s_db.loadByUid(uid);
		if (s_act == null) {
			s_db.createDB(uid);
		}
		
		PlayerLimitSevenActivityDB l_db = SpringContextUtil.getBean(PlayerLimitSevenActivityDB.class);
		PlayerLimitSevenActivityInfo l_act = l_db.loadByUid(uid);
		if (l_act == null) {
			l_db.createDB(uid);
		}
		
		PlayerInvestActivityDB i_db = SpringContextUtil.getBean(PlayerInvestActivityDB.class);
		PlayerInvestActivityInfo i_act = i_db.loadByUid(uid);
		if (i_act == null) {
			i_db.createDB(uid);
		}
		
		PlayerLvgrowActivityDB lv_db = SpringContextUtil.getBean(PlayerLvgrowActivityDB.class);
		PlayerLvgrowActivityInfo lv_act = lv_db.loadByUid(uid);
		if (lv_act == null) {
			lv_db.createDB(uid);
		}
		
		PlayerGkgrowActivityDB gk_db = SpringContextUtil.getBean(PlayerGkgrowActivityDB.class);
		PlayerGkgrowActivityInfo gk_act = gk_db.loadByUid(uid);
		if (gk_act == null) {
			gk_db.createDB(uid);
		}
		
		PlayerCheckinActivityDB ci_db = SpringContextUtil.getBean(PlayerCheckinActivityDB.class);
		PlayerCheckinActivityInfo ci_act = ci_db.loadByUid(uid);
		if (ci_act == null) {
			ci_db.createDB(uid);
		}
		
		PlayerAccSpendDB as_db = SpringContextUtil.getBean(PlayerAccSpendDB.class);
		PlayerAccSpendInfo as_info = as_db.loadByUid(uid);
		if (as_info == null) {
			as_db.createDB(uid);
		}
		
		PlayerTurnplateDB pt_db = SpringContextUtil.getBean(PlayerTurnplateDB.class);
		PlayerTurnplateInfo pt_info = pt_db.loadByUid(uid);
		if (pt_info == null) {
			pt_db.createDB(uid);
		}
		
		PlayerDaypayDB dp_db = SpringContextUtil.getBean(PlayerDaypayDB.class);
		PlayerDaypayInfo dp_info = dp_db.loadByUid(uid);
		if (dp_info == null) {
			dp_db.createDB(uid);
		}
		
		PlayerGiftActivityDB g_db = SpringContextUtil.getBean(PlayerGiftActivityDB.class);
		PlayerGiftActivityInfo g_info = g_db.loadByUid(uid);
		if (g_info == null) {
			g_db.createDB(uid);
		}
		
		PlayerLimitGiftDB lg_db = SpringContextUtil.getBean(PlayerLimitGiftDB.class);
		PlayerLimitGiftInfo lg_info = lg_db.loadByUid(uid);
		if (lg_info == null) {
			lg_db.createDB(uid);
		}
		
		PlayerDurationDB du_db = SpringContextUtil.getBean(PlayerDurationDB.class);
		PlayerDurationInfo du_info = du_db.loadByUid(uid);
		if (du_info == null) {
			du_db.createDB(uid);
		}
		
		PlayerDmdplateDB dmdp_db = SpringContextUtil.getBean(PlayerDmdplateDB.class);
		PlayerDmdplateInfo dmdp_info = dmdp_db.loadByUid(uid);
		if (dmdp_info == null) {
			dmdp_db.createDB(uid);
		}
		
		PlayerVipBenefitDB vb_db = SpringContextUtil.getBean(PlayerVipBenefitDB.class);
		PlayerVipBenefitInfo vb_info = vb_db.loadByUid(uid);
		if (vb_info == null) {
			vb_db.createDB(uid);
		}
		
		PlayerVipGiftDB vg_db = SpringContextUtil.getBean(PlayerVipGiftDB.class);
		PlayerVipGiftInfo vg_info = vg_db.loadByUid(uid);
		if (vg_info == null) {
			vg_db.createDB(uid);
		}
		
		PlayerRoleInvestDB ri_db = SpringContextUtil.getBean(PlayerRoleInvestDB.class);
		PlayerRoleInvestInfo ri_info = ri_db.loadByUid(uid);
		if (ri_info == null) {
			ri_db.createDB(uid);
		}
		
		PlayerEnchantInvestDB ei_db = SpringContextUtil.getBean(PlayerEnchantInvestDB.class);
		PlayerEnchantInvestInfo ei_info = ei_db.loadByUid(uid);
		if (ei_info == null) {
			ei_db.createDB(uid);
		}
		
		PlayerJewelryInvestDB ji_db = SpringContextUtil.getBean(PlayerJewelryInvestDB.class);
		PlayerJewelryInvestInfo ji_info = ji_db.loadByUid(uid);
		if (ji_info == null) {
			ji_db.createDB(uid);
		}
		
		PlayerExRoleGiftDB er_db = SpringContextUtil.getBean(PlayerExRoleGiftDB.class);
		PlayerExRoleGiftInfo er_info = er_db.loadByUid(uid);
		if (er_info == null) {
			er_db.createDB(uid);
		}
		
		PlayerSendDB se_db = SpringContextUtil.getBean(PlayerSendDB.class);
		PlayerSendInfo se_info = se_db.loadByUid(uid);
		if (se_info == null) {
			se_db.createDB(uid);
		}
		
		PlayerSendRcdDB ser_db = SpringContextUtil.getBean(PlayerSendRcdDB.class);
		PlayerSendRcdInfo ser_info = ser_db.loadByUid(uid);
		if (ser_info == null) {
			ser_db.createDB(uid);
		}
    }
}
