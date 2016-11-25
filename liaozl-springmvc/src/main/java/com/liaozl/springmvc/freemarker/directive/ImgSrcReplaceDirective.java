package com.liaozl.springmvc.freemarker.directive;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * 图片路径替换
 * @author liaozuliang
 * @date 2016年3月15日
 */
@Service("imgSrcReplaceDirective")
public class ImgSrcReplaceDirective implements TemplateDirectiveModel {

	private static final Logger log = LoggerFactory.getLogger(ImgSrcReplaceDirective.class);
	
	private static final String IMG_SIZE = "size";
	private static final String IMG_URL = "oldUrl";

	@Override
	public void execute(Environment env, Map paramMap, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		try {
			String size = paramMap.get(IMG_SIZE).toString();
			if (StringUtils.isBlank(size)) {
				size = "/180x135/";
			} else {
				size = "/" + size + "/";
			}

			String url = paramMap.get(IMG_URL).toString();
			if (StringUtils.isBlank(url)) {
				throw new TemplateModelException("This directive parameter oldUrl is required");
			}

			if (body == null) {
				throw new TemplateModelException("This directive body is required");
			}
			
			System.out.println("oldUrl: " + url);
			
			url = url.replace("I.QFANGIMG.COM", "is2.qfangimg.com")
					.replaceAll("/([0-9]+)x([0-9]+)/", size)
					.replace("/{size}/", size)
					.replace("/original/", size);
			
			System.out.println("newUrl: " + url);

			if (body != null) {
				if (loopVars.length > 0) {
					StringModel model = new StringModel(url, new BeansWrapper());
					loopVars[0] = model;
				}
				body.render(env.getOut());
			}
		} catch (Exception e) {
			log.error("替换图片路径出错：", e);
		}
	}
}
