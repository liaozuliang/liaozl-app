package com.liaozl.utils.security;

import java.util.Map;

/**
 * @author liaozuliang
 * @date 2017-02-28
 */
public class SignUtil {

    private SignUtil() {

    }

    public static String sign(String data, String base64PrivateKey) {
        try {
            return RSAUtil.sign(data, base64PrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkSign(String data, String base64PublicKey, String base64SignedStr) {
        try {
            return RSAUtil.checkSign(data, base64PublicKey, base64SignedStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> keyMap = RSAUtil.getRSAKey();
        String publicKey = keyMap.get(RSAUtil.PUBLIC_KEY).toString();
        String privateKey = keyMap.get(RSAUtil.PRIVATE_KEY).toString();
        System.out.println("公钥：" + publicKey);
        System.out.println("私钥：" + privateKey);

        String content = "测试一下公钥加密私钥解密，私钥加密公钥解密，哈哈12312abc！";
        System.out.println("原文：" + content);

        System.out.println("==============私钥加签, 公钥验签=============");
        String privateKeySignStr = sign(content, privateKey);
        System.out.println("私钥加签：" + privateKeySignStr);
        boolean checkSign = checkSign(content, publicKey, privateKeySignStr);
        System.out.println("公钥验签：" + checkSign);
    }
}
