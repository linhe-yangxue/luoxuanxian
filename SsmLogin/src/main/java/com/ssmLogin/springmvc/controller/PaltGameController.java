package com.ssmLogin.springmvc.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssmCore.constants.HttpWrite;
import com.ssmCore.constants.ReInfo;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.defdata.impl.PlatFormList;
import com.ssmLogin.springmvc.facde.I_PlatGame;
import com.ssmLogin.springmvc.facde.impl.PlatGameService;
import com.ssmShare.platform.PlatformInfo;

@Controller
@Scope("prototype")
@RequestMapping("platGame")
public class PaltGameController {
	
	private static final Logger log = LoggerFactory.getLogger(PaltGameController.class);
	@Autowired I_PlatGame platGame;
	@Autowired HttpWrite print;
	
	@RequestMapping(value = "pgame")
	public void getPlatGame(@RequestParam("pgame") String name,HttpServletResponse response){
		try{
			print.writeMsg(response, platGame.getPlatGame(name));
		}catch(Exception e){
			log.warn("获取平台游戏列表", e);
			print.writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		}finally{
			platGame.Destory();
			platGame = null;
		}
	}
	
	
	@RequestMapping(value = "list")
	public void getLsGame(HttpServletResponse response){
		try{
			print.writeMsg(response, platGame.getPlatGameList());
		}catch(Exception e){
			log.warn("获取平台游戏列表", e);
			print.writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		}finally{
			platGame.Destory();
			platGame = null;
		}
	}
	 
	@RequestMapping(value = "add")
	public void addGame(@ModelAttribute PlatformInfo plat,HttpServletResponse response){
		try{
			print.writeMsg(response, platGame.addPlatGame(plat));
		}catch(Exception e){
			log.warn("添加平台游戏", e);
			print.writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		}finally{
			platGame.Destory();
			platGame = null;
		}
	}
	
	@RequestMapping(value = "edit")
	public void editGame(@ModelAttribute PlatformInfo plat,HttpServletResponse response){
		try{
			print.writeMsg(response, platGame.editPlatGame(plat));
		}catch(Exception e){
			log.warn("编辑平台游戏", e);
			print.writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		}finally{
			platGame.Destory();
			platGame = null;
		}
	}
	
	@RequestMapping(value = "del")
	public void delGame(@RequestParam("pgame") String pgame,HttpServletResponse response){
		try{
			print.writeMsg(response, platGame.delPlatGame(pgame));
		}catch(Exception e){
			log.warn("删除平台游戏", e);
			print.writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		}finally{
			platGame.Destory();
			platGame = null;
		}
	}
	
	@RequestMapping(value = "platName")
	public void getPlatName(@RequestParam("isWx") Integer type,HttpServletResponse response){
		try{
			Object obj = PlatFormList.getInstance().getPlat(type);
			if(obj!=null)
				print.writeMsg(response, new ReInfo(obj));
		}catch(Exception e){
			log.warn("获取平台名称错误！", e);
			print.writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		}
	}
	
	//获取默认资源地址
	@RequestMapping(value = "loginUrl")
	public void getGameloginUrl(@RequestParam("gid") String gid,HttpServletResponse response){
		try{
			Object obj = PlatGameService.getInstance().getGameLogin(gid);
			if(obj!=null)
				print.writeMsg(response, new ReInfo(obj));
		}catch(Exception e){
			log.warn("获取平台名称错误！", e);
			print.writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		}
	}
}
