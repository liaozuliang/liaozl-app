package com.liaozl.utils.security;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author liaozuliang
 * @date 2017-02-27
 */
public class Base64Util {

    private Base64Util() {
    }

    public static String encode(String str) throws Exception {
        return encode(str.getBytes("UTF-8"));
    }

    public static String encode(byte[] str) throws Exception {
        try {
            return new BASE64Encoder().encode(str);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String decode(String str) throws Exception {
        return new String(decodeToByteArray(str), "UTF-8");
    }

    public static byte[] decodeToByteArray(String str) throws Exception {
        try {
            return new BASE64Decoder().decodeBuffer(str);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {
        String str = "哈哈测试一下base64喽！";
        System.out.println("原文：" + str);

        String encodeStr = encode(str);
        System.out.println("base64编码后：" + encodeStr);

        String decodeStr = decode(encodeStr);
        System.out.println("base64解码后：" + decodeStr);

        System.out.println(str.equals(decodeStr));
    }

}