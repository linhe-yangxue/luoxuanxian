package com.ssmCore.tool.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.stereotype.Service;

import com.ssmCore.file.FileUtil;

@Service
public class ClasspathPackageScanner {

	private static final Logger log = LoggerFactory.getLogger(ClasspathPackageScanner.class);
	private static final String SDK = "com.jksdk";
	private ApplicationContext _ctx;

	public void init(String jarPath, ApplicationContext _ctx) {
		this._ctx = _ctx;
		try {
			ClassLoader loader = FileUtil.getClassLoader(jarPath); //多个文件加载
			if(loader==null)
				return;
			// 注册bean
			ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(
					(DefaultListableBeanFactory) _ctx.getAutowireCapableBeanFactory());
			// 使用Spring的注解自动扫描
			scanner.scan(SDK);
			DefaultListableBeanFactory factory = (DefaultListableBeanFactory) _ctx.getAutowireCapableBeanFactory();
			factory.setBeanClassLoader(loader);
		} catch (Exception e) {
			log.error("加载jar文件错误", e);
		}
	}

	/**
	 * 文件更新添加
	 * 
	 * @param filePath
	 * @param opt
	 *            1添加文件 2更新文件
	 */
	public void loadjar(String filePath) {
		try {
			ClassLoader loader = FileUtil.getClassLoader(filePath);//单个文件加载
			// 注册bean
			ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(
					(DefaultListableBeanFactory) _ctx.getAutowireCapableBeanFactory());
			// 使用Spring的注解自动扫描
			scanner.scan(SDK);
			DefaultListableBeanFactory factory = (DefaultListableBeanFactory) _ctx.getAutowireCapableBeanFactory();
			factory.setBeanClassLoader(loader);
			
		} catch (Exception e) {
			log.error("加载jar文件错误", e);
		}
	}
}
