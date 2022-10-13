package com.ssmLogin.servlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmCore.constants.I_CoreErro;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.jetty.BaseServlet;
import com.ssmCore.jetty.FunUrl;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.constant.I_ModuleServlet;
import com.ssmShare.constants.LoginConstants;
import com.ssmShare.entity.WxPlatform;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

/**
 * 
 *微信服务器校验
 */
public class WxServlet extends BaseServlet{

	private static final long serialVersionUID = 1L;

	public static final Logger log = LoggerFactory.getLogger(WxServlet.class);

	public WxServlet() {
		super(WxServlet.class,new HttpParamString());
	}
	
	@FunUrl(value=I_ModuleServlet.WX_SV_AUTHORITY)
	protected String wxServerCheck(Map<String,Object> param){
		try{
			String pid = (String) param.get("pid");
			String signature = (String) param.get("signature");
			WxPlatform plat = LoginConstants.wxPlat.get(pid);
			Map<String,String> params = new HashMap<String,String>();
			params.put("nonce", (String) param.get("nonce"));
			params.put("token", plat.getToken());
			params.put("timestamp", (String) param.get("timestamp"));
			List<String> ls = new ArrayList<String>(params.keySet());
			Collections.sort(ls);
			String context = "";
			for(String str : ls){
				context += params.get(str);
			}
			context = Encryption.Encode(context, Encryption.SHA1);
			if(context.equals(signature)){
				log.info("校验码：" + context + "上传码："+(String)param.get("echostr"));
				return (String) param.get("echostr");
			}else{
				log.warn(JsonTransfer.getJson(
						new ReInfo(1,"SHA1: "+ context + " signature:" + signature)));
			}
		}catch(Exception e){
			log.warn(JsonTransfer.getJson(new ReInfo(1,e)));
		}
		return "微信授权错误！";	
	}
	
	@FunUrl(value=I_ModuleServlet.WX_INIT_CONF)
	protected Object getWxconf(Map<String,Object> param){
		try{
			String pid = (String) param.get("p_id");
			String url = (String) param.get("url");
			DataConf dSource = DataConf.getInstance();
			dSource.wxload(pid);
			if(dSource.wx!=null){
				I_Platform platform = (I_Platform) SpringContextUtil.getBean(LoginConstants.SDK + "Wx" + LoginConstants.IMPL);
				if(platform!=null)
					return platform.platInit(dSource, url);
			}
		}catch(Exception e){
			log.warn("微信初始化文件获取失败！",e);
		}
		return new ReInfo(I_CoreErro.ERRO_HTTP_PARAM,"微信初始化文件错误");
	} 
}
