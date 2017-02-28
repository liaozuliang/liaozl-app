package com.liaozl.utils.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * @author liaozuliang
 * @date 2017-02-27
 */
public class DESUtil {

    public static byte[] encrypt(String str, String encryptKey) throws Exception {
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(encryptKey.getBytes("UTF-8"));

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            javax.crypto.SecretKey secretKey = keyFactory.generateSecret(dks);

            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);

            return cipher.doFinal(str.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String encryptToBase64(String str, String encryptKey) throws Exception {
        return Base64Util.encode(encrypt(str, encryptKey));
    }

    public static String decrypt(byte[] str, String decryptKey) throws Exception {
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(decryptKey.getBytes("UTF-8"));

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            javax.crypto.SecretKey key = keyFactory.generateSecret(dks);

            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, sr);
            byte decryptedData[] = cipher.doFinal(str);

            return new String(decryptedData, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String decryptFromBase64(String str, String encryptKey) throws Exception {
        return decrypt(Base64Util.decodeToByteArray(str), encryptKey);
    }

    public static void main(String[] args) throws Exception {
        String key = "12345678abcdefg432";
        String content = "hahaGG123哈哈";

        System.out.println("明文：" + content);
        System.out.println("密钥：" + key);

        String encryptStr = encryptToBase64(content, key);
        System.out.println("密文：" + encryptStr);

        String decryptStr = decryptFromBase64(encryptStr, key);
        System.out.println("解密后：" + decryptStr);

        System.out.println(content.equals(decryptStr));
    }
}