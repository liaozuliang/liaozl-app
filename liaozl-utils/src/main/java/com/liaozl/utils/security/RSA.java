package com.liaozl.utils.security;


import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 只能公钥加密、私钥解密，适用多种密钥长度
 * @author liaozuliang
 * @date 2017-02-27
 */
public final class RSA {

    /**
     * RSA密钥长度必须是64的倍数，在1024~65536之间。默认是1024
     */
    public static final int KEY_SIZE = 2048;

    private RSA() {

    }

    /**
     * 随机生成密钥对
     */
    public static Map<String, Object> getRSAKey() {
        Map<String, Object> keyMap = new HashMap<String, Object>();

        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(KEY_SIZE);

            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

            String privateKeyStr = Base64Util.encode(privateKey.getEncoded());
            String publicKeyStr = Base64Util.encode(publicKey.getEncoded());

            keyMap.put("privateKey", privateKeyStr);
            keyMap.put("publicKey", publicKeyStr);
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

    public static String encryptByPublicKey(RSAPublicKey publicKey, String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // 模长
        int key_len = publicKey.getModulus().bitLength() / 8;

        // 加密数据长度 <= 模长-11
        String[] datas = splitString(data, key_len - 11);

        //如果明文长度大于模长-11则要分组加密
        StringBuilder sb = new StringBuilder();

        for (String s : datas) {
            sb.append(bcd2Str(cipher.doFinal(s.getBytes())));
        }

        return sb.toString();
    }

    public static String decryptByPrivateKey(RSAPrivateKey privateKey, String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        //模长
        int key_len = privateKey.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);

        //如果密文长度大于模长则要分组解密
        StringBuilder sb = new StringBuilder();
        byte[][] arrays = splitArray(bcd, key_len);

        for (byte[] arr : arrays) {
            sb.append(new String(cipher.doFinal(arr)));
        }

        return sb.toString();
    }

    /**
     * ASCII码转BCD码
     */
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    public static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }

    /**
     * BCD转字符串
     */
    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    /**
     * 拆分字符串
     */
    public static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    /**
     * 拆分数组
     */
    public static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    public static void main(String[] args) throws Exception {
        String content = "哈哈测试一下RSA公钥加密私钥解密，私钥加密公钥解密123_！发生的abc！";
        System.out.println("原文：" + content);

        Map<String, Object> keyMap = getRSAKey();

        String base64PublicKey = keyMap.get("publicKey").toString();
        RSAPublicKey publicKey = getPublicKey(base64PublicKey);
        System.out.println("公钥：" + base64PublicKey);

        String base64PrivateKey = keyMap.get("privateKey").toString();
        RSAPrivateKey privateKey = getPrivateKey(base64PrivateKey);
        System.out.println("私钥：" + base64PrivateKey);

        System.out.println("=================公钥加密, 私钥解密===============");
        String publicKeyEncryptStr = encryptByPublicKey(publicKey, content);
        System.out.println("公钥加密密文：" + publicKeyEncryptStr);

        String privateKeyDecryptStr = decryptByPrivateKey(privateKey, publicKeyEncryptStr);
        System.out.println("私钥解密密文：" + privateKeyDecryptStr);

        System.out.println(content.equals(privateKeyDecryptStr));
    }

}