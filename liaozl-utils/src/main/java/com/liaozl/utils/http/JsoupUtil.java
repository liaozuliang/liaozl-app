package com.liaozl.utils.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONObject;

/**
 * http请求
 * 
 * @author liaozuliang
 * @date 2015年9月18日
 */
public class JsoupUtil {

	static final Logger log = Logger.getLogger(JsoupUtil.class);

	static final int MAX_SIZE = 1 * 1024 * 1024 * 1024;
	static final int TIME_OUT = 60 * 1000;

	/**
	 * 发起http请求
	 * @Author liaozuliang
	 * @Date 2016年8月24日
	 * @param url 请求地址
	 * @param doPost 是否发起POST请求，否则发起GET请求
	 * @param paramMap 请求参数
	 * @param headerMap 请求头部信息
	 * @param fileMap 要上传的文件（key:文件名称， value:文件地址）
	 * @return
	 */
	public static Document doHttpRequest(String url, boolean doPost, Map<String, String> paramMap,
			Map<String, String> headerMap, Map<String, String> fileMap) {
		if (paramMap == null) {
			paramMap = new HashMap<String, String>();
		} else {
			checkParam(paramMap);
		}

		try {
			Connection connection = Jsoup.connect(url);

			// 设置大小限制、超时时间
			connection.ignoreContentType(true).maxBodySize(MAX_SIZE).timeout(TIME_OUT);

			// 设置请求参数
			connection.data(paramMap);

			// 设置头部信息
			if (headerMap != null) {
				for (String key : headerMap.keySet()) {
					if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(headerMap.get(key))) {
						connection.header(key, headerMap.get(key));
					}
				}
			}

			// 设置上传文件
			if (fileMap != null) {
				for (String key : fileMap.keySet()) {
					if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(fileMap.get(key))) {
						connection.data(key, key, new FileInputStream(fileMap.get(key)));
					}
				}
			}

			// 发起请求
			Document doc = null;
			if (doPost) {
				doc = connection.post();
			} else {
				doc = connection.get();
			}

			return doc;
		} catch (IOException e) {
			log.error("JsoupUtil [doHttpRequest] error: ", e);
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
	
	public static String doRequestReturnDocument(String url, boolean doPost, Map<String, String> paramMap,
			Map<String, String> headerMap, Map<String, String> fileMap) {
		try {
			Document doc = doHttpRequest(url, doPost, paramMap, headerMap, fileMap);
			return doc.html();
		} catch (Exception e) {
			log.error("JsoupUtil [doRequestReturnDomHtml] error: ", e);
		}

		return null;
	}
	
	public static String doRequest(String url, boolean doPost, Map<String, String> paramMap,
			Map<String, String> headerMap, Map<String, String> fileMap) {
		try {
			Document doc = doHttpRequest(url, doPost, paramMap, headerMap, fileMap);
			String body = doc.select("body").html();
			return body;
		} catch (Exception e) {
			log.error("JsoupUtil [doRequest] error: ", e);
		}

		return null;
	}

	public static JSONObject doPost(String url, Map<String, String> paramMap) {
		try {
			String body = doRequest(url, true, paramMap, null, null);
			return JSONObject.parseObject(body);
		} catch (Exception e) {
			log.error("JsoupUtil [doPost] error: ", e);
		}

		return null;
	}

	public static String doPost2(String url, Map<String, String> paramMap) {
		try {
			String body = doRequest(url, true, paramMap, null, null);
			return body;
		} catch (Exception e) {
			log.error("JsoupUtil [doPost2] error: ", e);
		}

		return null;
	}

	public static String doPost(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
		try {
			String body = doRequest(url, true, paramMap, headerMap, null);
			return body;
		} catch (Exception e) {
			log.error("JsoupUtil [doPost] error: ", e);
		}

		return null;
	}

	public static String doGet(String url, Map<String, String> paramMap) {
		try {
			String body = doRequest(url, false, paramMap, null, null);
			return body;
		} catch (Exception e) {
			log.error("JsoupUtil [doGet] error: ", e);
		}

		return null;
	}

	public static JSONObject doGet2(String url, Map<String, String> paramMap) {
		try {
			String body = doRequest(url, false, paramMap, null, null);
			return JSONObject.parseObject(body);
		} catch (Exception e) {
			log.error("JsoupUtil [doGet2] error: ", e);
		}

		return null;
	}

	public static String doGet(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
		try {
			String body = doRequest(url, false, paramMap, headerMap, null);
			return body;
		} catch (Exception e) {
			log.error("JsoupUtil [doGet] error: ", e);
		}

		return null;
	}

	public static Document doGet2(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
		try {
			Document doc = doHttpRequest(url, false, paramMap, headerMap, null);
			return doc;
		} catch (Exception e) {
			log.error("JsoupUtil [doGet2] error: ", e);
		}

		return null;
	}
	
	public static void test2() {
		String jsonString = "";
		String url = "http://192.168.10.32:9090/niiwoo-api/niwoportservice.svc/BatchSaveBGUAbilityScore?v=3.3";
		
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("jsonString", jsonString);
		
		String str = JsoupUtil.doPost(url, map, null);
		System.out.println(str);
	}
	
	public static void test1() {
		String url = "http://www.baidu.com";
		String str = JsoupUtil.doPost(url, null, null);
		System.out.println(str);
	}
	
	public static void main(String[] args) {
		test1();
		//test2();
	}
}
