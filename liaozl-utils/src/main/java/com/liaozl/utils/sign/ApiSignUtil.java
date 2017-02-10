package com.liaozl.utils.sign;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ApiSignUtil
 * @Description: api公共签名工具类
 * @author WangJiChao
 * @date 2017年2月6日下午4:22:07
 *
 */
public class ApiSignUtil {
	/**
	 * 获取签名原文
	 * @param paramMap
	 * @return
	 */
	public static String getSignData(Map<String,String> paramMap){
		StringBuffer content = new StringBuffer();

        // 按照key做首字母升序排列
        List<String> keys = new ArrayList<String>(paramMap.keySet());
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < keys.size(); i++)
        {
            String key = (String) keys.get(i);
            if ("sign".equals(key))
            {
                continue;
            }
            String value = paramMap.get(key);
            // 空串不参与签名
            if (isnull(value))
            {
                continue;
            }
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }
        String signSrc = content.toString();
        if (signSrc.startsWith("&"))
        {
            signSrc = signSrc.replaceFirst("&", "");
        }
        return signSrc;
	}
	
	/**
	 * 加签
	 * @param paramMap
	 * @param prikeyValue
	 * @return
	 */
	public static String createSign(Map<String,String> paramMap,String prikeyValue){
		try{
			return RSAUtil.sign(prikeyValue, getSignData(paramMap));
		}catch(Exception e){
			return null;
		}

	}
	
	/**
	 * 验签
	 * @param paramMap
	 * @param pubkeyValue
	 * @return
	 */
	public static boolean checkSign(Map<String,String> paramMap,String pubkeyValue){
		try{
			return RSAUtil.checksign(pubkeyValue, getSignData(paramMap), paramMap.get("sign"));
		}catch(Exception e){
			return false;
		}
		
	}
	
    public static boolean isnull(String str)
    {
        if (null == str || str.equalsIgnoreCase("null") || str.equals(""))
        {
            return true;
        } else
            return false;
    }

}
