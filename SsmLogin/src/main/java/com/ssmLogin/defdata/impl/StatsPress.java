package com.ssmLogin.defdata.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.mongo.PageList;
import com.ssmCore.tool.colligate.DateUtil;
import com.ssmShare.entity.statistics.LogCreateStats;
import com.ssmShare.entity.statistics.LogLoginStats;
import com.ssmShare.facde.I_StatsPress;

@Service
public class StatsPress implements I_StatsPress {
	private BaseDaoImpl db = BaseDaoImpl.getInstance();

	@Override
	public PageList getLoginLog(String gid, String pid, int zid, String startdate, String enddate, int pagesize, int page) throws Exception {
		Query query = new Query();
		query.addCriteria(Criteria.where("gid").is(gid));
		query.addCriteria(Criteria.where("pid").is(pid));
		if (zid != -1) {
			query.addCriteria(Criteria.where("zid").is(zid));
		}
		Date start = DateUtil.getStringDate(startdate, DateUtil.DEFAULT_DATA_PATTERN);
		Date end = DateUtil.getStringDate(enddate, DateUtil.DEFAULT_DATA_PATTERN);
		if (start.getTime() == end.getTime()) {
			Calendar ca = Calendar.getInstance();
			ca.add(Calendar.DATE, 1);
			end = ca.getTime();
		}
		query.addCriteria(Criteria.where("createTime").gte(start).lt(end));
		return db.PaginationgetPage(page, pagesize, query, LogLoginStats.class);
	}

	@Override
	public PageList getCreateRoleLog(String gid, String pid, int zid, String startdate, String enddate, int pagesize, int page) throws Exception {
		Query query = new Query();
		query.addCriteria(Criteria.where("gid").is(gid));
		query.addCriteria(Criteria.where("pid").is(pid));
		if (zid != -1) {
			query.addCriteria(Criteria.where("zid").is(zid));
		}
		Date start = DateUtil.getStringDate(startdate, DateUtil.DEFAULT_DATA_PATTERN);
		Date end = DateUtil.getStringDate(enddate, DateUtil.DEFAULT_DATA_PATTERN);
		if (start.getTime() == end.getTime()) {
			Calendar ca = Calendar.getInstance();
			ca.add(Calendar.DATE, 1);
			end = ca.getTime();
		}
		query.addCriteria(Criteria.where("createTime").gte(start).lt(end));
		return db.PaginationgetPage(page, pagesize, query, LogCreateStats.class);
	}

	@Override
	public Object getUserInfo(String gid, String pid, String uuid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getUserInfo(String gid, String pid, String zid, String account) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
