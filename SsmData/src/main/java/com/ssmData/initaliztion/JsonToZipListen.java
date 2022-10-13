package com.ssmData.initaliztion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class JsonToZipListen extends FileAlterationListenerAdaptor {
	private static final Logger log = LoggerFactory.getLogger(JsonToZipListen.class); 
	
	private static String json_path = "";
	
	private @Value("${NET_BASE}")String net_base_path;
	
	private boolean packing = false;
	private boolean need_pack = false;
	
	public void initConfig(String strPath)
	{
		log.info("配置文件初始化打包...");
		json_path = strPath;
		List<File> files = (List<File>) FileUtils.listFiles(new File(strPath), TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE);
		PackZip(files);
	}
	
    @Scheduled(fixedRate=30000)
	private void ZipFileUpdate(){
    	//System.out.println("ZipFileUpdate need_pack " + need_pack + " packing " + packing);
    	if (!need_pack) {
    		return;
    	}
    	if (packing) {
    		return;
    	}
    	need_pack = false;
		List<File> files = (List<File>) FileUtils.listFiles(new File(json_path), TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE);
		PackZip(files);
    }
	
	public void onFileCreate(File file) {
		log.info("新添配置文件打包...");
		//List<File> files = (List<File>) FileUtils.listFiles(new File(json_path), TrueFileFilter.INSTANCE,
		//		TrueFileFilter.INSTANCE);
		//PackZip(files);
		need_pack = true;
		//System.out.println("onFileCreate pack_times " + need_pack);
	}

	public void onFileChange(File file) {
		log.info("更改配置文件打包...");
		//List<File> files = (List<File>) FileUtils.listFiles(new File(json_path), TrueFileFilter.INSTANCE,
		//		TrueFileFilter.INSTANCE);
		//PackZip(files);
		need_pack = true;
		//System.out.println("onFileChange pack_times " + need_pack);
	}

	public void onFileDelete(File file) {
		//log.info("删除配置文件打包...");
		//List<File> files = (List<File>) FileUtils.listFiles(new File(json_path), TrueFileFilter.INSTANCE,
		//		TrueFileFilter.INSTANCE);
		//PackZip(files);
		need_pack = true;
		//System.out.println("onFileDelete pack_times " + need_pack);
	}
	
	private void PackZip(List<File> files)
	{
		try
		{
			packing = true;
			File zipFile = new File(net_base_path + "/jsonzip");
			InputStream input = null;
	        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            for(int i = 0; i < files.size(); ++i){
            	File j_file = files.get(i);
            	if (j_file.isDirectory()) {
					continue;
				}
                input = new FileInputStream(j_file);
                zipOut.putNextEntry(new ZipEntry(j_file.getName()));
                int temp = 0;
                while((temp = input.read()) != -1){
                    zipOut.write(temp);
                }
                input.close();
            }
	        zipOut.close();
	        log.info("配置文件打包成功");
	        packing = false;
		}
		catch (Exception e){
			e.printStackTrace();
			packing = false;
		}
	}
}
