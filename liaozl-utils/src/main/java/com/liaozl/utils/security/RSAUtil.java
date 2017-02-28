package com.liaozl.utils.security;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 1、密钥长度须为1024
 * 2、公钥加密，私钥解密
 * 3、私钥加密，公钥解密
 * 4、私钥加签，公钥验签
 *
 * @author liaozuliang
 * @date 2017-02-28
 */
public class RSAUtil {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    public static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    public static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 随机生成密钥对
     */
    public static Map<String, Object> getRSAKey() {
        Map<String, Object> keyMap = new HashMap<String, Object>();

        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024);

            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

            String publicKeyStr = Base64Util.encode(publicKey.getEncoded());
            String privateKeyStr = Base64Util.encode(privateKey.getEncoded());

            keyMap.put(PUBLIC_KEY, publicKeyStr);
            keyMap.put(PRIVATE_KEY, privateKeyStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return keyMap;
    }

    public static RSAPrivateKey getPrivateKey(String base64PrivateKeyStr) {
        try {
            byte[] buffer = Base64Util.decodeToByteArray(base64PrivateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static RSAPublicKey getPublicKey(String base64PublicKeyStr) {
        try {
            byte[] buffer = Base64Util.decodeToByteArray(base64PublicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String sign(String data, String base64PrivateKey) throws Exception {
        byte[] keyBytes = Base64Util.decodeToByteArray(base64PrivateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data.getBytes("UTF-8"));

        return Base64Util.encode(signature.sign());
    }

    public static boolean checkSign(String data, String base64PublicKey, String base64SignedStr) throws Exception {
        byte[] keyBytes = Base64Util.decodeToByteArray(base64PublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data.getBytes("UTF-8"));

        return signature.verify(Base64Util.decodeToByteArray(base64SignedStr));
    }

    public static byte[] decryptByPrivateKey(byte[] encryptedData, String base64PrivateKey) throws Exception {
        byte[] keyBytes = Base64Util.decodeToByteArray(base64PrivateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);

        int inputLen = encryptedData.length;
        byte[] result = new byte[(encryptedData.length / 128 + 1) * 117];

        int offSet = 0;
        byte[] cache;
        int i = 0;
        int len = 0;

        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
            for (byte b : cache) {
                result[len++] = b;
            }
        }

        return result;
    }

    public static String decryptByPrivateKey(String base64EncryptedData, String base64PrivateKey) throws Exception {
        return new String(decryptByPrivateKey(Base64Util.decodeToByteArray(base64EncryptedData), base64PrivateKey), "UTF-8").trim();
    }

    public static byte[] decryptByPublicKey(byte[] encryptedData, String base64PublicKey) throws Exception {
        byte[] keyBytes = Base64Util.decodeToByteArray(base64PublicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);

        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int offSet = 0;
        byte[] cache;
        int i = 0;

        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }

        byte[] decryptedData = out.toByteArray();
        out.close();

        return decryptedData;
    }

    public static String decryptByPublicKey(String base64EncryptedData, String base64PublicKey) throws Exception {
        return new String(decryptByPublicKey(Base64Util.decodeToByteArray(base64EncryptedData), base64PublicKey), "UTF-8").trim();
    }

    public static byte[] encryptByPublicKey(byte[] data, String base64PublicKey) throws Exception {
        byte[] keyBytes = Base64Util.decodeToByteArray(base64PublicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);

        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;

        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }

        byte[] encryptedData = out.toByteArray();
        out.close();

        return encryptedData;
    }

    public static String encryptByPublicKey(String data, String base64PublicKey) throws Exception {
        return Base64Util.encode(encryptByPublicKey(data.getBytes("UTF-8"), base64PublicKey));
    }

    public static byte[] encryptByPrivateKey(byte[] data, String base64PrivateKey) throws Exception {
        byte[] keyBytes = Base64Util.decodeToByteArray(base64PrivateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);

        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int offSet = 0;
        byte[] cache;
        int i = 0;

        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }

        byte[] encryptedData = out.toByteArray();
        out.close();

        return encryptedData;
    }

    public static String encryptByPrivateKey(String data, String base64PrivateKey) throws Exception {
        return Base64Util.encode(encryptByPrivateKey(data.getBytes("UTF-8"), base64PrivateKey));
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> keyMap = getRSAKey();
        String publicKey = keyMap.get(PUBLIC_KEY).toString();
        String privateKey = keyMap.get(PRIVATE_KEY).toString();
        System.out.println("公钥：" + publicKey);
        System.out.println("私钥：" + privateKey);

        String content = "测试一下公钥加密私钥解密，私钥加密公钥解密，哈哈12312abc！";
        System.out.println("原文：" + content);

        System.out.println("==============公钥加密, 私钥解密=============");
        String publicKeyEncryptStr = encryptByPublicKey(content, publicKey);
        System.out.println("公钥加密：" + publicKeyEncryptStr);
        String privateKeyDecryptStr = decryptByPrivateKey(publicKeyEncryptStr, privateKey);
        System.out.println("私钥解密：" + privateKeyDecryptStr);
        System.out.println(content.equals(privateKeyDecryptStr));

        System.out.println("==============私钥加密, 公钥解密=============");
        String privateKeyEncryptStr = encryptByPrivateKey(content, privateKey);
        System.out.println("私钥加密：" + privateKeyEncryptStr);
        String publicKeyDecryptStr = decryptByPublicKey(privateKeyEncryptStr, publicKey);
        System.out.println("公钥解密：" + publicKeyDecryptStr);
        System.out.println(content.equals(publicKeyDecryptStr));

        System.out.println("==============私钥加签, 公钥验签=============");
        String privateKeySignStr = sign(content, privateKey);
        System.out.println("私钥加签：" + privateKeySignStr);
        boolean checkSign = checkSign(content, publicKey, privateKeySignStr);
        System.out.println("公钥验签：" + checkSign);

    }

}
