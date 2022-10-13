package com.ssmLogin.springmvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssmCore.constants.HttpWrite;
import com.ssmCore.constants.ReInfo;
import com.ssmLogin.springmvc.facde.I_WxPublic;
import com.ssmShare.entity.role.WxPublic;

@Controller
@Scope("prototype")
@RequestMapping("wxpublic")
public class WxPublicController {
	
	@Autowired I_WxPublic wx;

	@RequestMapping("findall")
	public void getWxPulicAll(HttpServletRequest request,HttpServletResponse response) {
		try{
			String opt = request.getParameter("opt");
			Object obj = null;
			if(opt!=null && opt.equals("1")){
				obj = wx.findAll(Integer.parseInt(opt),null,null);
			}else{
				String star = request.getParameter("start");
				String finsh = request.getParameter("finshed");
				int start = 0;
				int finshed = 12;
				if(star!=null)
					start = Integer.parseInt(star);
				if(finsh!=null)
					finshed = Integer.parseInt(finsh);
				obj = wx.findAll(null,start,finshed); //返回浏览页面
			}
			if(obj!=null)
				HttpWrite.getInstance().writeMsg(response, new ReInfo(obj)); //返回json
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@RequestMapping("find")
	public void getWxPulic(@RequestParam("pid") String pid, HttpServletResponse response) {
		WxPublic wxp = wx.find(pid); 
		HttpWrite.getInstance().writeMsg(response, new ReInfo(wxp));
	}
	
	@RequestMapping("add")
	public void addWxPulic(@ModelAttribute WxPublic wxp, HttpServletResponse response) {
		wx.add(wxp);
		HttpWrite.getInstance().writeMsg(response, new ReInfo("微信公众号信息添加成功"));
	}
	
	@RequestMapping("edit")
	public void editWxPulic(@ModelAttribute WxPublic wxp, HttpServletResponse response) {
		wx.edit(wxp);
		HttpWrite.getInstance().writeMsg(response, new ReInfo("微信公众号信息添加成功"));
	}
	
	@RequestMapping("delete")
	public void delWxPulic(@RequestParam("pid") String pid, HttpServletResponse response) {
		wx.delete(pid);
		HttpWrite.getInstance().writeMsg(response, new ReInfo("微信公众号信息添加成功"));
	}
}
