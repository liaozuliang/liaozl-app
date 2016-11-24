package com.liaozl.utils.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpClientUtil {

	private static final Logger log = Logger.getLogger(HttpClientUtil.class);

	public static String doGet(String url) {
		String content = null;

		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

		HttpGet httpGet = new HttpGet(url);
		System.out.println(httpGet.getRequestLine());

		try {
			HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			System.out.println("status:" + httpResponse.getStatusLine());

			if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
				// System.out.println("contentEncoding:" + entity.getContentEncoding());
				content = EntityUtils.toString(entity);
			}
		} catch (IOException e) {
			log.error("doGet error: ", e);
		} finally {
			try {
				closeableHttpClient.close();
			} catch (Exception e) {
				log.error("doGet error: ", e);
			}
		}

		return content;
	}

	public static String doGet(String url, Map<String, String> headerMap) {
		String content = null;

		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

		// closeableHttpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		// closeableHttpClient.getParams().setParameter("http.protocol.single-cookie-header", true);

		HttpGet httpGet = new HttpGet(url);

		if (headerMap != null) {
			for (String key : headerMap.keySet()) {
				if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(headerMap.get(key))) {
					httpGet.addHeader(key, headerMap.get(key));
				}
			}
		}

		System.out.println(httpGet.getRequestLine());

		try {
			HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			System.out.println("status:" + httpResponse.getStatusLine());

			if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
				// System.out.println("contentEncoding:" + entity.getContentEncoding());
				content = EntityUtils.toString(entity);
			}
		} catch (IOException e) {
			log.error("doGet error: ", e);
		} finally {
			try {
				closeableHttpClient.close();
			} catch (Exception e) {
				log.error("doGet error: ", e);
			}
		}

		return content;
	}

	public static String doPost(String url) {
		String content = null;

		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

		HttpPost httpPost = new HttpPost(url);
		System.out.println(httpPost.getRequestLine());

		try {
			HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			System.out.println("status:" + httpResponse.getStatusLine());

			if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
				// System.out.println("contentEncoding:" + entity.getContentEncoding());
				content = EntityUtils.toString(entity);
			}
		} catch (IOException e) {
			log.error("doGet error: ", e);
		} finally {
			try {
				closeableHttpClient.close();
			} catch (Exception e) {
				log.error("doGet error: ", e);
			}
		}

		return content;
	}
	
	
	public static String doPost(String url, Map<String, String> headerMap) {
		String content = null;

		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

		HttpPost httpPost = new HttpPost(url);
		if (headerMap != null) {
			for (String key : headerMap.keySet()) {
				if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(headerMap.get(key))) {
					httpPost.addHeader(key, headerMap.get(key));
				}
			}
		}
		
		System.out.println(httpPost.getRequestLine());

		try {
			HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			System.out.println("status:" + httpResponse.getStatusLine());

			if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
				// System.out.println("contentEncoding:" + entity.getContentEncoding());
				content = EntityUtils.toString(entity);
			}
		} catch (IOException e) {
			log.error("doGet error: ", e);
		} finally {
			try {
				closeableHttpClient.close();
			} catch (Exception e) {
				log.error("doGet error: ", e);
			}
		}

		return content;
	}

	public static void main(String[] args) {
		String url = "http://www.baidu.com/";
		// System.out.println(doGet(url));

		url = "http://shanghai.qfang.com/";
		// System.out.println(doGet(url));

		Map<String, String> headerMap = new HashMap<String, String>();
		
		headerMap.put("Host", "www.91160.com");
		headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0");
		headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		headerMap.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		headerMap.put("Accept-Encoding", "gzip, deflate");
		headerMap.put("Cookie", "testcookie=yes; __guid=YDuanf575e69da814115.03459441; ip_city=sz; PHPSESSID=0ir0tb5kc2rv8e5v43jfj26383; __jsluid=131c257435d6ca378b008166e8f50eba; NY_VALIDATE_KEY=8761377fcb05c80d0eb72e48cb8a4b53; _ga=GA1.2.1611631694.1465805230; _gat=1; Hm_lvt_c4e8e5b919a5c12647962ea08462e63b=1465805230; Hm_lpvt_c4e8e5b919a5c12647962ea08462e63b=1465805296; vlstatId=vlstat-1465805230000-472772089");
		headerMap.put("Connection", "keep-alive");
		headerMap.put("Cache-Control", "max-age=0");
		
		url = "http://www.91160.com/doctors/index/docid-9833.html";
		System.out.println(doGet(url));
		System.out.println(doGet(url, headerMap));
		System.out.println(doPost(url, headerMap));

		url = "http://www.91160.com/doctors/index/docid-14683.html";
		//System.out.println(doGet(url, headerMap));
	}
}
