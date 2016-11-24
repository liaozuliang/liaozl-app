package com.liaozl.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map/baidu")
public class BaiduMapController {
	
	@RequestMapping(value = "index")
	public String index(ModelMap modelMap) {
		return "/map/BaiduMap";
	}

}
