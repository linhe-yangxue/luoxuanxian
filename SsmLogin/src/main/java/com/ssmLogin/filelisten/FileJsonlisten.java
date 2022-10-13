package com.ssmLogin.filelisten;

import java.io.File;
import java.lang.reflect.Method;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ssmCore.file.FileUtil;
import com.ssmCore.tool.spring.SpringContextUtil;

@Service
public class FileJsonlisten extends FileAlterationListenerAdaptor {

    private static final Logger log = LoggerFactory.getLogger(FileJsonlisten.class);

    public void onFileCreate(File file) {
        String name = file.getName();
        name = StringUtils.substring(name, 0, name.indexOf('.'));
        if (checkPlatform(FileUtil.FileToJson(file), name)) {
            log.info("平台文件：" + file.getName() + "创建完成！***");
            return;
        }
        log.info("文件：" + file.getName() + "创建无效！***");
    }

    public void onFileChange(File file) {
        String name = file.getName();
        name = StringUtils.substring(name, 0, name.indexOf('.'));
        if (checkPlatform(FileUtil.FileToJson(file), name)) {
            log.info("平台文件：" + file.getName() + "更新完成！***");
            return;
        }
        ;
        log.info("文件：" + file.getName() + "更新无效！***");
    }

    public void onFileDelete(File file) {
        log.warn("文件：" + file.getName() + "被删除！***");
    }

    private boolean checkPlatform(String json, String filename) {
        try {
            Object obj = SpringContextUtil.getBean(filename.toLowerCase());
            Class<?> clz = Class.forName(obj.getClass().getName());
            Method mod = clz.getMethod("upLoad", String.class);
            mod.invoke(clz.newInstance(), json);
            return true;
        } catch (Exception e) {
            System.out.println("没有查找到平台表单：" + filename);
            e.printStackTrace();
        }
        return false;
    }

}
