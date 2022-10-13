package com.ssmLogin.defdata;

import java.util.Iterator;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.defdata.entity.Activecode;
import com.ssmLogin.defdata.impl.ActivecodeImpl;

@Service
public class AcodeStroe implements Runnable {
	
	private Set<String> number;
	private String gid;
	private String pid;
	private Integer tempid;
	
	public static AcodeStroe getInstance(){
		return SpringContextUtil.getBean(AcodeStroe.class);
	}
	
	public void start(Set<String> number,String gid,String pid,String iden){
		this.number = number;
		this.gid = gid;
		this.pid = pid;
		this.tempid = Integer.parseInt(iden);
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		ActivecodeImpl db = ActivecodeImpl.instance();
		try{
			Iterator<String> it = number.iterator();
			while(it.hasNext()){
				Activecode activecode = new Activecode();
				activecode.setActiveCode(it.next());
				activecode.setGid(gid);
				activecode.setPid(pid);
				activecode.setStatus(tempid);
				db.addRecord(activecode);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			destory();
		}
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	public Set<String> getNumber() {
		return number;
	}
	public void setNumber(Set<String> number) {
		this.number = number;
	}

	public void destory(){
		number = null;
		gid = null;
		pid = null;
	}

	public Integer getTempid() {
		return tempid;
	}

	public void setTempid(Integer tempid) {
		this.tempid = tempid;
	}
}
