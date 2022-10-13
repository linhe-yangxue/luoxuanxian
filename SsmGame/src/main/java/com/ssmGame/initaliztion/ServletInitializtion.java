package com.ssmGame.initaliztion;

import com.ssmGame.servlet.*;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ssmCore.jetty.I_AddServlet;

@Service
public class ServletInitializtion implements I_AddServlet {

	private @Value("${WSOCKET_EXPIRE}") Integer expire;
	private @Value("${NET_BASE}") String net_base_path;

	@Override
	public void addServletHandle(Server server) {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		// 注册Servlet
		context.addServlet(ClientSysMsgServlet.class, "/sys/*"); // 客户端系统报错
		context.addServlet(BattleServlet.class, "/battle/*"); // 战斗
		context.addServlet(PlayerServlet.class, "/player/*"); // 用户相关
		context.addServlet(BagServlet.class, "/bag/*"); // 背包
		context.addServlet(PveServlet.class, "/pve/*"); // 关卡
		context.addServlet(RoleServlet.class, "/role/*"); // 斗士
		context.addServlet(InstanceServlet.class, "/instance/*");// 副本
		context.addServlet(DrawCardServlet.class, "/draw/*");// 抽卡
		context.addServlet(ScrollServlet.class, "/scroll/*");// 门票
		context.addServlet(EquipServlet.class, "/equip/*");// 装备
		context.addServlet(ArenaServlet.class, "/arena/*");// 竞技场
		context.addServlet(RankServlet.class, "/rank/*");// 排行榜
		context.addServlet(ShopServlet.class, "/shop/*");// 商店
		context.addServlet(MailServlet.class, "/mail/*");// 邮件
		context.addServlet(DuelServlet.class, "/duel/*");// 一骑当千
		context.addServlet(DailytaskServlet.class, "/daily/task/*");// 历练
		context.addServlet(TaskServlet.class, "/task/*");// 主线任务
		context.addServlet(BillingServlet.class, "/bill/*"); //充值
		context.addServlet(PrivServlet.class, "/priv/*"); //月卡特权
		context.addServlet(TowerServlet.class, "/tower/*"); //爬塔
		context.addServlet(AccRmbServlet.class, "/act/acc_rmb/*"); //活动累计充值
		context.addServlet(SevenActivityServlet.class, "/act/seven/*"); //活动7日
		context.addServlet(LimitSevenServlet.class, "/act/limit_seven/*"); //活动限时七日
		context.addServlet(TeamLvGrowActivityServlet.class, "/act/lv_grow/*"); //等级成长活动
		context.addServlet(GkGrowActivityServlet.class, "/act/gk_grow/*"); //等级成长活动
		context.addServlet(InvestActivityServlet.class, "/act/invest/*"); //投资活动
		context.addServlet(CheckinActivityServlet.class, "/act/check_in/*"); //签到活动
		context.addServlet(LimitGiftServlet.class, "/act/limit_gift/*"); //限时礼包活动
		context.addServlet(GiftActivityServlet.class, "/act/gift/*"); //礼包活动
		context.addServlet(AccSpendServlet.class, "/act/acc_spend/*"); //累计消耗活动
		context.addServlet(DaypayServlet.class, "/act/day_pay/*"); //累计充值活动
		context.addServlet(TurnplateServlet.class, "/act/turn_plate/*"); //转盘活动
		context.addServlet(DurationServlet.class, "/act/duration/*"); //在线活动
		context.addServlet(DmgRewardServlet.class, "/dmg/*"); //伤害值统计
		context.addServlet(WishServlet.class, "/wish/*"); //许愿
		context.addServlet(DramaServlet.class, "/drama/*"); //剧情
		context.addServlet(DmdplateServlet.class, "/act/dmd_plate/*"); //钻石转盘
		context.addServlet(HubServlet.class, "/hub/*");
		context.addServlet(GuildServlet.class, "/guild/*");
		context.addServlet(JewelryServlet.class, "/jewelry/*");
		context.addServlet(SnsServlet.class, "/sns/*");
		context.addServlet(BossServlet.class, "/boss/*");
		context.addServlet(VipBenefitServlet.class, "/act/vip_benefit/*"); //vip福利活动
		context.addServlet(VipGiftServlet.class, "/act/vip_weekly/*"); //vip周礼包活动
		context.addServlet(ExroleGiftServlet.class, "/act/ex_role/*"); //exrole包活动
		context.addServlet(GameServlet.class, "/client/*"); // 客户端轮训
		context.addServlet(SendServlet.class, "/send/*"); //斗士外派

		FilterHolder filterHolder = new FilterHolder(CrossOriginFilter.class);
		filterHolder.setInitParameter("allowCredentials", "true");
		filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
		filterHolder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		context.addFilter(filterHolder, "/", null);

		WebAppContext webcontext = new WebAppContext(net_base_path, "/" + net_base_path);
		webcontext.addFilter(filterHolder, "/*", null);
		// 设置webapp的位置
		webcontext.setResourceBase(net_base_path);
		webcontext.setClassLoader(Thread.currentThread().getContextClassLoader());

		// 多haddler设置
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { webcontext, context });
		// 载入hander
		server.setStopAtShutdown(true);
		server.setHandler(handlers);
	}

}
