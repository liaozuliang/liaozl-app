package com.liaozl.springmvc.service.impl;

import com.liaozl.springmvc.model.PhotoData;
import com.liaozl.springmvc.service.PhotoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("photoService")
public class PhotoServiceImpl implements PhotoService {

	private static Map<String, List<PhotoData>> pdMap;

	private void loadPhotoData() {
		pdMap = new HashMap<String, List<PhotoData>>();

		// =================深圳=====================
		PhotoData pd = new PhotoData();
		pd.setId();
		pd.setName("中山公园");
		pd.setCity("深圳");
		pd.setLongitude(113.92394d);
		pd.setLatitude(22.553087d);
		pd.setCreateTime(new Date());
		pd.setUrl("http://yun50.qfangimg.com/group1/180x135/M00/C2/7E/CpmSzVXBaxaAZPD2AAC4A068pnw070.jpg");
		pd.setDesc("周末出游-中山公园");

		List<PhotoData> pdList = null;
		if (pdMap.containsKey(pd.getCity())) {
			pdList = pdMap.get(pd.getCity());
			if (pdList == null) {
				pdList = new ArrayList<PhotoData>();
			}
		} else {
			pdList = new ArrayList<PhotoData>();
		}
		pdList.add(pd);
		pdMap.put(pd.getCity(), pdList);

		pd = new PhotoData();
		pd.setId();
		pd.setName("梧桐山");
		pd.setCity("深圳");
		pd.setLongitude(114.207121d);
		pd.setLatitude(22.581219d);
		pd.setCreateTime(new Date());
		pd.setUrl("http://yun50.qfangimg.com/group1/180x135/M00/D6/1A/CpmSzVWGJIaAfre7AAC-nLqce5I813.jpg");
		pd.setDesc("周末出游-梧桐山");

		pdList = null;
		if (pdMap.containsKey(pd.getCity())) {
			pdList = pdMap.get(pd.getCity());
			if (pdList == null) {
				pdList = new ArrayList<PhotoData>();
			}
		} else {
			pdList = new ArrayList<PhotoData>();
		}
		pdList.add(pd);
		pdMap.put(pd.getCity(), pdList);

		// =================赣州=====================
		pd = new PhotoData();
		pd.setId();
		pd.setName("陡水湖");
		pd.setCity("赣州");
		pd.setLongitude(114.415534d);
		pd.setLatitude(25.834413d);
		pd.setCreateTime(new Date());
		pd.setUrl("http://yun50.qfangimg.com/group1/180x135/M00/D6/00/CpmSzlWGJIaAE6ULAADcMvYkOmk168.jpg");
		pd.setDesc("周末出游-陡水湖");

		pdList = null;
		if (pdMap.containsKey(pd.getCity())) {
			pdList = pdMap.get(pd.getCity());
			if (pdList == null) {
				pdList = new ArrayList<PhotoData>();
			}
		} else {
			pdList = new ArrayList<PhotoData>();
		}
		pdList.add(pd);
		pdMap.put(pd.getCity(), pdList);

		pd = new PhotoData();
		pd.setId();
		pd.setName("火车站");
		pd.setCity("赣州");
		pd.setLongitude(114.970874d);
		pd.setLatitude(25.825527d);
		pd.setCreateTime(new Date());
		pd.setUrl("http://yun50.qfangimg.com/group1/180x135/M00/7D/11/CpmSzlWwvnOAWpLuAAUg3N40gTQ918.png");
		pd.setDesc("周末出游-火车站");

		pdList = null;
		if (pdMap.containsKey(pd.getCity())) {
			pdList = pdMap.get(pd.getCity());
			if (pdList == null) {
				pdList = new ArrayList<PhotoData>();
			}
		} else {
			pdList = new ArrayList<PhotoData>();
		}
		pdList.add(pd);
		pdMap.put(pd.getCity(), pdList);
	}

	@Override
	public List<PhotoData> getCityPhoto(String cityName) {
		if (pdMap == null) {
			synchronized (PhotoServiceImpl.class) {
				loadPhotoData();
			}
		}
		return pdMap.get(cityName);
	}

	@Override
	public List<String> getCity() {
		if (pdMap == null) {
			synchronized (PhotoServiceImpl.class) {
				loadPhotoData();
			}
		}

		return Arrays.asList(pdMap.keySet().toArray(new String[] {}));
	}

}
