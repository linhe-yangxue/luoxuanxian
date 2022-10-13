package com.ssmLogin.filelisten;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.ClasspathPackageScanner;


@Service
public class FileJarlisten extends FileAlterationListenerAdaptor{
	
	@Autowired ClasspathPackageScanner scanner;
	 @Value("${FILE_PATH_JAR}") String jarPath;
	
	public void onFileCreate(File file) {
			scanner.loadjar(jarPath);
			System.out.println("sdk加载！" + file.getName()+ "成功！");
	}

	public void onFileChange(File file) {
			scanner.loadjar(file.getPath());
			System.out.println("sdk更新！" + file.getName()+ "成功！");
	}

	public void onFileDelete(File file) {
		System.out.println("文件删除！" + file.getName());
	}
	
}
