package com.liaozl.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/test/img")
public class ImgSrcReplaceController {
	
	@RequestMapping("")
	public String imgTest(ModelMap map){
		String url = "http://yun50.qfangimg.com/group1/600x480/M00/81/D3/CpmSzlaTm_SAfUCGAAEFKWDFv0c007.jpg";
		map.put("url", url);
		return "/test/ImgSrcReplaceTest";
	}
	
}
