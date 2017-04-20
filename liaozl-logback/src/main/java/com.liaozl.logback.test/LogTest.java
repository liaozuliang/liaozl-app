package com.liaozl.logback.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liaozuliang
 * @date 2016-11-18
 */
public class LogTest {

    private static final Logger logger = LoggerFactory.getLogger(LogTest.class);

    public static void main(String[] args) {
        logger.debug("输出debug级别的日志.....");
        logger.info("输出info级别的日志.....");
        logger.error("输出error级别的日志.....");
        logger.trace("输出trace级别的日志.....");
        logger.warn("{}输出{}级别的{},{}", new String[]{"我", "warn", "日志", "哈哈"});


        logger.info("输出info级别的日志..{}.." + "111{}.", "abccc", "111abc222");
        //throw new IllegalArgumentException("哈哈");
    }

}
