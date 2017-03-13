package com.liaozl.quartz.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author liaozuliang
 * @date 2017-03-13
 */
@Service("timeJob")
public class TimeJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(TimeJob.class);

    // 定时器是否正在执行
    private static final AtomicBoolean running = new AtomicBoolean(false);

    private static final String JOB_NAME = "TimeJob";

    @Override
    public void execute() {
        if (running.get()) {
            logger.info("定时器[" + JOB_NAME + "]正在执行，请稍后。。。");
            return;
        }

        running.set(true);
        logger.info("开始执行定时器[" + JOB_NAME + "]");

        try {
            doJob();
        } catch (Exception e) {
            logger.error("定时器[" + JOB_NAME + "]执行出错：", e);
        }

        running.set(false);
        logger.info("定时器[" + JOB_NAME + "]执行完成");
    }

    private void doJob() {
        Date now = new Date();
        System.out.println("测试一下定时器[" + JOB_NAME + "]" + now.toLocaleString());
    }

}
