package com.liaozl.springmvc.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * @author liaozuliang
 * @date 2015年9月22日
 */
public class SpringCxt implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		SpringCxt.applicationContext = context;
	}
	
	public static void setAppContext(ApplicationContext context) {
		SpringCxt.applicationContext = context;
	}
	
	public static ApplicationContext getApplicationContext() {
        return applicationContext;
	}
	
	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}
	
	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}
	
}
