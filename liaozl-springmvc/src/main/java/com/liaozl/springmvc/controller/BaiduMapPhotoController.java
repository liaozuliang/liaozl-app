package com.liaozl.springmvc.controller;

import com.liaozl.springmvc.model.PhotoData;
import com.liaozl.springmvc.service.PhotoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/map/baidu/photo")
public class BaiduMapPhotoController extends BaseController {
	
	@Resource
	private PhotoService photoService;
	
	
	@RequestMapping("index")
	public String index(ModelMap modelMap) {
		List<String> cityList = photoService.getCity();

		String currentCity = "深圳";
		if (cityList != null && cityList.size() > 0) {
			currentCity = cityList.get(0);
		}

		modelMap.put("currentCity", currentCity);
		modelMap.put("cityList", cityList);

		return "/map/BaiduMapPhoto";
	}
	
	@RequestMapping("index2")
	public String index2(ModelMap modelMap) {
		return "/map/BaiduMapPhoto2";
	}
	
	@RequestMapping("queryPhoto")
	@ResponseBody
	public ModelMap queryPhoto(ModelMap modelMap, String cityName) {
		List<PhotoData> photoList = photoService.getCityPhoto(cityName);
		if (photoList == null) {
			photoList = Collections.EMPTY_LIST;
		}

		modelMap.put("photoList", photoList);

		return modelMap;
	}
}
