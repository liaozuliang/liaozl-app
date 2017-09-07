package com.liaozl.dao.base;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 解决mybatis mapping文件配置错误启动时无提示信息的问题
 * @author liaozuliang
 * @date 2017-09-07
 */
public class LiaozlSqlSessionFactoryBean extends SqlSessionFactoryBean {

    private final Logger logger = LoggerFactory.getLogger(LiaozlSqlSessionFactoryBean.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            super.afterPropertiesSet();
        } catch (Exception e) {
            logger.error(e.getMessage());//堆栈信息过长，只打印e.getMessage()

            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e1) {
            }

            throw e;
        }
    }
}
