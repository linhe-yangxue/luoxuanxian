package com.ssmLogin.defdata.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmLogin.defdata.entity.Logindata;
import com.ssmLogin.defdata.entity.Userdata;
import com.ssmShare.platform.UserInfo;

@Service //数据统计线程池
@Scope("prototype")
public class StatisticsPool implements Runnable{

	public static final BlockingQueue<UserInfo> dDate = new LinkedBlockingQueue<UserInfo>();
	private ExecutorService userlog;


	public void start(){
		userlog = Executors.newSingleThreadExecutor();
		userlog.execute(this);
	}
	
	@PreDestroy
	public void stop(){
		if(userlog!=null)
			userlog.shutdown();
	}
	
	@Override
	public void run() {
		while(true){ //每次更新用户基本信息
			UserInfo dat = null;
			LogindataImpl db = null;
			UserdataImpl user = null;
			try {
				dat = dDate.take();
				db = LogindataImpl.instance();
				Logindata ldat = new Logindata();
				ldat.setGid(dat.getUaction().getLastGid());
				ldat.setGuid(dat.getUserBase().getGuid());
				ldat.setZid(dat.getUaction().getLastZid(ldat.getGid()));
				ldat.setAccount(dat.getAccount());
				ldat.setLgdate(new Date());
				if(dat.getUserBase().getDevice()!=null && !dat.getPid().equals("egret"))
					ldat.setPid(dat.getPid() + "_" + dat.getUserBase().getDevice());
				else
					ldat.setPid(dat.getPid());
				ldat.setIsold(0);
				ldat.setUid(dat.getUserBase().getUid());
				
				user = UserdataImpl.instance();
				Userdata newdata = user.find(ldat.getGuid(),ldat.getZid());
				if(newdata==null)
					user.addRecord(JsonTransfer._In(JsonTransfer.getJson(ldat), Userdata.class));
				else{
					if(!sameDate(newdata.getLgdate(),ldat.getLgdate()))
						ldat.setIsold(1);
				}
				
				db.addRecord(ldat);
			} catch (Exception e) {
			    e.printStackTrace();
				continue;
			}finally{
				dat = null;
				db = null;
				user = null;
			}
		}	
	}
	
	public static boolean sameDate(Date d1, Date d2) {  
	    if(null == d1 || null == d2)  
	        return false;   
	    Calendar cal1 = Calendar.getInstance();  
	    cal1.setTime(d1);  
	    Calendar cal2 = Calendar.getInstance();  
	    cal2.setTime(d2);  
	    return  cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) 
	    		&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) 
	    		&& cal1.get(Calendar.DAY_OF_WEEK) == cal2.get(Calendar.DAY_OF_WEEK);  
	} 
}

