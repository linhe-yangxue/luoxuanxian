package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.goldbuy.GoldBuyImpl;
import com.ssmGame.module.shop.ShopImpl;

public class ShopServlet extends AdvancedServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShopServlet() {
		super(ShopServlet.class);
		// TODO Auto-generated constructor stub
	}
	
	@FunUrl(value = I_DefMoudle.SHOP_INFO)
    public CommonMsg onReqShopInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	ShopImpl s = ShopImpl.getInstance().init(receive.header.uid);
        	respond = s.handleShopInfo(respond, receive.body.shop.is_id);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SHOP);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SHOP_REFRESH)
    public CommonMsg onReqShopRefresh(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	ShopImpl s = ShopImpl.getInstance().init(receive.header.uid);
        	respond = s.handleReqRefreshGood(respond, receive.body.shop.is_id);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SHOP);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SHOP_BUY)
    public CommonMsg onReqShopBuy(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	ShopImpl s = ShopImpl.getInstance().init(receive.header.uid);
        	respond = s.handleReqBuyItem(respond, receive.body.shop.is_id
        			, receive.body.shop.good_id, receive.body.shop.good_list_id
        			, receive.body.shop.good_price);
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SHOP);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SHOP_GOLD_BUY)
    public CommonMsg onReqGoldBuy(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	GoldBuyImpl g = GoldBuyImpl.getInstance();
        	respond = g.handleBuy(respond);
        	g.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SHOP);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.SHOP_GOLD_BUY_INFO)
	public CommonMsg onReqGoldBuyInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	GoldBuyImpl g = GoldBuyImpl.getInstance();
        	respond = g.handleInfo(respond);
        	g.destroy();
        }
        catch(Exception e)
        {
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_SHOP);
        }
        return respond;
    }
}
