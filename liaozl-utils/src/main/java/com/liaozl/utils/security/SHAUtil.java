package com.liaozl.utils.security;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author liaozuliang
 * @date 2017-08-25
 */
public class SHAUtil {

    public static String SHA1Encrypt(String data) throws Exception {
        //指定sha1算法
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(data.getBytes("UTF-8"));

        //获取字节数组
        byte messageDigest[] = digest.digest();

        // Create Hex String
        StringBuffer hexString = new StringBuffer();

        // 字节数组转换为 十六进制 数
        for (int i = 0; i < messageDigest.length; i++) {
            String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }

        return hexString.toString().toUpperCase();
    }
}
