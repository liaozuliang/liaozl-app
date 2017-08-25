package com.liaozl.utils.security;

import com.liaozl.utils.http.JsoupUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liaozuliang
 * @date 2017-08-25
 */
public class NiiwooApiRequest {

    private static final String gateway = "http://localhost:3002/niiwoo-api/niwoportservice.svc/";
    //private static final String gateway = "http://218.17.157.50:5002/niiwoo-api/niwoportservice.svc/";
    private static final String appKey = "00002";
    private static final String appKeySecret = "A0B5C2D4E7F90301";

    public static void doPost(String method, String version, String token, String jsonStrParam) throws Exception {
        jsonStrParam = AESUtil2.encrypt(jsonStrParam, appKeySecret);

        StringBuffer signStrSB = new StringBuffer();
        signStrSB.append(appKeySecret);
        signStrSB.append("appKey").append(appKey);
        signStrSB.append("jsonString").append(jsonStrParam);

        if (StringUtils.isNotBlank(token)) {
            signStrSB.append("userToken").append(token);
        }

        signStrSB.append("v").append(version);
        signStrSB.append(appKeySecret);

        String sign = SHAUtil.SHA1Encrypt(signStrSB.toString());

        Map<String, String> params = new HashMap<String, String>();
        params.put("appKey", appKey);
        params.put("v", version);
        params.put("sign", "12");
        params.put("jsonString", jsonStrParam);

        if (StringUtils.isNotBlank(token)) {
            params.put("userToken", token);
        }

        String result = JsoupUtil.doPost2(gateway + method, params);
        System.out.println(result);
    }

    public static void main(String[] args) throws Exception {
        String method = "PostThirdAlipayAuthorize";
        String version = "1.0";
        String token = "";
        String jsonString = "{\"realName\":\"廖祖亮\", \"identityCard\":\"360724198812012013\", \"state\":\"liaozl\", \"channel\":\"1\", \"callBackUrl\":\"xxxxx\"}";

        doPost(method, version, token, jsonString);

        method = "GetUserCreditInfoAndState";
        version = "4.3.0";
        token = "99779076B1C874BEF0224A9BD872219C";
        jsonString = "{\"IsGetCreditResult\":\"0\", \"TerminalType\":\"1\"}";

        doPost(method, version, token, jsonString);

    }
}

