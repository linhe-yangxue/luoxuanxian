package com.ssmCore.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class FileUtil {
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

	/**
	 * 获取文件 URLClassLoader 资源
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static ClassLoader getClassLoader(String path) throws Exception {

		List<File> files = (List<File>) FileUtils.listFiles(new File(path), TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE);

		if (files.size() > 0) {
			URLClassLoader urlLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Class<URLClassLoader> sysclass = URLClassLoader.class;
			Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
			method.setAccessible(true);
			for (int i = 0; i < files.size(); i++) {
				try {
					method.invoke((Object) urlLoader, files.get(i).toURL());
				} catch (MalformedURLException e) {
					continue;
				}
			}
			return urlLoader;
		}
		return null;
	}

	public static ClassLoader getOneClassLoader(String path) throws Exception {
		URL url = new URL("file:" + path);
		URLClassLoader urlLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class<URLClassLoader> sysclass = URLClassLoader.class;
		Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
		method.setAccessible(true);
		method.invoke((Object) urlLoader, url);
		return urlLoader;
	}
}
