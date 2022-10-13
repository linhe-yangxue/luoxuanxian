package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.module.gm.GmImpl;
import com.ssmGame.module.monthcard.MonthcardImpl;

public class BillingServlet extends AdvancedServlet {
	//private static final Logger log = LoggerFactory.getLogger(BattleServlet.class);
	private static final long serialVersionUID = 1L;
	
	public BillingServlet() {
		super(BillingServlet.class);
	}
	
	@FunUrl(value = I_DefMoudle.BILL_BUY)
    public CommonMsg onReqInfo(CommonMsg receive){

        // 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        try
        {
        	MonthcardImpl i = MonthcardImpl.getInstance();
        	i.handleBuy(respond, receive.body.billing);
        	i.destroy();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	respond = CommonMsg.err(MsgCode.DESIGN_ERR_BILLING);
        }
        return respond;
    }
	
	@FunUrl(value = I_DefMoudle.BILL_GIFT)
	public CommonMsg onBillGift(CommonMsg receive){
	
    	// 构造返回消息
    	CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
    	try
    	{
    		GmImpl i = GmImpl.getInstance();
    		respond.body.return_msg = i.handleGift(respond, receive.body.gift);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		respond = CommonMsg.err(MsgCode.DESIGN_ERR_GIFT);
    	}
    	return respond;
	}
}
