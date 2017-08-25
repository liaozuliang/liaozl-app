package com.liaozl.utils.security;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.security.MessageDigest;

/**
 * @author liaozuliang
 * @date 2017-08-25
 */
public class AESUtil2 {

    private static final Logger logger = (Logger) LoggerFactory
            .getLogger(AESUtil2.class);

    public static String encrypt(String jsonString, String key) {
        if (!StringUtils.isEmpty(jsonString)) {
            try {
                byte[] bytes = jsonString.getBytes();
                bytes = encrypt(bytes, key.getBytes("UTF-8"), false,
                        key.getBytes("UTF-8"));
                jsonString = bytes == null ? jsonString : Base64.encodeBase64String(bytes);
            } catch (Exception e) {
                logger.error("加密错误jsonString=" + jsonString + ",key=" + key, e);
            }
        }
        return jsonString;

    }

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param key     加密密码
     * @param md5Key  是否对key进行md5加密
     * @param iv      加密向量
     * @return 加密后的字节数据
     */
    public static byte[] encrypt(byte[] content, byte[] key, boolean md5Key,
                                 byte[] iv) {
        try {
            if (md5Key) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                key = md.digest(key);
            }
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // "算法/模式/补码方式"
            IvParameterSpec ivps = new IvParameterSpec(iv);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivps);
            return cipher.doFinal(content);
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
        }
        return null;
    }

    public static String decrypt(String jsonString, String key) {
        if (!StringUtils.isEmpty(jsonString)) {
            try {
                JSONObject.parseObject(jsonString);
            } catch (Exception ex) {
                try {
                    byte[] bytes = jsonString.getBytes();
                    bytes = Base64.decodeBase64(bytes);
                    bytes = decrypt(bytes, key.getBytes("UTF-8"), false, key.getBytes("UTF-8"));
                    jsonString = bytes == null ? jsonString : new String(bytes, "UTF-8");
                } catch (Exception e) {
                    logger.error("解密错误jsonString=" + jsonString + ",key=" + key, e);
                }
            }

        }
        return jsonString;
    }

    public static byte[] decrypt(byte[] content, byte[] key, boolean md5Key,
                                 byte[] iv) {
        try {
            if (md5Key) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                key = md.digest(key);
            }
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // "算法/模式/补码方式"
            IvParameterSpec ivps = new IvParameterSpec(iv);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivps);
            return cipher.doFinal(content);
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String key = "A1B2C3D4E5F60708";

        String source = "jsonString={UserId:\"310723AF-A705-48EC-BD78-855FF725BC1F\",HaveOtherIncome:\"True\",OtherIncome:\"200\",CompanyName:\"团贷网\",Position:\"工程师\",Deparment:\"技术中心\",JobProvince:\"广东省\",JobCity:\"东莞市\",JobArea:\"南城区\",PositionLevel:\"1\",IncomeTypeId:\"1\",WorkEmail:\"test@tuandau.com\",CompanyProvince:\"广东省\",CompanyCity:\"东莞市\",CompanyArea:\"南城区\",CompanyAddress:\"民间金融街\",CompanyTypeId:\"1\",CompanyIndustryTypeId:\"1\",CompanySizeTypeId:\"2\",WorkYears:\"1\",CompanyPhone:\"0768999999\",UserTypeId:\"1\",IsComplete:\"1\"}";
        System.out.println("原文: " + source);

        String encryptData = encrypt(source, key);
        System.out.println("加密后: " + encryptData);

        String decryptData = decrypt(encryptData, key);
        System.out.println("解密后: " + decryptData);
        System.out.println(decryptData.equals(source));

        String decryptData2 = decrypt("7t15oXCgYQ+ivHHEuSgHxKdR67ebxUHs2w0jfUvETAUrHYiDM8iW0WNYzzsBB78SqvGzsdKRuU0njFyAJhSXV4MGDapJqbFTi831BcwK0IxLwQslhiZ0stPNh2gVQMPFDE8RlMZWZJq+8dguAOU2UyOZChHfiL8rPcLo1/fG6Lg=", "A0B5C2D4E7F90301");
        System.out.println("解密后2: "+decryptData2);
    }
}
