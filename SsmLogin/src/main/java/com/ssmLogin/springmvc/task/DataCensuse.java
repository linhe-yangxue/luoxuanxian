package com.ssmLogin.springmvc.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PreDestroy;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.colligate.DateUtil;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmCore.trigger.TaskTrigger;
import com.ssmLogin.defdata.entity.Censuse;
import com.ssmLogin.defdata.impl.CensusPlatImpl;
import com.ssmLogin.defdata.impl.CensuseImpl;

@Service
public class DataCensuse implements Job {
	private Logger log = LoggerFactory.getLogger(DataCensuse.class);

	private @Autowired TaskTrigger task; // 任务管理器
	private @Value("${TASK_ID}") Integer taskid;
	private @Value("${TASK_TIME}") String tasktime;
	public static boolean start = false;

	public DataCensuse _instance() {
		return SpringContextUtil.getBean(DataCensuse.class);
	}

	public void init() throws SchedulerException {
		task.init();
		task.CreateJob(DataCensuse.class, taskid, tasktime);
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		if (Constants.start) {
			return;
		}
		List<String> strDate = getDate(-8);
		try {
			Constants.start = true;
			for (String gid : Constants.gids) {
				Set<String> plats = Constants.gToPid.get(gid);
//				System.out.println("plat");
//				System.out.println(new Gson().toJson(plats));
				if (gid.equals("yqdqwb")) {
/*
					for (int i = 0; i < 2; i++) {
						Set<Integer> zids = Constants.gTozid.get(gid + i);
						if (plats == null || zids == null)
							continue;
						for (String pid : plats) {// 平台
							for (Integer zid : zids) {// 区
								for (String dat : strDate) {
									Censuse censuses = CensusPlatImpl.getInstance().dayCensus(gid, pid+"_"+i, zid,
											dat, dat);
									Censuse censuse = new Censuse();
									censuse.setGid(gid);
									censuse.setPid(pid+"_"+i);
									censuse.setZid(zid);
									censuse.setLgdate(DateUtil.getStringDate(dat, "yyyy-MM-dd"));
									censuse.setNewUser((Integer) censuses.get("newUser"));
									censuse.setActUser((Integer) censuses.get("actUser"));
									censuse.setOldUser((Integer) censuses.get("oldUser"));

									censuse.setTime_left(censuses.get("time_left") != null
											? Float.parseFloat(String.valueOf(censuses.get("time_left"))) : null);
									censuse.setDay3_left(censuses.get("day3_left") != null
											? Float.parseFloat(String.valueOf(censuses.get("day3_left"))) : null);
									censuse.setDay7_left(censuses.get("day7_left") != null
											? Float.parseFloat(String.valueOf(censuses.get("day7_left"))) : null);
									censuse.setNewpay(censuses.get("newpay") != null
											? Float.parseFloat(String.valueOf(censuses.get("newpay"))) : null);
									censuse.setMoney(censuses.get("money") != null
											? Float.parseFloat(String.valueOf(censuses.get("money"))) : null);
									censuse.setPayRate(censuses.get("payRate") != null
											? Float.parseFloat(String.valueOf(censuses.get("payRate"))) : null);
									censuse.setNew7pay(censuses.get("new7pay") != null
											? Float.parseFloat(String.valueOf(censuses.get("new7pay"))) : null);
									censuse.setArpu(censuses.get("arpu") != null
											? Float.parseFloat(String.valueOf(censuses.get("arpu"))) : null);
									censuse.setArppu(censuses.get("arppu") != null
											? Float.parseFloat(String.valueOf(censuses.get("arppu"))) : null);
									CensuseImpl.instance().addRecord(censuse);
								}
							}
						}
					}*/
				} else {
					Set<Integer> zids = Constants.gTozid.get(gid);
					if (plats == null || zids == null)
						continue;
					for (String pid : plats) {// 平台
						for (Integer zid : zids) {// 区
							for (String dat : strDate) {
								Censuse censuse = null;
								try {
									censuse =
											CensusPlatImpl.getInstance().dayCensus(gid, pid, zid,
													dat, dat);
								} catch (Exception e) {
									log.info(ExceptionUtils.getStackTrace(e));
								}
								if (censuse == null){
									continue;
								}
//								System.out.println(gid + pid + zid);
//								System.out.println(new Gson().toJson(censuse));
								censuse.setGid(gid);
								censuse.setPid(pid);
								censuse.setZid(zid);
								censuse.setLgdate(DateUtil.getStringDate(dat, "yyyy-MM-dd"));
							/*	censuse.setNewUser((Integer) censuses.getNewUser()get("newUser"));
								censuse.setActUser((Integer) censuses.get("actUser"));
								censuse.setOldUser((Integer) censuses.get("oldUser"));

								censuse.setTime_left(censuses.get("time_left") != null
										? Float.parseFloat(String.valueOf(censuses.get("time_left"))) : null);
								censuse.setDay3_left(censuses.get("day3_left") != null
										? Float.parseFloat(String.valueOf(censuses.get("day3_left"))) : null);
								censuse.setDay7_left(censuses.get("day7_left") != null
										? Float.parseFloat(String.valueOf(censuses.get("day7_left"))) : null);
								censuse.setNewpay(censuses.get("newpay") != null
										? Float.parseFloat(String.valueOf(censuses.get("newpay"))) : null);
								censuse.setMoney(censuses.get("money") != null
										? Float.parseFloat(String.valueOf(censuses.get("money"))) : null);
								censuse.setPayRate(censuses.get("payRate") != null
										? Float.parseFloat(String.valueOf(censuses.get("payRate"))) : null);
								censuse.setNew7pay(censuses.get("new7pay") != null
										? Float.parseFloat(String.valueOf(censuses.get("new7pay"))) : null);
								censuse.setArpu(censuses.get("arpu") != null
										? Float.parseFloat(String.valueOf(censuses.get("arpu"))) : null);
								censuse.setArppu(censuses.get("arppu") != null
										? Float.parseFloat(String.valueOf(censuses.get("arppu"))) : null);*/
								CensuseImpl.instance().addRecord(censuse);
							}
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Constants.start = false;
	}

	protected static List<String> getDate(int num) {
		List<String> ls = new ArrayList<String>();
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(new Date());
		do {
			ls.add(DateUtil.getDateTime("yyyy-MM-dd", gc.getTimeInMillis()));
			gc.add(5, -1);
			num++;
		} while (num < 0);
		return ls;
	}

	@PreDestroy
	public void destory() throws SchedulerException {
		task.destory();
	}
}
