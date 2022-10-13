package com.ssmData.initaliztion;

import java.io.File;
import java.lang.reflect.Method;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ssmCore.file.FileUtil;

@Service
public class FileJsonlisten extends FileAlterationListenerAdaptor {
	
	private static final Logger log = LoggerFactory.getLogger(FileJsonlisten.class); 
	private @Value("${FILE_PACK}")String pack;  //文件对应的实体报
	private static final String IMPL="Impl";
	
	public void onFileCreate(File file) {
		String name = file.getName();
		name = StringUtils.substring(name,0,name.indexOf('.'));
		if(checkPlatform(FileUtil.FileToJson(file),name)){
			log.info("平台文件："+ file.getName() + "创建完成！***");
			return;
		}
		log.info("文件："+ file.getName() + "创建无效！***");
	}

	public void onFileChange(File file) {
		String name = file.getName();
		name = StringUtils.substring(name,0,name.indexOf('.'));
		if(checkPlatform(FileUtil.FileToJson(file),name)){
			log.info("平台文件："+ file.getName() + "更新完成！***");
			return;
		};
		log.info("文件："+ file.getName() + "更新无效！***");
	}

	public void onFileDelete(File file) {
		log.warn("文件："+ file.getName() + "被删除！***");
	}
	
	private boolean checkPlatform(String json,String filename){
		try{
			Class<?> clz = Class.forName(pack + filename + IMPL);
			Method mod = clz.getMethod("upLoad", String.class);
			mod.invoke(clz.newInstance(), json);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
}
