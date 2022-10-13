package com.ssmData.config.constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 配置文件加载器
 *
 */
@Service
public class ConfigLoad extends FileAlterationListenerAdaptor {

	private static final Logger log = LoggerFactory.getLogger(ConfigLoad.class);
	private static final String PACKAGE ="com.ssmData.config.impl.";//配置文件包名前缀
	/**
	 * 加载初始化配置文件
	 * 
	 * @param strPath
	 */
	public void initConfig(String strPath) {
		List<File> files = (List<File>) FileUtils.listFiles(new File(strPath), TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE);
		for (File file : files) {
			String key = null;
			try {
				key = file.getName();
				key = StringUtils.substring(key,0,key.lastIndexOf('.'));
				pressJson(key, FileToJson(file));
//				log.info("配置文件：" + key + " 加载成功！");
			} catch (Exception e) {
				log.info("配置文件加载内存失败！文件为：" + key);
				continue;
			}
		}
	}

	public static void main(String[] args) {
		System.out.println(StringUtils.substring(".svn",0,".svn".lastIndexOf('.')));
	}

	// 创建文件
	public void onFileCreate(File file) {
		String key = null;
		try{
			key = file.getName();
			key = StringUtils.substring(key,0,key.lastIndexOf('.'));
			pressJson(key, FileToJson(file));
		}catch(Exception e){
			log.info("配置文件创建失败！文件为：" + key,e);
		}
	}

	// 修改文件
	public void onFileChange(File file) {
		String key = null;
		try{
			key = file.getName();
			key = StringUtils.substring(key,0,key.lastIndexOf('.'));
			pressJson(key, FileToJson(file));
		}catch(Exception e){
			log.info("配置文件修改失败！文件为：" + key,e);
		}
	}

	// 删除文件
	public void onFileDelete(File file) {
		System.out.println("文件删除");
	}

	/**
	 * json配置文件加载和更新
	 * 
	 * @param filename
	 *            文件名称
	 * @param json
	 *            json文件文本
	 * @throws Exception 
	 */
	public void pressJson(String filename, String json) throws Exception{
		if (filename != null) {
			//json = StringUtils.replace(json, " ", "");
			json = StringUtils.replace(json, "\n", "");
			json = StringUtils.replace(json, "\t", "");
			json = StringUtils.replace(json, "\r", "");
			Class<?> clz = Class.forName(PACKAGE.concat("T" + filename + "Impl"));
			Method mod = clz.getMethod("loading", String.class);
			mod.invoke(clz.newInstance(), json);
		}
	}

	/**
	 * 文件转json字符串
	 * 
	 * @param file
	 * @return
	 */
	public static String FileToJson(File file) {
		Long filelength = file.length();
		byte[] bytes = new byte[filelength.intValue()];
		FileInputStream _in = null;
		try {
			_in = new FileInputStream(file);
			_in.read(bytes);
			if ((bytes[0] == (byte) 0xEF) && (bytes[1] == (byte) 0xBB) && (bytes[2] == (byte) 0xBF)) {
				byte[] by = new byte[filelength.intValue() - 3];
				System.arraycopy(bytes, 3, by, 0, filelength.intValue() - 3);
				return new String(by, "utf-8");
			} else {
				return new String(bytes, "utf-8");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				_in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
