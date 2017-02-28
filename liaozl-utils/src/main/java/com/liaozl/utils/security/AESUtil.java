package com.liaozl.utils.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * @author liaozuliang
 * @date 2017-02-27
 */
public class AESUtil {

    private AESUtil() {

    }

    public static byte[] decrypt(byte[] content, String decryptPassword) throws Exception {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(decryptPassword.getBytes("UTF-8")));

            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static byte[] encrypt(String content, String encryptPassword) throws Exception {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(encryptPassword.getBytes("UTF-8")));

            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes("UTF-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String decryptFromBase64(String ciphertext, String decryptPassword) throws Exception {
        byte[] cipher = Base64Util.decodeToByteArray(ciphertext);
        byte[] plainText = decrypt(cipher, decryptPassword);
        return new String(plainText, "UTF-8");
    }

    public static String encryptToBase64(String plaintext, String encryptPassword) throws Exception {
        byte[] cipher = encrypt(plaintext, encryptPassword);
        return Base64Util.encode(cipher);
    }

    public static void main(String[] args) throws Exception {
        String content = "Hello!12345你好";
        String password = "12345678a";

        System.out.println("加密前：" + content);
        String encryptStr = encryptToBase64(content, password);
        System.out.println("加密后：" + encryptStr);

        String decryptStr = decryptFromBase64(encryptStr, password);
        System.out.println("解密后：" + decryptStr);

        System.out.println(content.equals(decryptStr));
    }
}