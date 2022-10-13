package com.ssmGame.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmCore.jetty.FunUrl;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;

public class ClientSysMsgServlet extends AdvancedServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(ClientSysMsgServlet.class);
	
    public ClientSysMsgServlet() {
        super(ClientSysMsgServlet.class);
    }
    
    @FunUrl(value = I_DefMoudle.SYS_WINDOW_ERROR)
	public CommonMsg onReqWndErr(CommonMsg receive){
    	// 构造返回消息
        CommonMsg respond = new CommonMsg(MsgCode.SUCCESS, receive.header.uid);
        
        if (receive.body.systemMsg != null)
        {
        	log.info("SYS_WINDOW_ERROR: uid:{} msg:{} file:{} line:{} col:{}", receive.header.uid
        			, receive.body.systemMsg.message
        			, receive.body.systemMsg.filename
        			, receive.body.systemMsg.lineno
        			, receive.body.systemMsg.colno);
        }

        return respond;
    }
}
