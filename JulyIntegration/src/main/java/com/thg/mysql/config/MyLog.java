package com.thg.mysql.config;

import org.apache.ibatis.logging.log4j2.Log4j2Impl;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/13 22:23
 **/
public class MyLog extends Log4j2Impl {

    public MyLog(String clazz) {
        super(clazz);
    }

    @Override
    public void debug(String s) {
        if(s.startsWith("<==")||s.startsWith("==>")){
            System.out.println(s);
        }
        super.debug(s);
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }
}
