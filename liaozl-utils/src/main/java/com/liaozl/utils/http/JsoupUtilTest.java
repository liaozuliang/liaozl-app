package com.liaozl.utils.http;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.liaozl.utils.http.JsoupUtil;
import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liaozl.photo.util.md5.SecurityMD5Util;

public class JsoupUtilTest {


	@Test
	public void testDoRequest() {
		String url = "http://crm.qfang.com/crm-web/interface/broker/login";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userName", "18616322650");

		JSONObject json = JsoupUtil.doPost(url, paramMap);
		Assert.assertTrue(json != null);
	}

	@Test
	public void testDoRequest2() {
		String url = "http://news.baidu.com/";
		
		String str = JsoupUtil.doPost2(url, null);
		System.out.println(str);

		Assert.assertTrue(str != null);
	}

	public static void test(){
		String url = "http://193.168.0.61/qfang-dictionary/api/housesDealList";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("date", "201506");
		paramMap.put("city", "SHENZHEN");
	
		int currentPage = 0;
		while(true){
			currentPage++;
			paramMap.put("page", currentPage+"");
			
			JSONObject json = JsoupUtil.doPost(url, paramMap);
			if(json==null){
				break;
			}
			
			JSONArray dataList = json.getJSONArray("LIST");
			if(dataList!=null && dataList.size()>0){
				for(int i=0;i<dataList.size();i++){
					JSONObject data = dataList.getJSONObject(i);
					if("+73Q7DKmGUaxHzHKvqMeZUNEoho=".equals(data.getString("GRADENID"))){//gardenId:56268
						System.out.print(data.getString("MONTH")+" ");
						System.out.print(data.getString("TRANSACTIONDATE")+" ");
						System.out.print(data.getString("DEALPRICE")+" ");		
						System.out.print(data.getString("TOTALFLOORS")+" ");	
						System.out.print(data.getString("GOVSTATUS")+" ");		
						System.out.print(data.getString("AREA")+" ");	
						//System.out.print(data.getString("FLOORNUMBER")+" ");		
						//System.out.print(data.getString("BALCONY")+" ");		
						//System.out.print(data.getString("BATHROOM")+" ");		
						//System.out.print(data.getString("BEDROOM")+" ");		
						//System.out.print(data.getString("DIRECTION")+" ");		
						//System.out.print(data.getString("GRADENID")+" ");		
						//System.out.print(data.getString("KITCHEN")+" ");	
						//System.out.print(data.getString("LIVINGROOM")+" ");	
						
						System.out.println();
					}
				}
			}
			
			int pageCount = json.getInteger("PAGECOUNT");
			if(currentPage>=pageCount){
				break;
			}
		}
		
	}
	
	public static void test2() throws NoSuchAlgorithmException {
		String url = "http://http://192.168.0.241:9292/qfang-manager/manager/api/broker/login";
		String password = "88888888";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("accountLinkId", "");
		paramMap.put("phone", "15014142020");
		paramMap.put("password", SecurityMD5Util.getInstance().MD5Encode(password));
		
		System.out.println(JsoupUtil.doPost(url, paramMap));
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		test();
		//test2();
	}
}
