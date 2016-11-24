package com.liaozl.springmvc.freemarker;

import freemarker.template.SimpleHash;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * freemarker视图参数配置
 * @author liaozuliang
 * @date 2014-12-31
 */
public class MyFreeMarkerView extends FreeMarkerView {
	
	@Override
	protected SimpleHash buildTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		
		// 方便在模板文件中应用${ctx}
		model.put("ctx", request.getContextPath());
		
		// 方便在模板文件中应用${base} 默认情况下指向林路径/themes/default
		//model.put("base", request.getContextPath() + "/themes/default");
		model.put("base", request.getContextPath());
		
		model.put("imgSrcReplaceDirective", ContextLoader.getCurrentWebApplicationContext().getBean("imgSrcReplaceDirective"));
		
		return super.buildTemplateModel(model, request, response);
	}
}
