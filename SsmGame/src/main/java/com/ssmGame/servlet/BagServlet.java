package com.ssmGame.servlet;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;

/**
 * 背包Servlet
 * Created by WYM on 2016/11/1.
 */
public class BagServlet extends AdvancedServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 构造函数
     */
    public BagServlet() {
        super(BagServlet.class);
    }

    /**
     * 背包测试
     * @param msg
     * @return
     */
    @FunUrl(value = I_DefMoudle.BAG_TEST)
    public CommonMsg onBagTest(CommonMsg msg){

        CommonMsg resMsg = new CommonMsg(MsgCode.SUCCESS, "");
        //resMsg.body.bagInfo = new BagInfoMsg();
        //resMsg.body.bagInfo.items.put(100, 20);
        //resMsg.body.bagInfo.items.put(150, 0);
        //resMsg.body.bagInfo.items.put(30, 6);

        return resMsg;
    }
}
