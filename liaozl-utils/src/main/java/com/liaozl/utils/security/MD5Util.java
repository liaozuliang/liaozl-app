package com.liaozl.utils.security;

import java.security.MessageDigest;

/**
 * @author liaozuliang
 * @date 2017-02-27
 */
public class MD5Util {

    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private MD5Util() {

    }

    public static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    public static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public static String getMD5Code(String str) throws Exception {
        String resultString = null;
        try {
            //将给定字符串追加一个静态字符串，以提高复杂度
            resultString = new String(str);
            MessageDigest md = MessageDigest.getInstance("MD5");

            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(resultString.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return resultString;
    }

    public static void main(String[] args) throws Exception {
        String time = "haha123456";
        System.out.println("明文：" + time);
        String md5 = MD5Util.getMD5Code(time);
        System.out.println("密文：" + md5);
    }
}
