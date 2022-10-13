package com.jksdk.test;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmShare.entity.UserBase;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;
import com.ssmShare.platform.UserInfo;

@Service("Sdktest1Impl")
@Scope("prototype")
public class SdkText1Impl implements I_Platform {

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		BaseDaoImpl db = BaseDaoImpl.getInstance();
		UserInfo userInfo = db.find(new Query(Criteria.where("account").is((String) param.get("nickname")).and("pid").is(param.get("pid"))), UserInfo.class);

		UserBase ubase = null;
		if (userInfo != null) {
			dSource.rtn.setInfo(userInfo);
			dSource.rtn.getInfo().setAccount(userInfo.getAccount());// 接收微信帐号
			dSource.rtn.getInfo().setPid((String) param.get("pid"));
			ubase = userInfo.getUserBase();
		} else {
			ubase = new UserBase();
			ubase.setNickname((String) param.get("nickname"));
			dSource.rtn.getInfo().setAccount((String) param.get("nickname"));
			dSource.rtn.getInfo().setPid((String) param.get("pid"));
			dSource.rtn.setUsebase(ubase);
		}
		ubase.pid = (String) param.get("pid");
		return ubase;
	}

	@Override
	public ReInfo shareVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReInfo followVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object platInit(DataConf dSource, String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void callPay(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object statslog(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getUserinfo(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPayInfo(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object other(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
