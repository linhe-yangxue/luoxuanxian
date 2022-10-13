package com.ssmGame.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.ssmCore.constants.HttpWrite;
import com.ssmCore.constants.I_CoreErro;
import com.ssmCore.jetty.BaseServlet;
import com.ssmCore.jetty.FunUrl;
import com.ssmCore.memcached.MemAccess;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmShare.order.GiftItem;
import com.ssmShare.order.GmItem;

/**
 * 游戏服Servlet基类
 * Created by WYM on 2016/10/29.
 */
public class AdvancedServlet extends HttpServlet {
	private @Value("${LOGIN_RUL}") String loginUrl; // 登录服务器地址
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 日志记录器
     */
    private static final Logger log = LoggerFactory.getLogger(BaseServlet.class);

    /**
     * 响应函数对应Map
     */
    protected final Map<String, Method> accFun = new ConcurrentHashMap<String, Method>();

    /**
     * 构造函数
     * @param clz 具体Servlet类型
     */
    public AdvancedServlet(Class<?> clz){
        super();
        Method[] methods = clz.getDeclaredMethods();
        for(Method m : methods){
            if (m.isAnnotationPresent(FunUrl.class)) {
                FunUrl f = m.getAnnotation(FunUrl.class);
                accFun.put(f.value(), m);
            }
        }
    }

    /**
     * 响应post请求
     * @param request 请求体
     * @param response 响应体
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURI();
        //System.out.println(url);
        try{
            Method method = accFun.get(url) != null ? accFun.get(url) : accFun.get(url.substring(0, url.length()-1));
            if(method != null){

                // 获取json参数
                String jsonStr = request.getParameter("param");

                // 判断参数存在
                if(jsonStr == null){
                    HttpWrite.getInstance().writeMsg(response, CommonMsg.err(I_CoreErro.ERRO_HTTP_PARAM));
                    return;
                }

                boolean test_pass = false;
                Object resMsg = null;
                CommonMsg msg = null;
                if (url.startsWith(I_DefMoudle.HUB_PREFIX)) {
                	msg = JsonTransfer._In(jsonStr, CommonMsg.class);
                	test_pass = true;
                }
                else if (url.equals(I_DefMoudle.BILL_BUY))
                {
                	String temp = Encryption.decrypt(jsonStr, "e4b0857d93cfb280");
            		// 解析消息
            		GmItem gi = JsonTransfer._In(temp, GmItem.class);
            		if (gi.getUid() == null)
            			return;
            		msg = new CommonMsg(MsgCode.SUCCESS, gi.getUid());
            		msg.body.billing = gi;
            		resMsg = method.invoke(this.getClass().newInstance(), msg);
                    HttpWrite.getInstance().writeMsg(response, resMsg);
                    return;
                }
                else if (url.equals(I_DefMoudle.BILL_GIFT))
                {
                	String temp = Encryption.decrypt(jsonStr, "e4b0857d93cfb280");
                	// 解析消息
            		GiftItem gi = JsonTransfer._In(temp, GiftItem.class);          		
            		msg = new CommonMsg(MsgCode.SUCCESS, "0");
            		msg.body.gift = gi;
            		CommonMsg m = (CommonMsg)method.invoke(this.getClass().newInstance(), msg);
                    HttpWrite.getInstance().writeMsg(response, m.body.return_msg);
                    return;
                }
                else
                {
                	// 解析消息
                    msg = JsonTransfer._In(jsonStr, CommonMsg.class);
                    if (msg != null && msg.header != null && msg.header.mkey != null)
                    {
                    	if (url.equals(I_DefMoudle.PLAYER_PLATFORM))
                    	{
                    		//登陆，直接触发响应函数
                        	resMsg = method.invoke(this.getClass().newInstance(), msg);
                        	if (resMsg != null)
                        	{
                        		CommonMsg t = (CommonMsg)resMsg;
                        		MemAccess.Update(MsgTimer.MTK + t.header.uid, MsgTimer.EXP, msg.header.mkey);
                        		//log.info("MK IN");
                        		// 返回消息
                                HttpWrite.getInstance().writeMsg(response, resMsg);
                                return;
                        	}
                    	}      
                    	else
                    	{
                    		String old_key = (String)MemAccess.GetValue(MsgTimer.MTK + msg.header.uid);
                    		if (old_key != null && old_key.equals(msg.header.mkey))
                    		{
                    			MemAccess.Update(MsgTimer.MTK + msg.header.uid, MsgTimer.EXP, old_key);
                    			test_pass = true;
                    			//log.info("MK CHECK");
                    		}
                    	}
                    }
                }              
                 
                if (test_pass == true)
                {
                	//触发响应函数
                	resMsg = method.invoke(this.getClass().newInstance(), msg);
                }
                else
                {
                	//log.info("MK ERROR");
                	resMsg = new CommonMsg(MsgCode.HTTP_MKEY_ERROR, msg.header.uid);;
                }                

                // 返回消息
                HttpWrite.getInstance().writeMsg(response, resMsg);
                return;
            }
        }catch(Exception e){
        	e.printStackTrace();
            log.warn(e.getMessage());
        }

        // 返回错误消息
        HttpWrite.getInstance().writeMsg(response , CommonMsg.err(I_CoreErro.ERRO_CALLMETHOD_EXITS));
    }

    /**
     * 转发get请求
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        this.doPost(request, response);
    }
    
    public class MsgTimer
    {
    	public static final String MTK = "MTK";
    	public static final int EXP = 5 * 60; //秒
    }
}


