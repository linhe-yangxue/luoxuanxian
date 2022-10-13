package com.ssmCore.trigger;

import java.text.ParseException;
import javax.annotation.PreDestroy;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;

@Service
public class TaskTrigger {

	public static String JOB="job_";
	public static String TRIGGER="trigger_";
	public static String TASK="task_";
	public static SchedulerFactory schedulerFactory; 
	
	public static TaskTrigger instance(){
		return SpringContextUtil.getBean(TaskTrigger.class);
	} 
	
	/**
	 * 定时任务管理器初始化
	 * @throws SchedulerException
	 */
	public void init() throws SchedulerException{
		if(schedulerFactory==null)
			schedulerFactory = new StdSchedulerFactory();
		
		Scheduler scheduler = schedulerFactory.getScheduler();
		if(!scheduler.isStarted())
			scheduler.start();
	}
	
   /**
    * @param clazz   定时任务类
    * @param taskid  任务id
    * @param rule	   时间规则
    * @param group   执行组
    * @return
 * @throws SchedulerException 
    */
	public void CreateJob(Class<? extends Job> clazz,Integer taskid,String rule) throws SchedulerException{
		//创建任务	
		JobDetail job = JobBuilder.newJob(clazz)
					   .usingJobData(clazz.getSimpleName(), taskid)
				       .withIdentity(TASK.concat(taskid.toString()), JOB.concat(taskid.toString()))
				       .build();
		//创建定时器
		Trigger	trigger = TriggerBuilder.newTrigger()
				      .withIdentity(TASK.concat(taskid.toString()), TRIGGER.concat(taskid.toString()))
				      .withSchedule(CronScheduleBuilder.cronSchedule(rule))
				      .build();
		
		schedulerFactory.getScheduler().scheduleJob(job, trigger); //添加到定时管理器
	}
	/**
	 * 修改任务时间
	 * @param clazz
	 * @param taskid
	 * @param group
	 * @param time
	 * @throws ParseException
	 * @throws SchedulerException
	 */
	public void modifyJobTime(Class<? extends Job> clazz,String taskid,String group,String time) throws ParseException, SchedulerException{
		Scheduler sched = schedulerFactory.getScheduler();
		TriggerKey tkey = new TriggerKey(TASK.concat(taskid),TRIGGER.concat(taskid.toString()));
		JobKey jkey = new JobKey(TASK.concat(taskid),JOB.concat(taskid.toString()));
		Trigger trigger =  sched.getTrigger(tkey);
		JobDetail job = sched.getJobDetail(jkey);
		if(job==null && trigger==null){
			CreateJob(clazz,Integer.parseInt(taskid),time);
			return;
		}
		if(trigger != null){
			CronTriggerImpl  ct = (CronTriggerImpl)trigger;
			 ct.setCronExpression(time);
		}
		PushJob(taskid,group);
		DeleteJob(taskid,group);
		sched.scheduleJob(job,trigger);
	}
	
	/**
	 * 删除任务
	 * @param taskid
	 * @param group
	 * @throws SchedulerException
	 */
	public void DeleteJob(String taskid,String group)throws SchedulerException{
		TriggerKey tkey = new TriggerKey(TASK.concat(taskid),TRIGGER.concat(taskid.toString()));
		schedulerFactory.getScheduler().unscheduleJob(tkey);
	}
	/**
	 * 停止任务调度
	 * @param TriggerKey
	 * @return 
	 */
	public void PushJob(String taskid,String group) throws SchedulerException{
		TriggerKey tkey = new TriggerKey(TASK.concat(taskid),TRIGGER.concat(taskid.toString()));
		schedulerFactory.getScheduler().pauseTrigger(tkey);
	} 
	
	/**
	 * 恢复任务调度
	 * @param TriggerKey
	 * @return 
	 */
	public void RestartJob(Class<? extends Job> clazz,String taskid,String group,String time)throws SchedulerException{
		Scheduler sched = schedulerFactory.getScheduler();
		TriggerKey tkey = new TriggerKey(TASK.concat(taskid),TRIGGER.concat(taskid.toString()));
		JobKey jkey = new JobKey(TASK.concat(taskid),JOB.concat(taskid.toString()));
		Trigger trigger =  sched.getTrigger(tkey);
		JobDetail job = sched.getJobDetail(jkey);
		if(job==null && trigger==null){
			CreateJob(clazz,Integer.parseInt(taskid),time);
			return;
		}
		schedulerFactory.getScheduler().resumeTrigger(tkey);;  
	}
	
	/**
	 * 删除任务
	 * @param t	askid
	 * @param group
	 */
	public void RemoveJob(String taskid,String group)throws SchedulerException{
		Scheduler sched = schedulerFactory.getScheduler();
		TriggerKey tkey = new TriggerKey(TASK.concat(taskid),TRIGGER.concat(taskid.toString()));
		JobKey jkey = new JobKey(TASK.concat(taskid),JOB.concat(taskid.toString()));
		Trigger trigger =  sched.getTrigger(tkey);
		JobDetail job = sched.getJobDetail(jkey);
		if(trigger!=null){
			schedulerFactory.getScheduler().unscheduleJob(tkey);
		}
		if(job!=null){
			sched.deleteJob(jkey);
		}
	}
	
	/**
	 * 任务管理器销毁
	 * @throws SchedulerException
	 */
	@PreDestroy
	public void destory() throws SchedulerException{
		if(schedulerFactory!=null)
			schedulerFactory.getScheduler().shutdown();
	}
}
