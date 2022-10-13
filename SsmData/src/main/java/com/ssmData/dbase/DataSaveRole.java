package com.ssmData.dbase;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;


@Service
@Scope("prototype")
public class DataSaveRole implements Runnable {
	public static final BlockingQueue<BaseDataSource> savedb = new LinkedBlockingQueue<BaseDataSource>();
	public static final ExecutorService dataDb = Executors.newSingleThreadExecutor();
	public static final ExecutorService dataDb1 = Executors.newSingleThreadExecutor();
	public static final ExecutorService dataDb3 = Executors.newSingleThreadExecutor();

	public static DataSaveRole _instance(){
		return SpringContextUtil.getBean(DataSaveRole.class);
	}
		
	/// 每一个线程池 三个线程 ，线程安全队列
	public void start() {
		if(!DataSaveRole.dataDb.isShutdown())
			DataSaveRole.dataDb.execute(_instance());
		if(!DataSaveRole.dataDb1.isShutdown())
			DataSaveRole.dataDb1.execute(_instance());
		if(!DataSaveRole.dataDb3.isShutdown())
			DataSaveRole.dataDb3.execute(_instance());
	}

	@PreDestroy
	public void stop() {
		if(dataDb3.isShutdown())
			dataDb3.shutdown();
		if(dataDb1.isShutdown())
			dataDb1.shutdown();
		if(dataDb.isShutdown())
			dataDb.shutdown();
	}

	/// 存库
	@Override
	public void run() {
		while (true) {
			BaseDataSource obj;
			try {
				obj = savedb.take();
				obj.onSave();
				obj.destory();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				obj = null;
			}
		}
	}
}
