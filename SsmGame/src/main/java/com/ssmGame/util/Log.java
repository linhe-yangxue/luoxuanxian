package com.ssmGame.util;

import org.apache.log4j.Logger;

/**
 * Log工具类
 * Created by WYM on 2016/10/26.
 */
public class Log {

    /**
     * log4j对象
     */
    public static Logger logger = Logger.getLogger(Log.class);

    /**
     * 打印一条log
     * @param obj
     */
    public static void print(Object obj){
        System.out.print(obj);
        System.out.print("\r\n");
    }

    /**
     * 打印一条error
     * @param obj
     */
    public static void err(Object obj){
        System.err.print(obj);
        System.err.print("\r\n");
    }

}
