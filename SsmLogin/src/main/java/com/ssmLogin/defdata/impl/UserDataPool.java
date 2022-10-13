package com.ssmLogin.defdata.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmShare.entity.InviteEny;
import com.ssmShare.platform.DataConf;
import com.ssmShare.platform.UserInfo;

@Service
@Scope("prototype")
public class UserDataPool implements Runnable {

	public static final BlockingQueue<DataConf> uDate = new LinkedBlockingQueue<DataConf>();
	private ExecutorService userstore1;

	public void start() {
		userstore1 = Executors.newSingleThreadExecutor();
		userstore1.execute(this);
	}

	@PreDestroy
	public void stop() {
		if (userstore1 != null)
			userstore1.shutdown();
	}

	@Override
	public void run() {
		while (true) { // 每次更新用户基本信息
			DataConf dat = null;
			try {
				System.out.println(uDate);
				dat = uDate.take();
				BaseDaoImpl db = BaseDaoImpl.getInstance();
				Criteria criteria = null;
				Update update = new Update();

				if (dat.doc.getIsWx() == 1) {
					criteria = Criteria.where("account").is(dat.rtn.getInfo().getAccount());
					UserInfo info = (UserInfo) db.find(new Query(criteria), UserInfo.class);
					if (info != null) {
						update.set("subPid", dat.rtn.getInfo().getSubPid());
						update.set("userBase", dat.rtn.getInfo().getUserBase());
						db.saveOrUpdate(new Query(criteria), update, UserInfo.class);
					} else {
						db.add(dat.rtn.getInfo());
						if (dat.rtn.getInfo().getUserBase().getInvitID()!=null){
							UserInfo yq = db.find(new Query(Criteria.where("_id") //根据id查出用户
									.is(dat.rtn.getInfo().getUserBase().getInvitID())), UserInfo.class);
							
							if (dat.rtn.getInfo().getUserBase().getInvitID()!=null)
								Invitation(yq.getAccount() //邀请人帐号
										,dat.rtn.getInfo().getAccount() //被邀请人帐号
										,dat.rtn.getInfo().getPid());
						}
					}
				} else {
					criteria = Criteria.where("account").is(dat.rtn.getInfo().getAccount()).and("pid")
							.is(dat.rtn.getInfo().getPid());
					UserInfo info = (UserInfo) db.find(new Query(criteria), UserInfo.class);
					if (info != null) { 
						update.set("userBase", dat.rtn.getInfo().getUserBase());
						db.saveOrUpdate(new Query(criteria), update, UserInfo.class);
					} else {
						db.add(dat.rtn.getInfo());
						if (dat.rtn.getInfo().getUserBase().getInvitID()!=null){
							Invitation(dat.rtn.getInfo().getUserBase().getInvitID() //邀请人帐号
									,dat.rtn.getInfo().getAccount() //被邀请人帐号
									,dat.rtn.getInfo().getPid());
						}
					}
				}
				db = null;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dat.Destory();
				dat = null;
			}
		}
	}

	/**
	 * 用户邀请记录存储
	 * 
	 * @param dSoucse
	 */
	private void Invitation(String id, String account ,String pid) {
		if (id != null && account!=null && pid!=null) {
			InviteEny inv = new InviteEny(id,account,pid);
			BaseDaoImpl.getInstance().add(inv);
		}
	}
}
