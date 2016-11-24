package com.liaozl.springmvc.freemarker;

import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;


/**
 * 自定义扩展freemarker视图解析�?
 * @author liaozuliang
 * @date 2014-12-31
 */
public class MyFreeMarkerViewResolver extends FreeMarkerViewResolver {
	
	public String getPrefix() {
		return super.getPrefix();
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected Class requiredViewClass() {
		return MyFreeMarkerView.class;
	}
	
}
