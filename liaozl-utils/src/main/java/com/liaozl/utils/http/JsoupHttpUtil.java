package com.liaozl.photo.util.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONObject;

/**
 * jsoup http 请求
 * @author liaozuliang
 * @date 2016年3月29日
 */
public class JsoupHttpUtil {

	private static final Logger log = Logger.getLogger(JsoupHttpUtil.class);

	private static final int MAX_SIZE = 1 * 1024 * 1024 * 1024; // 1GB
	private static final int TIME_OUT = 60 * 1000; // 60秒

	/**
	 * @author liaozuliang
	 * @date 2016年3月29日
	 * @param url 请求地址（必填）
	 * @param paramMap 参数
	 * @param timeOut 超时
	 * @return
	 */
	public static JSONObject doPost(String url, Map<String, String> paramMap, Integer timeOut) {
		if (paramMap == null) {
			paramMap = new HashMap<String, String>();
		} else {
			checkParam(paramMap);
		}

		if (timeOut == null || timeOut <= 0) {
			timeOut = TIME_OUT;
		}

		try {
			Document doc = Jsoup.connect(url).ignoreContentType(true).maxBodySize(MAX_SIZE)
					.data(paramMap).timeout(timeOut).post();
			String body = doc.select("body").text().toString();
			return JSONObject.parseObject(body);
		} catch (IOException e) {
			log.error("JsoupHttpUtil [doPost] error: ", e);
		}

		return null;
	}

	/**
	 * @author liaozuliang
	 * @date 2016年3月29日
	 * @param url 请求地址（必填）
	 * @param paramMap 参数
	 * @param timeOut 超时
	 * @return
	 */
	public static String doPost2(String url, Map<String, String> paramMap, Integer timeOut) {
		if (paramMap == null) {
			paramMap = new HashMap<String, String>();
		} else {
			checkParam(paramMap);
		}

		if (timeOut == null || timeOut <= 0) {
			timeOut = TIME_OUT;
		}

		try {
			Document doc = Jsoup.connect(url).ignoreContentType(true).maxBodySize(MAX_SIZE)
					.data(paramMap).timeout(timeOut).post();
			String body = doc.select("body").html();
			return body;
		} catch (IOException e) {
			log.error("JsoupHttpUtil [doPost2] error: ", e);
		}

		return null;
	}

	private static void checkParam(Map<String, String> paramMap) {
		for (String key : paramMap.keySet()) {
			String value = paramMap.get(key);
			if (value == null) {
				paramMap.put(key, "");
			}
		}
	}
}
