package com.ssmCore.file;

import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class FileCleaner {

	private static final Logger log = LoggerFactory.getLogger(FileCleaner.class);
	private FileAlterationObserver observer;
	private FileAlterationMonitor monitor;

	public void FileMonitor(String fOlDER,String type,FileAlterationListenerAdaptor filelistener) throws Exception {

		String rootDir = fOlDER;// 文件路径
		long interval = TimeUnit.SECONDS.toMillis(2);
		observer = new FileAlterationObserver(rootDir, FileFilterUtils.or(FileFilterUtils.directoryFileFilter(),
				FileFilterUtils.suffixFileFilter(type)));

		observer.addListener(filelistener);
		monitor = new FileAlterationMonitor(interval, observer);
		monitor.start();
		log.info("文件夹监听服务启动!----文件夹路径：" + fOlDER);
	}

	@PreDestroy
	public void destoryFile() {
		try {
			if (monitor != null) {
				monitor.stop();
				observer.destroy();
				monitor = null;
				observer = null;
			}
		} catch (Exception e) {
			log.warn("文件服务关闭出错！", e);
		}
	}

}
