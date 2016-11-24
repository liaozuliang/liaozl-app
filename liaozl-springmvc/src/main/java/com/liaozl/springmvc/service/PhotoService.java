package com.liaozl.springmvc.service;

import com.liaozl.springmvc.model.PhotoData;

import java.util.List;

public interface PhotoService {
	
	/**
	 * 获取照片数据
	 * @author liaozuliang
	 * @date 2015年8月3日
	 * @param cityName
	 * @return
	 */
	public List<PhotoData> getCityPhoto(String cityName);
	
	/**
	 * 获取城市
	 * @author liaozuliang
	 * @date 2015年8月3日
	 * @return
	 */
	public List<String> getCity();
}
